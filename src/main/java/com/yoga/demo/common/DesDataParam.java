package com.yoga.demo.common;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

/** 
* 说明：脱敏参数
* @author yoga
*/
public class DesDataParam implements Serializable {
    private static final long serialVersionUID = -2633435449040501673L;
    /** 字符串开始脱敏位置下标 */
    private Integer startIndex;
    /** 字符串结束脱敏位置下标 */
    private Integer endIndex;
    /** 字符串脱敏长度 */
    private Integer length;
    /** 字符串脱敏后，保留后几位字符 */
    private Integer endLength;
    /** 脱敏替换的字符串 */
    private String replaceStr;
    /** 脱敏替换的字符 */
    private char replaceChar = '*';
    public Integer getStartIndex() {
        return startIndex;
    }
    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }
    public Integer getEndIndex() {
        return endIndex;
    }
    public void setEndIndex(Integer endIndex) {
        this.endIndex = endIndex;
    }
    public Integer getLength() {
        return length;
    }
    public void setLength(Integer length) {
        this.length = length;
    }
    public Integer getEndLength() {
        return endLength;
    }
    public void setEndLength(Integer endLength) {
        this.endLength = endLength;
    }
    public String getReplaceStr() {
        return replaceStr;
    }
    public void setReplaceStr(String replaceStr) {
        this.replaceStr = replaceStr;
    }
    public char getReplaceChar() {
        return replaceChar;
    }
    public void setReplaceChar(char replaceChar) {
        this.replaceChar = replaceChar;
    }
    
    public static String desStr(String oldStr, DesDataParam param) {
        if(StringUtils.isNotEmpty(oldStr)) {
            if(param.getStartIndex() == null && param.getEndIndex() == null && param.getEndLength() == null) {
                param.setStartIndex(0);
            }
            
            if(param.getStartIndex() != null) {
                if(param.getLength() != null) {
                    param.setEndIndex(param.getStartIndex() + param.getLength());
                } else if(param.getEndLength() != null) {
                    param.setEndIndex(oldStr.length() - param.getEndLength());
                } else if(param.getEndIndex() == null) {
                    param.setEndIndex(oldStr.length());
                }
            } else if(param.getEndIndex() != null) {
                if(param.getLength() != null) {
                    param.setStartIndex(param.getEndIndex() - param.getLength());
                }
            } else if(param.getEndLength() != null) {
                param.setEndIndex(oldStr.length() - param.getEndLength());
                if(param.getLength() != null) {
                    param.setStartIndex(param.getEndIndex() - param.getLength());
                } else {
                    param.setStartIndex(0);
                }
            }
            
            if(param.getStartIndex() != null && param.getEndIndex() != null) {
                if(param.getEndIndex() < 0) {
                    param.setEndIndex(0);
                }
                if(param.getStartIndex() > param.getEndIndex()) {
                    param.setStartIndex(0);
                }
                if(param.getReplaceStr() != null) {
                    return StringUtils.substring(oldStr, 0, param.getStartIndex()).concat(param.getReplaceStr()).concat(StringUtils.substring(oldStr, param.getEndIndex()));
                } else {
                    return StringUtils.substring(oldStr, 0, param.getStartIndex()).concat(StringUtils.repeat(param.getReplaceChar(), param.getEndIndex() - param.getStartIndex())).concat(StringUtils.substring(oldStr, param.getEndIndex()));
                }
            }
        }
        return oldStr;
    }
}
