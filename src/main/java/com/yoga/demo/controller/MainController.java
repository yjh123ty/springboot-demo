package com.yoga.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {
   
	@RequestMapping(value = "/",method = RequestMethod.GET)
    public String main(Model model){
        return "pages/main/index";
    }
	
	@RequestMapping(value ="/main/index",method = RequestMethod.GET)
    public String mainIndex(Model model){
        return "pages/main/main_info";
    }

}
