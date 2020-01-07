package com.zhiwei.campusbus.controller;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhiwei.campusbus.po.UserPo;
import com.zhiwei.campusbus.service.IUserService;



@Controller
@RequestMapping(value="/yard/user")
public class UserController {
	
	private static Logger logger = LogManager.getLogger(UserController.class.getName());
	
	@Resource(name="userService")
	private IUserService userService;
	
	
	@ResponseBody
	@RequestMapping(value="load",method = RequestMethod.POST)
	public UserPo selUser(Integer id){
		System.out.println("*******************id:"+id);
		UserPo result = userService.selectByUserId(id);
		System.out.println(result.getUsername());
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value="addUser",method = RequestMethod.POST)
	public int addUser(String username,String password){
		System.out.println("*******************username:"+username+"   password:"+password);
		UserPo po=new UserPo();
		po.setUsername(username);
		po.setPassword(password);
		int result = userService.add(po);
		System.out.println(result);
		return result;
	}
	
	
	
	
	/*
	@ResponseBody
	@RequestMapping(value="load",method = RequestMethod.POST)
	public Msg loadUserData(@RequestBody Condition condition){
		Msg result = new Msg();
		if(condition==null){
			return result.failure();
		}
		List<UserPo> list = userService.selectAllUser(condition);
		
		return result.success().add("data", list);
		
	}
	*/
	
	
	/*@ResponseBody
	@RequestMapping(value="data",method = RequestMethod.POST)
	public Msg loadOneUser(Integer id){
		Msg result = new Msg();
		if(id==null){
			result.setCode("200").setMsg("加载失败");
		}
		try {
			UserPo po = userService.selectById(id);
			result.add("data", po);
			result.setCode("100").setMsg("加载成功");
		} catch (Exception e) {
			result.setCode("200").setMsg("加载失败");
		}
		return result;
	}*/
	
	/*@ResponseBody
	@RequestMapping(value="edit",method = RequestMethod.POST)
	public Msg editUser(@RequestBody UserPo po){
		Msg message = new Msg();
		try {
			message = userService.edit(po);
			message.setCode("100").setMsg("修改成功");
		} catch (Exception e) {
			message.setCode("200").setMsg("修改失败");
		}
		
		return message;
	}*/
	
	/*@ResponseBody
	@RequestMapping("load")
	public Msg loadUserData(@RequestParam(value="pn",defaultValue="1")Integer pn){
		
		Msg result = new Msg();
		try {
			//pn传入页码以及每页大小
			PageHelper.startPage(pn, 10);
			List<UserPo> list = userService.selectAllUser();
			//System.out.println(JSONArray.toJSON(list));
			PageInfo page = new PageInfo(list,5);
			result.add("pageInfo", page).setCode("100").setMsg("成功");
		} catch (Exception e) {
			result.setCode("200").setMsg("失败");
		}
		return result;
	}*/
	
	
}
