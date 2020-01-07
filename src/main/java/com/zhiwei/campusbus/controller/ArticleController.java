package com.zhiwei.campusbus.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.zhiwei.campusbus.utils.FtpDemo;
import com.zhiwei.campusbus.vo.FileImgVo;


@Controller
@RequestMapping(value="/Article")
public class ArticleController {

	
	@RequestMapping(value="file",method = RequestMethod.POST)
	public @ResponseBody String  file(HttpServletRequest request,FileImgVo vo){
		System.out.println("**********************");
		System.out.println(vo.getId()+"   "+vo.getName()+"   ");
		System.out.println((CommonsMultipartFile) vo.getImg());
		
		FtpDemo.uploadtofile((CommonsMultipartFile)vo.getImg());
        return  "成功";
		
	}
}
