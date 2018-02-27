package com.yoga.demo.service.impl;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.yoga.demo.common.DesDataParam;
import com.yoga.demo.common.FieldDes;
import com.yoga.demo.common.Page;
import com.yoga.demo.service.AuthorityCheckService;

@Service
public class AuthorityCheckServiceImpl implements AuthorityCheckService{
	
	// 配置角色-URL下的UI元素黑名单，在黑名单内的不显示
	private static Map<Integer, Map<String, String>> permissions = new HashMap<Integer, Map<String,String>>();
	// 配置角色-方法下的字段黑名单，在黑名单内的置为null
	private static Map<Integer, Map<String, String>> permissionsFieldVisible = new HashMap<Integer, Map<String,String>>();
	// 配置角色-方法下的字段黑名单，在黑名单内的值按对应脱敏规则脱敏
	private static Map<Integer, Map<String, String>> permissionsFieldDes = new HashMap<Integer, Map<String, String>>();
	
	// 配置角色-URL下的UI元素白名单，在白名单内的正常显示，优先级比黑名单高，慎用
	private static Map<Integer, Map<String, String>> whitePermissions = new HashMap<Integer, Map<String,String>>();
	// 配置角色-方法下的字段白名单，在白名单内的正常显示，优先级比黑名单高，慎用
    private static Map<Integer, Map<String, String>> whitePermissionsFieldVisible = new HashMap<Integer, Map<String,String>>();
    // 配置角色-方法下的字段白名单，在白名单内的正常显示，优先级比黑名单高，慎用
    private static Map<Integer, Map<String, String>> whitePermissionsFieldDes = new HashMap<Integer, Map<String,String>>();
	
	static{
		Map<String, String> urlIdsPermissions = new HashMap<String, String>();
		// 多个ID用逗号分割
		urlIdsPermissions.put("page.user.command", "command-edit-btn,command-delete-btn,command-save-btn,");
		
        // ajax不显示字段
        Map<String, String> methodFieldPermissions = new HashMap<String, String>();
//        methodFieldPermissions.put("com.allinpay.gb.loan.controller.pc.LoanApplicationFastloanController.listLoan", 
//                "ftManageFeeRate,ftLoanRate,ftDailyRate,ftDecisionCode,reviewManageFeeRate,reviewRate");
//        methodFieldPermissions.put("com.allinpay.gb.loan.controller.pc.LoanApplicationDailyloanController.listWithdraw", 
//        		"applyFailedReason");
        
        // ajax脱敏字段
        Map<String, String> methodFieldDesPermissions = new HashMap<String, String>();
        //屏蔽身份证信息，从倒数第四位开始，屏蔽8位
        methodFieldDesPermissions.put("com.yoga.demo.controller.user.UserController.listUsers", 
        		"[{\"field\":\"idNo\", \"desParam\":{\"length\":8,\"endLength\":4}}]");
        
        //TODO 修改角色id（游客 id=3）
        int roleId = 3;
		permissions.put(roleId, urlIdsPermissions);
		permissionsFieldVisible.put(roleId, methodFieldPermissions);
		permissionsFieldDes.put(roleId, methodFieldDesPermissions);
		
	}

	@Override
	public boolean checkUIDataPermission(List<Integer> roleIds, String key, String id) {
		for (Integer roleId : roleIds) {
	        // 先判断白名单
	        Map<String, String> wurlIdsPermissions = whitePermissions.get(roleId);
	        if(MapUtils.isNotEmpty(wurlIdsPermissions)) {
	            String whiteIds = wurlIdsPermissions.get(key);
	            if(StringUtils.isNotEmpty(whiteIds)) {
	                List<String> whiteIdsList = Arrays.asList(whiteIds.split(","));
	                if(whiteIdsList.contains(id))
	                    return true;
	            }
	        }
        }
	    for (Integer roleId : roleIds) {
	        // 后黑名单
            Map<String, String> urlIdsPermissions = permissions.get(roleId);
            if(MapUtils.isNotEmpty(urlIdsPermissions)) {
                String blackIds = urlIdsPermissions.get(key);
                if (StringUtils.isNotEmpty(blackIds)) {
                    List<String> blackIdsList = Arrays.asList(blackIds.split(","));
                    if(blackIdsList.contains(id))
                        return false;
                }
            }
        }
		return true;
	}

	@Override
	public void filterDataByPermission(String fullName, List<Integer> roleIds, Object data) {
		if(data instanceof List){
			List<?> dataList = (List<?>) data;
			if(CollectionUtils.isEmpty(dataList))return;
			for (Object object : dataList) {
				filterObjectFields(fullName, roleIds, object);
			}
		}else if(data instanceof Page){
		    Page<?> pageList = (Page<?>) data;
            if(pageList == null || CollectionUtils.isEmpty(pageList.getRows()))return;
            for (Object object : pageList.getRows()) {
                filterObjectFields(fullName, roleIds, object);
            }
        }else{
			filterObjectFields(fullName, roleIds, data);
		}
		
	}
	
	private void filterObjectFields(String fullName, List<Integer> roleIds, Object object){
		List<String> blackFields = listAjaxBlackFields(fullName, roleIds);
		if(CollectionUtils.isNotEmpty(blackFields)){
			for (String fdName : blackFields) {
				try {
					Field field = getDeclaredField(object, fdName);
					if(field != null) {
    					field.setAccessible(true);
    					// 将无权限查看的字段设置为NULL
    					field.set(object, null);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void desDataByPermission(String fullName, List<Integer> roleIds, Object data) {
		if(data instanceof List){
            List<?> dataList = (List<?>) data;
            if(CollectionUtils.isEmpty(dataList))return;
            for (Object object : dataList) {
                desObjectFields(fullName, roleIds, object);
            }
        }else if(data instanceof Page){
            Page<?> pageList = (Page<?>) data;
            if(pageList == null || CollectionUtils.isEmpty(pageList.getRows()))return;
            for (Object object : pageList.getRows()) {
                desObjectFields(fullName, roleIds, object);
            }
        }else{
            desObjectFields(fullName, roleIds, data);
        }
	}
	
	private void desObjectFields(String fullName, List<Integer> roleIds, Object object){
        List<FieldDes> blackFields = listAjaxBlackDesFields(fullName, roleIds);
        if(CollectionUtils.isNotEmpty(blackFields)){
            for (FieldDes fieldDes : blackFields) {
                try {
                    Field field = getDeclaredField(object, fieldDes.getField());
                    if(field != null && field.getType() == String.class) {
                        field.setAccessible(true);
                        // 将无权限查看的字符串字段按对应规则脱敏
                        System.out.println("before: "+field.get(object));
                        field.set(object, DesDataParam.desStr(field.get(object).toString(), fieldDes.getDesParam()));
                        System.out.println("after: "+field.get(object));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
	
	
	/** 
     * 循环向上转型, 获取对象的 DeclaredField 
     * @param object : 子类对象 
     * @param fieldName : 父类中的属性名 
     * @return 父类中的属性对象 
     */  
    private Field getDeclaredField(Object object, String fieldName) {  
        Field field = null;
        Class<?> clazz = object.getClass();
        for(; clazz != Object.class ; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                return field;
            } catch (Exception e) {
                //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去.
                //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了
                
            }
        }
        return null;
    }
    
 // 获取黑名单，若存在白名单中，则黑名单中该项不返回
 	private List<String> listAjaxBlackFields(String fullName, List<Integer> roleIds){
         List<String> blackFields = null;
         for (Integer roleId : roleIds) {
             Map<String, String> blackPerms = permissionsFieldVisible.get(roleId);
             if(MapUtils.isNotEmpty(blackPerms)) {
                 String blackListString = blackPerms.get(fullName);
                 // 多个角色时，只有全部都配了的字段才处理成空（null）
                 if(StringUtils.isEmpty(blackListString))
                     break;
                 
                 if(null == blackFields){
                     blackFields = Arrays.asList(blackListString.split(","));
                 }else{
                     blackFields.retainAll(Arrays.asList(blackListString.split(",")));
                 }
             }
         }
         //白名单
         if(CollectionUtils.isNotEmpty(blackFields)) {
             for (Integer roleId : roleIds) {
                 Map<String, String> whitePerms = whitePermissionsFieldVisible.get(roleId);
                 if(MapUtils.isNotEmpty(whitePerms)) {
                     String whiteListString = whitePerms.get(fullName);
                     if(StringUtils.isEmpty(whiteListString))
                         break;
                     blackFields.removeAll(Arrays.asList(whiteListString.split(",")));
                 }
             }
         }
         return blackFields;
     }
 	
 	// 获取黑名单，若存在白名单中，则黑名单中该项不返回
 	private List<FieldDes> listAjaxBlackDesFields(String fullName, List<Integer> roleIds){
         List<FieldDes> blackFields = null;
         ObjectMapper mapper = new ObjectMapper();
         for (Integer roleId : roleIds) {
             Map<String, String> blackPerms = permissionsFieldDes.get(roleId);
             if(MapUtils.isNotEmpty(blackPerms)) {
                 String blackListString = blackPerms.get(fullName);
                 // 多个角色时，只有全部都配了的字段才脱敏
                 if(StringUtils.isEmpty(blackListString))
                     break;
                 try {
                     List<FieldDes> list = mapper.readValue(blackListString, new TypeReference<List<FieldDes>>(){});
                     if(blackFields == null) {
                         blackFields = list;
                     } else {
                         blackFields.retainAll(list);
                     }
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
             }
         }
         //白名单
         if(CollectionUtils.isNotEmpty(blackFields)) {
             for (Integer roleId : roleIds) {
                 Map<String, String> whitePerms = whitePermissionsFieldDes.get(roleId);
                 if(MapUtils.isNotEmpty(whitePerms)) {
                     String whiteListString = whitePerms.get(fullName);
                     if(StringUtils.isEmpty(whiteListString))
                         break;
                     blackFields.removeAll(Arrays.asList(whiteListString.split(",")));
                 }
             }
         }
         return blackFields;
     }

}
