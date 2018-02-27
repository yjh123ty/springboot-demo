package com.yoga.demo.utils.poi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.springframework.util.ResourceUtils;

/**
 * 导出工具类
 * 
 * @author yoga
 */
public class ExportUtils {
	
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
