package com.yong.dao;

import org.springframework.stereotype.Repository;

import com.yong.base.BaseDao;
import com.yong.entity.test.Team;

@SuppressWarnings("all")
@Repository
public class TeamDao extends BaseDao<Team>{

	/*@Resource
	SessionFactory sf;*/
	
	public void addTeam(Team t){
		//Session session = sf.getCurrentSession();
		//session.save(t);
		//throw new RuntimeException("error");
		this.save(t);
	}

	/*public SessionFactory getSf() {
		return sf;
	}

	public void setSf(SessionFactory sf) {
		this.sf = sf;
	}*/
}
