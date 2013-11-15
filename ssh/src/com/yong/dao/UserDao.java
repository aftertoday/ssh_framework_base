package com.yong.dao;

import org.springframework.stereotype.Repository;

import com.yong.base.BaseDao;
import com.yong.entity.test.User;

@SuppressWarnings("all")
@Repository
public class UserDao extends BaseDao<User>{
	
	public void addUser(User u){
		this.save(u);
		//throw new RuntimeException("error");
	}
}
