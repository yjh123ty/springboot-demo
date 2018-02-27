package com.yoga.demo.utils.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alibaba.fastjson.JSONObject;
import com.yoga.demo.domain.shiro.UserInfo;



/**
 * excel 导入/导出服务
 * 
 * @author barry
 *
 */
public class ExcelUtils {
	
	/**
	 * 将excel2007+文件的inputstream转成指定的类的集合
	 * @param clazz
	 * @param inputStream
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @return 返回解析后数据集合
	 */
	public static List<?> parseEntity(Class<?> clazz, InputStream inputStream) throws Exception{
		// 2007版本的excel，用.xlsx结尾
		Workbook wookbook = new XSSFWorkbook(inputStream);
		try {
			List<Object> beans = new ArrayList<Object>();
			Sheet sheet = wookbook.getSheetAt(0);
			DecimalFormat df = new DecimalFormat("#");
			for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
				XSSFRow hssfRow = (XSSFRow) sheet.getRow(rowNum);
				Object bean = clazz.newInstance();
				beans.add(bean);
				Field[] fields = clazz.getDeclaredFields();
				for(int col = 0; col < fields.length; col ++){
					Field field = fields[col];
					XSSFCell cell = hssfRow.getCell(col);
					if(null == cell)
						continue;
					field.setAccessible(true);
					String type = field.getType().toString();
					if(type.endsWith("String")){
						if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
							field.set(bean, df.format(cell.getNumericCellValue()));
						}else{
							field.set(bean, cell.toString());
						}
						continue;
					}
					if(type.endsWith("int") || type.endsWith("Integer")){
						field.setInt(bean, Integer.valueOf(cell.toString()));
						continue;
					}
					if(type.endsWith("double") || type.endsWith("Double")){
						field.setDouble(bean, Double.valueOf(cell.toString()));
						continue;
					}
					if(type.endsWith("long") || type.endsWith("Long")){
						field.setLong(bean, Long.valueOf(cell.toString()));
						continue;
					}
				}
			}
			return beans;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally{
			wookbook.close();
		}
	}
	
	/**
	 * 一个简单的数据导出
	 * @param dataset
	 * 				需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
	 * 				javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
	 * @param out
	 * @throws IOException  
	 */
	public static void exportExcel(Collection<?> dataset, OutputStream out) throws IOException {
		exportExcel("", null, dataset, out, null);
	}
	
	/**
	 * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
	 *
	 * @param title
	 *            表格标题名
	 * @param titleMap
	 *            表头信息（对象属性名称->要显示的标题值)[按顺序添加]
	 * @param dataset
	 *            需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
	 *            javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
	 * @param out
	 *            与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
	 * @param pattern
	 *            如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
	 * @throws IOException 
	 */
	public static void exportExcel(String title, Map<String, String> titleMap, Collection<?> dataset, OutputStream out, String pattern) throws IOException {
		// TODO excel导出的核心代码实现
		if(out != null) {
//			System.out.println("dataset count is "+dataset.size());
			if(StringUtils.isBlank(pattern)) {
				pattern = "yyy-MM-dd";
			}
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		    
			Map<Field, String> fieldAndTypes = null;
			if(CollectionUtils.isNotEmpty(dataset)) {
				Iterator<?> iter = dataset.iterator();
				Class<?> objClass = iter.next().getClass();
	            fieldAndTypes = recursive(objClass, titleMap);
			}
            
            //HSSFWorkbook workbook = new SXSSFWorkbook();
            SXSSFWorkbook workbook = new SXSSFWorkbook();
            //大数据有一定的防OOM能力
            //HSSFSheet sheet;
            SXSSFSheet sheet;
			if(StringUtils.isBlank(title)) {
				sheet = workbook.createSheet("数据表格");
			} else {
				sheet = workbook.createSheet(title);
				//标题样式1 
//				HSSFCellStyle style = workbook.createCellStyle();
				CellStyle style = workbook.createCellStyle();// 样式对象
		        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中

//		        HSSFRow row1 = sheet.createRow(0);
		        Row row1 = sheet.createRow(0);   //--->创建一行
		        /*if(MapUtils.isNotEmpty(fieldAndTypes)) {
		        	// 四个参数分别是：起始行，起始列，结束行，结束列
			        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, fieldAndTypes.size() - 1));
	            } else */if(MapUtils.isNotEmpty(titleMap)) {
	            	sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, titleMap.size() - 1));
	            }
		        
		        row1.setHeightInPoints(25);
//		        HSSFCell cell1 = row1.createCell(0);
		        Cell cell1 = row1.createCell(0);
		        cell1.setCellStyle(style);
		        cell1.setCellValue(title);
			}
			if(MapUtils.isNotEmpty(titleMap)) {
				Row row2 = null;
				if(StringUtils.isBlank(title)) {
					row2 = sheet.createRow(0);   //--->创建第一行
				} else {
					row2 = sheet.createRow(1);   //--->创建第二行
				}
				int headerCellIndex = 0;
				Cell headerCell = null;
				Iterator<String> iterValue = titleMap.values().iterator();
				while (iterValue.hasNext()) {
					headerCell = row2.createCell(headerCellIndex);
					headerCell.setCellValue(iterValue.next());
					headerCellIndex++;
				}
			}
			
			if(MapUtils.isNotEmpty(fieldAndTypes)) {
				int dataIndex = 0;
				if(StringUtils.isNotBlank(title)) {
					dataIndex++;
				}
				if(MapUtils.isNotEmpty(titleMap)) {
					dataIndex++;
				}
				int dataCellIndex = 0;
				Row dataRow = null;
				Cell dataCell = null;
				if(CollectionUtils.isNotEmpty(dataset)) {
					for (Object object : dataset) {
						dataRow = sheet.createRow(dataIndex);
						dataCellIndex = 0;
						if(MapUtils.isNotEmpty(titleMap)) {
						    for(Entry<String, String> keyName : titleMap.entrySet()) {
								dataCell = dataRow.createCell(dataCellIndex);
								for(Entry<Field, String> fieldType : fieldAndTypes.entrySet()) {
									if(fieldType.getKey().getName().equals(keyName.getKey())) {
										if(fieldType.getValue().endsWith("String")) {
											try {
												dataCell.setCellValue((String)fieldType.getKey().get(object));
											} catch (Exception e) {}
										} else if(fieldType.getValue().endsWith("int") || fieldType.getValue().endsWith("Integer")) {
											try {
												dataCell.setCellValue((Integer)fieldType.getKey().get(object));
											} catch (Exception e) {}
										} else if(fieldType.getValue().endsWith("double") || fieldType.getValue().endsWith("Double")) {
											try {
												dataCell.setCellValue((Double)fieldType.getKey().get(object));
											} catch (Exception e) {}
										} else if(fieldType.getValue().endsWith("long") || fieldType.getValue().endsWith("Long")) {
											try {
												dataCell.setCellValue((String)fieldType.getKey().get(object));
											} catch (Exception e) {}
										} else if(fieldType.getValue().endsWith("boolean") || fieldType.getValue().endsWith("Boolean")) {
											try {
												dataCell.setCellValue((Boolean)fieldType.getKey().get(object));
											} catch (Exception e) {}
										} else if(fieldType.getValue().endsWith("BigDecimal")) {
											try {
												dataCell.setCellValue(((BigDecimal)fieldType.getKey().get(object)).toString());
											} catch (Exception e) {
												try {
													dataCell.setCellValue((String)fieldType.getKey().get(object));
												} catch (Exception e1) {}
											}
										} else if(fieldType.getValue().endsWith("Date")) {
											try {
												dataCell.setCellValue(sdf.format((Date)fieldType.getKey().get(object)));
											} catch (Exception e) {
												try {
													dataCell.setCellValue((Date)fieldType.getKey().get(object));
												} catch (Exception e1) {}
											}
										} else {
											try {
												dataCell.setCellValue((String)fieldType.getKey().get(object));
											} catch (Exception e) {}
										}
										continue;
									}
								}
								dataCellIndex++;
							}
						} else {
							for(Entry<Field, String> fieldType : fieldAndTypes.entrySet()) {
								dataCell = dataRow.createCell(dataCellIndex);
								if(fieldType.getValue().endsWith("String")) {
									try {
										dataCell.setCellValue((String)fieldType.getKey().get(object));
									} catch (Exception e) {}
								} else if(fieldType.getValue().endsWith("int") || fieldType.getValue().endsWith("Integer")) {
									try {
										dataCell.setCellValue((Integer)fieldType.getKey().get(object));
									} catch (Exception e) {}
								} else if(fieldType.getValue().endsWith("double") || fieldType.getValue().endsWith("Double")) {
									try {
										dataCell.setCellValue((Double)fieldType.getKey().get(object));
									} catch (Exception e) {}
								} else if(fieldType.getValue().endsWith("long") || fieldType.getValue().endsWith("Long")) {
									try {
										dataCell.setCellValue((String)fieldType.getKey().get(object));
									} catch (Exception e) {}
								} else if(fieldType.getValue().endsWith("boolean") || fieldType.getValue().endsWith("Boolean")) {
									try {
										dataCell.setCellValue((Boolean)fieldType.getKey().get(object));
									} catch (Exception e) {}
								} else if(fieldType.getValue().endsWith("BigDecimal")) {
									try {
										dataCell.setCellValue(((BigDecimal)fieldType.getKey().get(object)).toString());
									} catch (Exception e) {
										try {
											dataCell.setCellValue((String)fieldType.getKey().get(object));
										} catch (Exception e1) {}
									}
								} else if(fieldType.getValue().endsWith("Date")) {
									try {
										dataCell.setCellValue(sdf.format((Date)fieldType.getKey().get(object)));
									} catch (Exception e) {
										try {
											dataCell.setCellValue((Date)fieldType.getKey().get(object));
										} catch (Exception e1) {}
									}
								} else {
									try {
										dataCell.setCellValue((String)fieldType.getKey().get(object));
									} catch (Exception e) {}
								}
								dataCellIndex++;
							}
						}
						dataIndex++;
					}
				}
			}
			//设置单元格自适应
			sheet.trackAllColumnsForAutoSizing();
			if(MapUtils.isNotEmpty(titleMap)) {
				int headerCellIndex = 0;
				Iterator<String> iterValue = titleMap.values().iterator();
				while (iterValue.hasNext()) {
					iterValue.next();
					try {
						sheet.autoSizeColumn(headerCellIndex, true);
					} catch (Exception e) {}
					headerCellIndex++;
				}
			} else if(MapUtils.isNotEmpty(fieldAndTypes)) {
				int headerCellIndex = 0;
				Iterator<String> iterValue = fieldAndTypes.values().iterator();
				while (iterValue.hasNext()) {
					iterValue.next();
					try {
						sheet.autoSizeColumn(headerCellIndex, true);
					} catch (Exception e) {}
					headerCellIndex++;
				}
			}
			workbook.write(out);
			workbook.close();
		}
	}
	
	private static Map<Field, String> recursive(Class<?> _class, Map<String, String> needTitleMap){
        if(_class == null) {
        	return null;
        } else {
            Map<Field, String> methodAndTypes = new HashMap<Field, String>();
            Map<String, String> needSuperTitleMap = null;
            if(MapUtils.isNotEmpty(needTitleMap)) {
            	needSuperTitleMap = new HashMap<String, String>();
            	Iterator<Entry<String, String>> iterKeyValue = needTitleMap.entrySet().iterator();
            	Field field = null;
            	while (iterKeyValue.hasNext()) {
            		Entry<String, String> keyValue = iterKeyValue.next();
					try {
						field = _class.getDeclaredField(keyValue.getKey());
						if(!Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers())) {
							field.setAccessible(true);
							methodAndTypes.put(field, field.getGenericType().toString());
						} else {
							needSuperTitleMap.put(keyValue.getKey(), keyValue.getValue());
						}
					} catch(Exception e) {
						needSuperTitleMap.put(keyValue.getKey(), keyValue.getValue());
//						iterKeyValue.remove();
//						e.printStackTrace(System.out);
					}
				}
            } else {
	            // 遍历所有属性
            	Field[] fields = _class.getDeclaredFields();
            	Field field = null;
	            for(int i = 0, total = fields.length; i < total; i++) {
	                try {
	                	field = fields[i];
	                	if(!Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers())) {
	                		field.setAccessible(true);
	                		methodAndTypes.put(field, field.getGenericType().toString());
	                	}
					} catch(SecurityException e) {}
	            }
            }
            Map<Field, String> superFieldAndTypes = recursive(_class.getSuperclass(), needSuperTitleMap);
            if(superFieldAndTypes != null) {
            	methodAndTypes.putAll(superFieldAndTypes);
            }
            return methodAndTypes;
         }
	}
	
	@SuppressWarnings("rawtypes")
	public static Map<String, String> getHeaderMapFromJson(String json) {
		Map<String, String> headerMap = null;
		if(StringUtils.isNotBlank(json)) {
			JSONObject jsonObject = JSONObject.parseObject(json);
			if(!jsonObject.isEmpty()) {
				headerMap = new LinkedHashMap<String, String>();
				Set entry = jsonObject.entrySet();
				Iterator iterator = entry.iterator();
				while (iterator.hasNext()) {
					try {
						@SuppressWarnings("unchecked")
						Map.Entry<String, String> object = (Map.Entry<String, String>)iterator.next();
						headerMap.put(object.getKey(),object.getValue());
					} catch (Exception e) {}
				}
			}
		}
		return headerMap;
	}
	
	public static void main(String[] args) throws Exception {
		List<?> importVos = ExcelUtils.parseEntity(UserInfo.class, new FileInputStream(new File("E:\\yjh\\temp\\info.xlsx")));
		for (Object object : importVos) {
			System.out.println(((UserInfo)object).getUsername());
		}
	}
}
