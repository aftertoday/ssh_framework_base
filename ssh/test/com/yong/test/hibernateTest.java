package com.yong.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yong.dao.UserDao;
import com.yong.entity.test.Team;
import com.yong.entity.test.User;
import com.yong.service.SpringUserService;
import com.yong.service.TeamService;
import com.yong.service.UserService;


@SuppressWarnings("all")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/applicationContext.xml")
public class hibernateTest {
	
	
	@Test
	public void userAdd(){
		User u = new User();
		u.setAddress("hi");
		u.setAge(1);
		u.setName("hi-name");
		//userDao.addUser(u);
		userService.addUser(u);
	}
	
	
	@Test
	public void teamTest(){
		Team t = new Team();
		t.setTeamDesc("hi-team-desc");
		t.setTeamName("hi-team-name");
		teamService.addTeam(t);
	}
	
	@Test
	public void baseDaoTest(){
		System.out.println("===============================================");
		userService.daoTest();
		System.out.println("===============================================");
	}
	
	
	@Test
	public void addUser(){
		for(int i=0;i<20;i++){
			User u = new User();
			u.setAddress("hi"+i);
			u.setAge(i);
			u.setName("hi-name"+i);
			userService.saveUser(u);
		}
	}
	
	public static UserDao userDao;
	public static UserService userService;
	public static SpringUserService springUserService;
	public static TeamService teamService;
	
	@Before
	public void before(){
		try{
			ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
			userService = (UserService)ctx.getBean("userService");
			userDao = (UserDao)ctx.getBean("userDao");
			teamService = (TeamService)ctx.getBean("teamService");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
