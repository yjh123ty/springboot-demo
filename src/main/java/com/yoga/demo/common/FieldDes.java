package com.yoga.demo.common;

import java.io.Serializable;

/** 
* 说明：脱敏处理需要使用的对象
* @author yoga
*/
public class FieldDes implements Serializable {
    private static final long serialVersionUID = -6048950660160910165L;
    private String field;
    private DesDataParam desParam;
    public String getField() {
        return field;
    }
    public void setField(String field) {
        this.field = field;
    }
    public DesDataParam getDesParam() {
        return desParam;
    }
    public void setDesParam(DesDataParam desParam) {
        this.desParam = desParam;
    }
}
