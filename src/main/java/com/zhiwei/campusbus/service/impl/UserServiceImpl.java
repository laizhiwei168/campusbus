package com.zhiwei.campusbus.service.impl;



import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.zhiwei.campusbus.dao.IUserDao;
import com.zhiwei.campusbus.po.UserPo;
import com.zhiwei.campusbus.service.IUserService;


@Component("userService")
public class UserServiceImpl implements IUserService {

	private static Logger logger = LogManager.getLogger(UserServiceImpl.class.getName());
	
	@Resource(name="userDao")
	private IUserDao userDao;
	
	@Override
	public UserPo selectByUserId(Integer userId) {
		System.out.println("*******************************************************");
		// TODO Auto-generated method stub
		return userDao.selectByUserId(userId);
	}

	@Override
	public int add(UserPo po) {
		// TODO Auto-generated method stub
		return userDao.add(po);
	}

	@Override
	public UserPo selectByUsername(String username) {
		// TODO Auto-generated method stub
		return userDao.selectByUsername(username);
	}

	
}
