package com.gl.adminportal.stationery.dao.impl;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gl.adminportal.stationery.dao.TestDao;
import com.gl.adminportal.stationery.model.Test;

@Repository("testDao")
public class TestDaoImpl implements TestDao{

	@Autowired  
	 private SessionFactory sessionFactory;  
	  
	 public void setSessionFactory(SessionFactory sf) {  
	  this.sessionFactory = sf;  
	 }  

	@Override
	public Test save(Test test) {
		Session session = null;  
		try {
		    session = sessionFactory.getCurrentSession();
		} catch (HibernateException e) {
		    session = sessionFactory.openSession();
		}
		session.persist(test);  
		return test;
			
	}

	@Override
	public void update(Test test) {
		Session session = this.sessionFactory.getCurrentSession();  
		session.update(test);  
		
		
	}

	@Override
	public void delete(Test test) {
		Session session = this.sessionFactory.getCurrentSession();  
		session.delete(test);  
			
	}
	
}
