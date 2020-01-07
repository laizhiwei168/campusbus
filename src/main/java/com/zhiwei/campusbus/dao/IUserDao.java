package com.zhiwei.campusbus.dao;

import org.springframework.stereotype.Component;

import com.zhiwei.campusbus.po.UserPo;


@Component("userDao")
public interface IUserDao {
	
	//根据账号查询
	public UserPo selectByUserId(Integer userId);
	//根据用户名查询
	public UserPo selectByUsername(String  username);
	//插入一条数据
	public int add(UserPo po);
	
	//public ServicePropertyPo selectByserviceId(Integer propertyId);
}
