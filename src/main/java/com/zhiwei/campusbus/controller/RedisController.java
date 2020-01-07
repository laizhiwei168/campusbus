package com.zhiwei.campusbus.controller;


import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhiwei.campusbus.service.RedisService;


@Controller
@RequestMapping(value="/Redis")
public class RedisController {


	@Resource(name="redisService")
	private RedisService redisService;
	
	@RequestMapping(value="set",method = RequestMethod.POST)
	public @ResponseBody String  set(String key,String value){
		System.out.println(key+"  ----  "+value);
		//redisService.set(key, value);
		System.out.println(redisService.get(key));
		return "ok";
	}
}
