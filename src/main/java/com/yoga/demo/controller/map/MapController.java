package com.yoga.demo.controller.map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yoga.demo.common.JsonMsgBean;
import com.yoga.demo.domain.address.vo.AddressInfo;
import com.yoga.demo.utils.Tools;
import com.yoga.demo.utils.baidu.BaiduUtils;
import com.yoga.demo.utils.result.JsonMsgBeanUtils;

@Controller
@RequestMapping("/map")
public class MapController {
   
	@RequestMapping("/index")
    public String map() {
        return "pages/baidu/map";
    }
    
    @RequestMapping("/distance")
    public String mapDistance() {
        return "pages/baidu/distance";
    }
    
    @RequestMapping(value = "getLocationByIp",method = RequestMethod.GET)
	@ResponseBody
	public JsonMsgBean getLocationByIp(HttpServletRequest request){
    	//获取客户端登录的IP
    	String ipAddr = Tools.getIpAddr(request);
    	AddressInfo addressInfo = BaiduUtils.getLocationByIp(ipAddr);
    	return JsonMsgBeanUtils.defaultSeccess(addressInfo);
	} 

}
