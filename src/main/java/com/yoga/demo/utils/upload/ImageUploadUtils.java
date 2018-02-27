package com.yoga.demo.utils.upload;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import net.coobird.thumbnailator.Thumbnails;

public class ImageUploadUtils {
	private static Logger log = LoggerFactory.getLogger(ImageUploadUtils.class);
	
	/** 输出图片的质量 降低图片的大小 */
    private static final double DEFALUT_IMG_QUALITY = 1d;
    
	/** 
     * 不能超过的最大压缩宽 
     */  
    private static final int COMPRESS_MAX_WIDTH = 1920;  
  
    /** 
     * 不能超过的最大压缩长 
     */  
    private static final int COMPRESS_MAX_HEIGHT = 1080;  
	
	/** 
     * 默认图片压缩后的实际存放目录  
     */  
    private static final String DEFAULT_SAVE_PATH = "E:/yjh/temp/imgs/";  
	
	/** 
     * 图片最大限制 单位MB 
     */  
    private static final int UPLOAD_IMAGE_MAX_SIZE_MB = 5;  
    
    /** 
     * 图片最大限制 单位B 
     */  
    private static final long UPLOAD_IMAGE_MAX_SIZE_B = UPLOAD_IMAGE_MAX_SIZE_MB * 1024 * 1024L; 
	
	/** 
     * 允许的图片格式 
     */  
    private static final String UPLOAD_IMAGE_EXTS = "jpg/jpeg/png/gif";  
    
    /** 默认的后缀 */
    public static final String DEFAULT_EXT = "jpg";
  
    /** 
     * 图片文件过大 提示 
     */  
    private static final String UPLOAD_IMAGR_SIZE_LIMIT = "图片文件过大，不能超过" + UPLOAD_IMAGE_MAX_SIZE_MB  
                                                          + "M";  
  
    /**  
     * 图片上传后缀 提示 
     */  
    private static final String UPLOAD_IMAGE_EXT_LIMIT = "图片格式只能为" + UPLOAD_IMAGE_EXTS;
	
    public static String compressImage(MultipartFile multiFile, int width, int height, boolean isScale, String dirPath)  
    {  
        InputStream in = null;
        try {
            //判断图片格式  
            String ext = FilenameUtils.getExtension(multiFile.getOriginalFilename());  
            //将文件后缀转成小写  
            if (StringUtils.isNotBlank(ext))  
            {  
                ext = ext.toLowerCase();  
            }  
            if (!UPLOAD_IMAGE_EXTS.contains(ext))  
            {  
                log.warn(" file ext:" + ext + " not allowed in:" + UPLOAD_IMAGE_EXTS);  
                throw new RuntimeException(UPLOAD_IMAGE_EXT_LIMIT);  
            }  
            //判断图片大小  
            long size = multiFile.getSize();  
            if (size > UPLOAD_IMAGE_MAX_SIZE_B)  
            {  
                log.warn(" file size:" + size + " outof sizeMaxLimit:" + UPLOAD_IMAGE_MAX_SIZE_B);  
                throw new RuntimeException(UPLOAD_IMAGR_SIZE_LIMIT);  
            } 
            in = multiFile.getInputStream();
            String store = compress(in, ext, width, height, isScale,  dirPath); 
            log.debug(" file store in:" + store);  
            return store;
        } catch (IOException e) {
            log.error("文件上传错误", e);
            return null;
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
    
    
	/**
     * 压缩文件
     * @return
     */
    private static String compress(InputStream in, String ext, int width, int height, boolean isScale,  String dirPath) {
        String path = null;
        try {
            width = width > COMPRESS_MAX_WIDTH ? COMPRESS_MAX_WIDTH : width;
            height = height > COMPRESS_MAX_HEIGHT ? COMPRESS_MAX_HEIGHT : height;
            path = DEFAULT_SAVE_PATH + dirPath + "." + ext;
            File file = new File(path);
            File dir = file.getParentFile();
            if(!dir.exists()) {
                dir.mkdirs();
            }
            if(isScale) {
                BufferedImage image = ImageIO.read(in);
                double w = image.getWidth();
                double h = image.getHeight();
                if(w > width || h > height){
                    double rateW = width / w  ;
                    double rateH = height / h;
                    double rate = rateW < rateH ? rateW : rateH;
                    int new_w = (int) (w * rate);
                    int new_h = (int) (h * rate);
                    Thumbnails.of(image)
                        .size(new_w, new_h)
                        .keepAspectRatio(true)
                        .outputQuality(DEFALUT_IMG_QUALITY)
                        .outputFormat(ext)
                        .toFile(path);
                } else {
                    Thumbnails
                        .of(image)
                        .size((int)w, (int)h)
                        .outputQuality(DEFALUT_IMG_QUALITY)
                        .outputFormat(ext)
                        .toFile(path);
                }
            } else {
                    Thumbnails
                        .of(in)
                        .size(width, height)
                        .keepAspectRatio(true)
                        .outputQuality(DEFALUT_IMG_QUALITY)
                        .outputFormat(ext)
                        .toFile(path);
            }
        } catch (IOException e) {
        	e.printStackTrace();
        }
        return path.substring(DEFAULT_SAVE_PATH.length());
    }
    
    
    /**
     * 上传文件使用默认的设置.
     * @param img
     */
    public static String uploadImgDefault(MultipartFile img, String dirPath) {
        return compressImage(img, COMPRESS_MAX_WIDTH, COMPRESS_MAX_HEIGHT, true, dirPath);
    }
    
}
