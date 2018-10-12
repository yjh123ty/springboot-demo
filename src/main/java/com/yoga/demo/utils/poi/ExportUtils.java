package com.yoga.demo.utils.poi;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

/**
 * 导出工具类
 * 
 * @author yoga
 */
@Component
public class ExportUtils {

	public void exportExcel(List<T> dataList, String fileName, String title, String sheetName, List<ExcelExportEntity> dynamicColumn, HttpServletResponse response) throws Exception{
		OutputStream os = response.getOutputStream();
		ExportParams params   = new ExportParams(title,sheetName);
		Workbook workbook = ExcelExportUtil.exportExcel(params,dynamicColumn,dataList);
		workbook.write(os);
		os.flush();
		os.close();
	}
	
	/**
	 * 导出
	 * @param tmpFile
	 * @param contentMap
	 * @param exportFile
	 * @throws Exception
	 */
	public static void exportWord(File tmpFile, Map<String, String> contentMap, String exportFile) throws Exception {
	    FileInputStream tempFileInputStream = new FileInputStream(tmpFile);
	    HWPFDocument document = new HWPFDocument(tempFileInputStream);
	    // 读取文本内容
	    Range bodyRange = document.getRange();
	    // 替换内容
	    for (Map.Entry<String, String> entry : contentMap.entrySet()) {
	        bodyRange.replaceText("${" + entry.getKey() + "}", entry.getValue());
	    }

	    //导出到文件
	    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	    document.write(byteArrayOutputStream);
	    OutputStream outputStream = new FileOutputStream(exportFile);
	    outputStream.write(byteArrayOutputStream.toByteArray());
	    outputStream.close();
	}
	
	public static void main(String[] args) throws Exception {
		File tmpFile = ResourceUtils.getFile("classpath:templates/template.doc");
		
		Map<String, String> contentMap = new HashMap<String, String>();
		contentMap.put("title", "这是一个标题");
		contentMap.put("content", "这是内容，XXXXX");
		contentMap.put("author", "余子酱");
		contentMap.put("url", "http://suibianxiede.com");
		
		exportWord(tmpFile, contentMap, "E:/yjh/temp/template.doc");
		System.err.println("导出完成");
	}
}
