package com.yoga.demo.utils.poi;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/** 
* 说明：支持分页导出Excel数据
*   每一页，一个sheet
* @author 周海刚
* @version 创建时间：2017年4月1日 下午2:50:00
*/
public class PageExcelUtil {
    private SimpleDateFormat sdf = null;
    private Map<Field, String> fieldAndTypes = null;
    private List<String> specialFields = null;
    private SXSSFWorkbook workbook = null;
    private String title = null;
    private CellStyle titleStyle = null;
    private CellStyle valueStyle = null;
    private Map<String, String> titleMap = null;
    @SuppressWarnings("rawtypes")
    private Map<String, ExcelDataFormat> excelDataFormats = null;
    
    public static final int SUGGEST_MAX_PAGE_SIZE = 500000;
    public static final int SUGGEST_MAX_PAGE = 255;
    
    public static final int SUGGEST_EACH_PAGE_SIZE = 100000;
    
    /**
     * 构造方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
     *
     * @param title
     *            表格标题名
     * @param titleMap
     *            表头信息（对象属性名称->要显示的标题值)[按顺序添加]
     * @param objClass
     *            符合javabean风格的类的对象。此方法支持的javabean属性的数据类型
     *            有基本数据类型及String,Date,byte[](图片数据)
     * @param pattern
     *            如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
     */
    public PageExcelUtil(String title, Map<String, String> titleMap, Class<?> objClass, String pattern) {
        this(title, titleMap, objClass, pattern, null);
    }
    
    /**
     * 构造方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
     *
     * @param title
     *            表格标题名
     * @param titleMap
     *            表头信息（对象属性名称->要显示的标题值)[按顺序添加]
     * @param objClass
     *            符合javabean风格的类的对象。此方法支持的javabean属性的数据类型
     *            有基本数据类型及String,Date,byte[](图片数据)
     * @param pattern
     *            如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
     * @param excelDataFormats
     *            属性值格式化（属性名->格式化方法）。可为null
     */
    @SuppressWarnings("rawtypes")
    public PageExcelUtil(String title, Map<String, String> titleMap, Class<?> objClass, String pattern, Map<String, ExcelDataFormat> excelDataFormats) {
        if(StringUtils.isBlank(pattern)) {
            pattern = "yyy-MM-dd";
        }
        sdf = new SimpleDateFormat(pattern);
        workbook = new SXSSFWorkbook();
        this.title = title;
        this.titleMap = titleMap;
        this.excelDataFormats = excelDataFormats;
        
        fieldAndTypes = recursive(objClass, titleMap);
        addSpecialField(fieldAndTypes, titleMap);
        
        titleStyle = workbook.createCellStyle();// 样式对象
        titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中
        //titleStyle.setWrapText(false);
        
        valueStyle = workbook.createCellStyle();// 样式对象
        valueStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
        //valueStyle.setWrapText(true);
    }
    
    private void addSpecialField(Map<Field, String> fieldAndTypes, Map<String, String> titleMap) {
        if(MapUtils.isNotEmpty(titleMap)) {
            List<String> hasFields = new ArrayList<String>();
            if(MapUtils.isNotEmpty(fieldAndTypes)) {
                Iterator<Entry<Field, String>> iterator = fieldAndTypes.entrySet().iterator();
                while (iterator.hasNext()) {
                    hasFields.add(iterator.next().getKey().getName());
                }
            }
            Iterator<Entry<String, String>> iterator = titleMap.entrySet().iterator();
            specialFields = new ArrayList<String>();
            while (iterator.hasNext()) {
                String title = iterator.next().getKey();
                if(!hasFields.contains(title)) {
                    specialFields.add(title);
                }
            }
        }
    }
    
    /**
     * 分页添加excel数据，每一页对应一个Sheet
     * @param page
     * @param dataset 数据
     */
    public void appendExcel(int page, Collection<?> dataset) {
        SXSSFSheet sheet;
        if(StringUtils.isBlank(title)) {
            sheet = workbook.createSheet("数据表格-"+page+"页");
        } else {
            sheet = workbook.createSheet(title+"-"+page+"页");
            Row row1 = sheet.createRow(0);   //--->创建一行
            if(MapUtils.isNotEmpty(titleMap)) {
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, titleMap.size() - 1));
            }
            
            row1.setHeightInPoints(25);
//          HSSFCell cell1 = row1.createCell(0);
            Cell cell1 = row1.createCell(0);
            cell1.setCellStyle(titleStyle);
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
                headerCell.setCellStyle(valueStyle);
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
                            if(CollectionUtils.isNotEmpty(specialFields)) {
                                if(specialFields.contains(keyName.getKey())) {
                                    fillCellValue(dataCell, keyName.getKey(), object);
                                    dataCellIndex++;
                                    continue;
                                }
                            }
                            for(Entry<Field, String> fieldType : fieldAndTypes.entrySet()) {
                                if(fieldType.getKey().getName().equals(keyName.getKey())) {
                                    fillCellValue(dataCell, fieldType, object);
                                    continue;
                                }
                            }
                            dataCellIndex++;
                        }
                    } else {
                        for(Entry<Field, String> fieldType : fieldAndTypes.entrySet()) {
                            dataCell = dataRow.createCell(dataCellIndex);
                            fillCellValue(dataCell, fieldType, object);
                            dataCellIndex++;
                        }
                    }
                    dataIndex++;
                }
            }
        }
//        autoSizeColumn(sheet);
    }
    
    /**
     * 打印表格到输出流
     * @param out
     * @throws IOException
     */
    public void printStream(OutputStream out) throws IOException {
        if(out != null) {
            workbook.write(out);
            workbook.close();
        }
    }
    
    private void autoSizeColumn(SXSSFSheet sheet) {
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
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void fillCellValue(Cell dataCell, Entry<Field, String> fieldType, Object object) {
        ExcelDataFormat excelDataFormat = null;
        if(MapUtils.isNotEmpty(excelDataFormats) && excelDataFormats.containsKey(fieldType.getKey().getName())) {
            excelDataFormat = excelDataFormats.get(fieldType.getKey().getName());
        }
        dataCell.setCellStyle(valueStyle);
        dataCell.setCellType(Cell.CELL_TYPE_STRING);
        if(excelDataFormat == null) {
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
        } else {
            try {
                dataCell.setCellValue(excelDataFormat.getFieldFormatData(object));
            } catch (Exception e) {}
        }
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void fillCellValue(Cell dataCell, String specialField, Object object) {
        ExcelDataFormat excelDataFormat = null;
        if(MapUtils.isNotEmpty(excelDataFormats) && excelDataFormats.containsKey(specialField)) {
            excelDataFormat = excelDataFormats.get(specialField);
        }
        dataCell.setCellStyle(valueStyle);
        dataCell.setCellType(Cell.CELL_TYPE_STRING);
        if(excelDataFormat != null) {
            try {
                dataCell.setCellValue(excelDataFormat.getFieldFormatData(object));
            } catch (Exception e) {
//                e.printStackTrace(System.out);
            }
        }
    }
    
    public interface ExcelDataFormat<T> {
        /** 
         * 格式化属性值
         * @param 原对象
         * @return 格式化后的字符串
         */
        String getFieldFormatData(T inputObject);
    }
    
    private Map<Field, String> recursive(Class<?> _class, Map<String, String> needTitleMap){
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
//                      iterKeyValue.remove();
//                      e.printStackTrace(System.out);
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
}
