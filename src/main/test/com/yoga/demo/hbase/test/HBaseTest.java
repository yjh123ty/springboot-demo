package com.yoga.demo.hbase.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;

import com.yoga.demo.domain.shiro.UserInfo;

/**
 *  hbase数据结构的总体设计思路：
 *  	在保存数据时，使用Listput<Put> listput = new ArrayList<Put>();
 *  	listput里面存放的是rowkey为 indexput1,indexput2...等的索引key，以及最后的dataput
 *  	索引key的构造 ： 业务类型 +
 *  	构造一个rowkey添加到scan中，就是一次查询.
 *  	查询到匹配的rowkey，将rowkey的后半段（构造的时候也是如此构造，可以通过时间戳的最后1位拼接上例如“000”，再拼接一个业务键的形式，如：i + "000" + userId()）的数据获取，例如这里的userId
 *  	再根据这个 userId 作为 rowkey的一部分，去查询真正数据存放的列族
 *  	
 * 
 * @author yoga
 */
public class HBaseTest {
	private static Configuration conf = HBaseConfiguration.create();
	/**
	 * column_family
	 */
	private static String COLUMN_FAMILY = "user_test";
	
	// 声明静态配置
    static {
    	conf.set("hbase.rootdir", "hdfs://192.168.4.128:9000/hbase");
    	conf.set("hbase.zookeeper.quorum", "192.168.4.128:2181,192.168.4.128:2182,192.168.4.128:2183");
    }
    
    
    /*
     * 创建表
     * 
     * @tableName 表名
     * 
     * @family 列族列表
     */
    public static void creatTable(String tableName, String[] family)
            throws Exception {
        HBaseAdmin admin = new HBaseAdmin(conf);
        HTableDescriptor desc = new HTableDescriptor(tableName);
        for (int i = 0; i < family.length; i++) {
            desc.addFamily(new HColumnDescriptor(family[i]));
        }
        if (admin.tableExists(tableName)) {
            System.err.println("table Exists!");
            System.exit(0);
        } else {
            admin.createTable(desc);
            System.err.println("create table Success!");
        }
    }
    
    /*
     * 为表添加数据（适合知道有多少列族的固定表）
     * 
     * @rowKey rowKey
     * 
     * @tableName 表名
     * 
     * @column1 第一个列族列表
     * 
     * @value1 第一个列的值的列表
     * 
     * @column2 第二个列族列表
     * 
     * @value2 第二个列的值的列表
     */
    public static void addData(String rowKey, String tableName,
            String[] column1, String[] value1, String[] column2, String[] value2)
            throws IOException {
        Put put = new Put(Bytes.toBytes(rowKey));// 设置rowkey
        HTable table = new HTable(conf, Bytes.toBytes(tableName));// HTabel负责跟记录相关的操作如增删改查等//
                                                                    // 获取表
        HColumnDescriptor[] columnFamilies = table.getTableDescriptor() // 获取所有的列族
                .getColumnFamilies();

        for (int i = 0; i < columnFamilies.length; i++) {
            String familyName = columnFamilies[i].getNameAsString(); // 获取列族名
            if (familyName.equals("article")) { // article列族put数据
                for (int j = 0; j < column1.length; j++) {
                    put.add(Bytes.toBytes(familyName),
                            Bytes.toBytes(column1[j]), Bytes.toBytes(value1[j]));
                }
            }
            if (familyName.equals("author")) { // author列族put数据
                for (int j = 0; j < column2.length; j++) {
                    put.add(Bytes.toBytes(familyName),
                            Bytes.toBytes(column2[j]), Bytes.toBytes(value2[j]));
                }
            }
        }
        table.put(put);
        System.err.println("add data Success!");
    }
    
    /*
     * 
     * @rowKey rowKey
     * 
     * @tableName 表名
     * 
     * @column1 列族列表
     * 
     * @value1 列的值的列表
     * 
     * @param familyName 列族名
     * 
     * @throws IOException
     */
    public static void addData2(String rowKey, String tableName, String familyName,
            String[] column1, String[] value1)
            throws IOException {
        Put put = new Put(Bytes.toBytes(rowKey));// 设置rowkey
        HTable table = new HTable(conf, Bytes.toBytes(tableName));// HTabel负责跟记录相关的操作如增删改查等//
        for (int j = 0; j < column1.length; j++) {
            put.add(Bytes.toBytes(familyName),
                    Bytes.toBytes(column1[j]), Bytes.toBytes(value1[j]));
        }
        table.put(put);
        System.err.println("add data Success!");
    }
    
    
    public static void addTestData(String tableName, String familyName, UserInfo user)
            throws IOException {
    	//TODO
        Connection connection = ConnectionFactory.createConnection(conf);
        
        // 构造搜索的索引键
        String valueKey = String.valueOf(user.getUid());
        
        List<Put> listPut = new ArrayList<Put>();
        Put indexPut1 = new Put(Bytes.toBytes("idx_1_".concat(valueKey)));//模拟业务键1
        indexPut1.addColumn(Bytes.toBytes(familyName), Bytes.toBytes("i"), Bytes.toBytes(""));
        listPut.add(indexPut1);
        
        Put indexPut2 = new Put(Bytes.toBytes("idx_2_".concat(valueKey)));//模拟业务键2
        indexPut2.addColumn(Bytes.toBytes(familyName), Bytes.toBytes("i"), Bytes.toBytes(""));
        listPut.add(indexPut2);
        
        Put indexPut3 = new Put(Bytes.toBytes("idx_3_".concat(valueKey)));//模拟业务键3
        indexPut3.addColumn(Bytes.toBytes(familyName), Bytes.toBytes("i"), Bytes.toBytes(""));
        listPut.add(indexPut3);
        
        // 具体数据 ： 构造保存的数据键 rowkey, 使用创建时间作为key
//    	String valueKey = new SimpleDateFormat("yyyyMMddHHmmss").format(user.getCreateTime());
        
 		Put put = new Put(Bytes.toBytes(valueKey));
		put.addColumn(Bytes.toBytes(familyName), Bytes.toBytes("username"), Bytes.toBytes(user.getUsername()));
		put.addColumn(Bytes.toBytes(familyName), Bytes.toBytes("password"), Bytes.toBytes(user.getPassword()));
 		listPut.add(put);
 		
		try {
			Table table = connection.getTable(TableName.valueOf(tableName));
			table.put(listPut);
			System.err.println("add data Success!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != connection)
					connection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
    
    
    
    /*
     * 根据rwokey查询
     * 
     * @rowKey rowKey
     * 
     * @tableName 表名
     */
    public static Result getResult(String tableName, String rowKey)
            throws IOException {
        Get get = new Get(Bytes.toBytes(rowKey));
        HTable table = new HTable(conf, Bytes.toBytes(tableName));// 获取表
        Result result = table.get(get);
        for (KeyValue kv : result.list()) {
            System.err.println("family:" + Bytes.toString(kv.getFamily()));
            System.err
                    .println("qualifier:" + Bytes.toString(kv.getQualifier()));
            System.err.println("value:" + Bytes.toString(kv.getValue()));
            System.err.println("Timestamp:" + kv.getTimestamp());
            System.err.println("-------------------------------------------");
        }
        return result;
    }
    
    /*
     * 遍历查询hbase表
     * 
     * @tableName 表名
     */
    public static void getResultScann(String tableName) throws IOException {
        Scan scan = new Scan();
        scan.addFamily(Bytes.toBytes(COLUMN_FAMILY));
//		scan.addColumn(Bytes.toBytes(COLUMN_FAMILY), Bytes.toBytes("username-1"));
		
		// 指定扫描分区(增加前缀)
//		FilterList flist = new FilterList(FilterList.Operator.MUST_PASS_ALL);
//		flist.addFilter(new PrefixFilter(Bytes.toBytes("1")));
//		scan.setFilter(flist);
		
        ResultScanner rs = null;
        
        Connection connection = ConnectionFactory.createConnection(conf);
    	Table table = connection.getTable(TableName.valueOf(tableName));
    	
        try {
            rs = table.getScanner(scan);
            for (Result r : rs) {
                for (KeyValue kv : r.list()) {
                    System.err.println("row:" + Bytes.toString(kv.getRow()));
                    System.err.println("family:"
                            + Bytes.toString(kv.getFamily()));
                    System.err.println("qualifier:"
                            + Bytes.toString(kv.getQualifier()));
                    System.err
                            .println("value:" + Bytes.toString(kv.getValue()));
                    System.err.println("timestamp:" + kv.getTimestamp());
                    System.err
                            .println("-------------------------------------------");
                }
            }
        } finally {
            rs.close();
        }
    }

    /*TODO
     * 遍历查询hbase表
     * 
     * @tableName 表名
     */
    public static void getResultScannForUserInfo(String tableName, String familyName, String rowPrefixFilter, Class<?> clz) throws IOException {
    	Connection connection = ConnectionFactory.createConnection(conf);
    	Table table = connection.getTable(TableName.valueOf(tableName));
    	
        Scan scan = new Scan();
//        scan.setStartRow(Bytes.toBytes(start_rowkey));//从这个rowkey开始，这个rowkey也包含在内
//        scan.setStopRow(Bytes.toBytes(stop_rowkey));//截止到的rowkey，注意，这个rowkey不会纳入
        scan.addFamily(Bytes.toBytes(familyName));
		
		// 筛选匹配行键的前缀成功的行
        FilterList flist = new FilterList(FilterList.Operator.MUST_PASS_ALL);
		if (StringUtils.isNotEmpty(rowPrefixFilter)) {
			flist.addFilter(new PrefixFilter(Bytes.toBytes(rowPrefixFilter)));
		}
		scan.setFilter(flist);
        
        // 筛选出前缀匹配的列  
//        Filter cpf = new ColumnPrefixFilter(Bytes.toBytes("password"));
//        scan.setFilter(cpf);
		
		ResultScanner rs = null;
        try {
            rs = table.getScanner(scan);
            for (Result result : rs) {
            	//获取索引 row key
				String idxRow = Bytes.toString(result.getRow());
				String dataRow = idxRow.substring(idxRow.lastIndexOf("_") + 1);
				System.err.println(dataRow);
				//根据rowkey 获取数据
				Get get = new Get(Bytes.toBytes(dataRow));
				Result dataResult = table.get(get);
				//查看对应属性的列
//				for (int i = 0; i < 10; i++) {
//					get.addColumn(Bytes.toBytes(familyName), Bytes.toBytes("username-" + i));
//				}
				System.err.println(dataResult);
				byte[] byteValues = dataResult.getValue(Bytes.toBytes(familyName), Bytes.toBytes("username"));
				byte[] byteValues2 = dataResult.getValue(Bytes.toBytes(familyName), Bytes.toBytes("password"));
				System.err.println("******* start *******");
				if (null != byteValues)
					System.err.println("username ： " + Bytes.toString(byteValues));
				if (null != byteValues)
					System.err.println("password ： " + Bytes.toString(byteValues2));
				System.err.println("******* end *******");
				System.err.println();
            }
        } finally {
            rs.close();
        }
    }

    /*
     * 查询表中的某一列
     * 
     * @tableName 表名
     * 
     * @rowKey rowKey
     */
    public static void getResultByColumn(String tableName, String rowKey,
            String familyName, String columnName) throws IOException {
        HTable table = new HTable(conf, Bytes.toBytes(tableName));
        Get get = new Get(Bytes.toBytes(rowKey));
        get.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(columnName)); // 获取指定列族和列修饰符对应的列
        Result result = table.get(get);
        for (KeyValue kv : result.list()) {
            System.err.println("family:" + Bytes.toString(kv.getFamily()));
            System.err
                    .println("qualifier:" + Bytes.toString(kv.getQualifier()));
            System.err.println("value:" + Bytes.toString(kv.getValue()));
            System.err.println("Timestamp:" + kv.getTimestamp());
            System.err.println("-------------------------------------------");
        }
    }

    /*
     * 更新表中的某一列
     * 
     * @tableName 表名
     * 
     * @rowKey rowKey
     * 
     * @familyName 列族名
     * 
     * @columnName 列名
     * 
     * @value 更新后的值
     */
    public static void updateTable(String tableName, String rowKey,
            String familyName, String columnName, String value)
            throws IOException {
        HTable table = new HTable(conf, Bytes.toBytes(tableName));
        Put put = new Put(Bytes.toBytes(rowKey));
        put.add(Bytes.toBytes(familyName), Bytes.toBytes(columnName),
                Bytes.toBytes(value));
        table.put(put);
        System.err.println("update table Success!");
    }

    /*
     * 查询某列数据的多个版本
     * 
     * @tableName 表名
     * 
     * @rowKey rowKey
     * 
     * @familyName 列族名
     * 
     * @columnName 列名
     */
    public static void getResultByVersion(String tableName, String rowKey,
            String familyName, String columnName) throws IOException {
        HTable table = new HTable(conf, Bytes.toBytes(tableName));
        Get get = new Get(Bytes.toBytes(rowKey));
        get.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(columnName));
        get.setMaxVersions(5);
        Result result = table.get(get);
        for (KeyValue kv : result.list()) {
            System.err.println("family:" + Bytes.toString(kv.getFamily()));
            System.err
                    .println("qualifier:" + Bytes.toString(kv.getQualifier()));
            System.err.println("value:" + Bytes.toString(kv.getValue()));
            System.err.println("Timestamp:" + kv.getTimestamp());
            System.err.println("-------------------------------------------");
        }
        /*
         * List<?> results = table.get(get).list(); Iterator<?> it =
         * results.iterator(); while (it.hasNext()) {
         * System.err.println(it.next().toString()); }
         */
    }

    /*
     * 删除指定的列
     * 
     * @tableName 表名
     * 
     * @rowKey rowKey
     * 
     * @familyName 列族名
     * 
     * @columnName 列名
     */
    public static void deleteColumn(String tableName, String rowKey,
            String falilyName, String columnName) throws IOException {
        HTable table = new HTable(conf, Bytes.toBytes(tableName));
        Delete deleteColumn = new Delete(Bytes.toBytes(rowKey));
        deleteColumn.deleteColumns(Bytes.toBytes(falilyName),
                Bytes.toBytes(columnName));
        table.delete(deleteColumn);
        System.err.println(falilyName + ":" + columnName + "is deleted!");
    }

    /*
     * 删除指定的列
     * 
     * @tableName 表名
     * 
     * @rowKey rowKey
     */
    public static void deleteAllColumn(String tableName, String rowKey)
            throws IOException {
        HTable table = new HTable(conf, Bytes.toBytes(tableName));
        Delete deleteAll = new Delete(Bytes.toBytes(rowKey));
        table.delete(deleteAll);
        System.err.println("all columns are deleted!");
    }

    /*
     * 删除表
     * 
     * @tableName 表名
     */
    public static void deleteTable(String tableName) throws IOException {
        HBaseAdmin admin = new HBaseAdmin(conf);
        admin.disableTable(tableName);
        admin.deleteTable(tableName);
        System.err.println(tableName + "is deleted!");
    }
	
	public static void main(String[] args) throws Exception {
		UserInfo user = new UserInfo();
		user.setUid(1);
		user.setUsername("yu子酱");
		user.setPassword("123");
		
		// 创建表
//        String tableName = "yoga_test";
        String tableName = "test";
        
//        String myRowkey = "myRowkey1";
//        String myRowkey = "rowkey_test";
        
//        creatTable(tableName, new String[]{COLUMN_FAMILY});
        
        // 为表添加数据
//        String[] column1 = { "title", "content", "tag" };
//        String[] value1 = {
//                "Head First HBase",
//                "HBase is the Hadoop database. Use it when you need random, realtime read/write access to your Big Data.",
//                "Hadoop,HBase,NoSQL" };
//        String[] column2 = { "username", "password" };
//        String[] value2 = { "nicholas", "123" };
//        addData(myRowkey, tableName, column1, value1, column2, value2);
//        addData("rowkey2", tableName, column1, value1, column2, value2);
//        addData("rowkey3", tableName, column1, value1, column2, value2);
        
        //添加数据，方法2(针对具体的列簇)
//        String familyName = "sys_role";
//        String[] column1 = { "role_id", "role_name" };
//        String[] value1 = { "1", "admin" };
//        addData2("myRowkey1", tableName, familyName, column1, value1);
        
        //添加数据，方法3
//        addTestData(tableName, "user_test", user);
        
        // 遍历查询
//        getResultScann(tableName);
        
        // 根据row key范围遍历查询
        getResultScannForUserInfo(tableName, COLUMN_FAMILY, "idx_3_", UserInfo.class);

        // 查询
//        getResult(tableName, myRowkey);

        // 查询某一列的值
//        getResultByColumn(tableName, myRowkey, "user_test", "username-9");

        // 更新列
//        updateTable(tableName, myRowkey, "user_info", "username", "yu");

        // 查询某一列的值
//        getResultByColumn(tableName, myRowkey, "user_info", "username");

        // 查询某列的多版本
//        getResultByVersion(tableName, myRowkey, "user_info", "username");

        // 删除一列
//        deleteColumn(tableName, myRowkey, "user_info", "username");

        // 删除所有列
//        deleteAllColumn(tableName, myRowkey);
        
        // 删除表
//        deleteTable(tableName);
		
	}
}
