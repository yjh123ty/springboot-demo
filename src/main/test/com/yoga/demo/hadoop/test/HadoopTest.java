package com.yoga.demo.hadoop.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

public class HadoopTest {
	
	//删除hadoop上的文件
	private static void deleteFile(final FileSystem fileSystem)
            throws IOException {
        // 删除文件-true/false（文件夹-true）
		String filepath = "/output/wordcount";
		Path path = new Path(filepath);
		if(fileSystem.exists(path)){
			fileSystem.delete(new Path(filepath), true);
			System.err.println("已删除文件 ：" + filepath);
		}else
			System.err.println("文件不存在!");
    }
	
	public static void main(String[] args) throws Exception {
		 String uri = "hdfs://192.168.4.128:9000/";
		 Configuration config = new Configuration();
		 FileSystem fs = FileSystem.get(URI.create(uri), config);
		 
		 //删除文件
		 deleteFile(fs);
		 
		 // 列出hdfs上/input目录下的所有文件和目录
//		 FileStatus[] statuses = fs.listStatus(new Path("/output"));
//		 for (FileStatus status : statuses) {
//		  System.err.println(status);
//		 }
		 
		 // 在hdfs的/input目录下创建一个文件，并写入一行文本
//		 FSDataOutputStream os = fs.create(new Path("/input/test.log"));
//		 os.write("Hello World!".getBytes());
//		 os.flush();
//		 os.close();
		 
		 // 显示在hdfs的/input下指定文件的内容
//		 InputStream is = fs.open(new Path("/output/wordcount/part-r-00000"));
//		 IOUtils.copyBytes(is, System.out, 1024, true);
		 
		 
	}
}
