package com.yong.service;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yong.dao.SpringUserDao;
import com.yong.dao.TeamDao;
import com.yong.dao.UserDao;
import com.yong.entity.test.Team;
import com.yong.entity.test.User;
import com.yong.util.Pagination;

@SuppressWarnings("all")
@Service
@Transactional
public class UserService{

	@Autowired
	SpringUserDao springUserDao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	TeamDao teamDao;
	
	public void addUser(User u){
		userDao.addUser(u);
		Team t = new Team();
		t.setTeamDesc("hi-team-desc");
		t.setTeamName("hi-team-name");
		teamDao.addTeam(t);
		throw new RuntimeException("error");
	}
	
	public void daoTest(){
		Criteria c = userDao.getCriteria(new User());
		Criteria ec = userDao.getEmptyCriteria();
		List<User> userList = userDao.findAll();
		printList(userList);
		System.out.println(userDao.findAllCount());
		System.out.println("======  find by id =======");
		User usr = userDao.findById(12);
		printUser(usr);
		System.out.println("=========== find by conditions =======");
		User paramU = new User();
		paramU.setAge(9);
		List<User> conList = userDao.findByConditions(paramU);
		printList(conList);
		System.out.println("=========== find by page ==========");
		
		User pageU = new User();
		Pagination<User> page = userDao.findPagination(0, 3, pageU);
		printPage(page);
		
		System.out.println("========= find page by criteria ======");
		Criteria pagec = userDao.getCriteria(pageU);
		Pagination<User> cPage = userDao.findPagination(0, 3, pagec);
		printPage(cPage);
	}
	
	
	public void printList(List<User> list){
		for(User u : list){
			System.out.println(u.getId()+"   "+u.getName()+"   "+u.getAddress());
		}
	}
	
	public void printUser(User u){
		System.out.println(u.getId()+"   "+u.getName()+"   "+u.getAddress());
	}
	
	public void printPage(Pagination<User> page){
		System.out.println(page.getBeginPage()+"  "+page.getCustPageSize()+"    "+page.getFirstPage()+"   "+page.getTotalPage());
		printList(page.getResults());
	}
	
	public void saveUser(User u){
		userDao.save(u);
	}

	public SpringUserDao getSpringUserDao() {
		return springUserDao;
	}

	public void setSpringUserDao(SpringUserDao springUserDao) {
		this.springUserDao = springUserDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public TeamDao getTeamDao() {
		return teamDao;
	}

	public void setTeamDao(TeamDao teamDao) {
		this.teamDao = teamDao;
	}
	
} 
