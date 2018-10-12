/*
 * OnFintech
 * Copyright (c) 2018.客如云 All Rights Reserved.
 */
package com.yoga.demo.controller.export;


import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.google.common.collect.Lists;
import com.yoga.demo.common.Page;
import com.yoga.demo.domain.dto.UserInfoSearchDTO;
import com.yoga.demo.domain.shiro.UserInfo;
import com.yoga.demo.service.UserInfoService;
import com.yoga.demo.utils.poi.ExportUtils;
import io.swagger.models.auth.In;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 导出
 *
 * @auther yujiahao
 * @date 2018/10/12/16:56
 */
@RestController
@RequestMapping("export")
@Slf4j
public class ExportController {

  @Autowired
  private UserInfoService userInfoService;

  @Autowired
  private ExportUtils exportUtils;

  @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
  public void getAuditOrderStatus(HttpServletResponse response) {
    String fileName = "用户信息.xls";
    response.setContentType("application/ms-excel");
    response.setHeader("Content-Disposition", "attachment;Filename=" + fileName);
    try {
      UserInfo user = userInfoService.findByUsername("yjh");
      List data = Lists.newArrayList(user);
      //将list转为map
      List newList = new ArrayList();
      data.forEach(vo->{
        Map mapvo = org.springframework.cglib.beans.BeanMap.create(vo);
        newList.add(mapvo);
      });
      exportUtils.exportExcel(newList, fileName, "用户信息", "用户信息", dynaAssembleClo(), response);
    } catch (Exception e) {
      e.printStackTrace();
      log.error("导出异常， {}", e.getMessage());
    }
  }

  private List<ExcelExportEntity> dynaAssembleClo() {
    List<ExcelExportEntity> colList = new ArrayList<ExcelExportEntity>();
    ExcelExportEntity colEntity = new ExcelExportEntity("用户uid", "uid");
    colEntity.setNeedMerge(true);
    colList.add(colEntity);

    colEntity = new ExcelExportEntity("姓名", "username");
    colEntity.setNeedMerge(true);
    colEntity.setWidth(10);
    colList.add(colEntity);

    colEntity = new ExcelExportEntity("密码", "password");
    colEntity.setNeedMerge(true);
    colEntity.setWidth(10);
    colList.add(colEntity);
    return colList;
  }

}
