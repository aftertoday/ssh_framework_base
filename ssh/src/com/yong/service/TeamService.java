package com.yong.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yong.dao.SpringUserDao;
import com.yong.dao.TeamDao;
import com.yong.dao.UserDao;
import com.yong.entity.test.Team;
import com.yong.entity.test.User;

@SuppressWarnings("all")
@Service
@Transactional
public class TeamService{

	@Autowired
	TeamDao teamDao;
	
	
	public void addTeam(Team t){
		teamDao.addTeam(t);
		//throw new RuntimeException("error");
	}


	public TeamDao getTeamDao() {
		return teamDao;
	}

	public void setTeamDao(TeamDao teamDao) {
		this.teamDao = teamDao;
	}
	
} 
