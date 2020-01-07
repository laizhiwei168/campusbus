package com.zhiwei.campusbus.service;

import com.zhiwei.campusbus.po.UserPo;



public interface IUserService {
	//根据UserId查询
	public UserPo selectByUserId(Integer userId);
	public UserPo selectByUsername(String  username);
	//插入一条数据
	public int add(UserPo po);
	
}
