package com.gl.adminportal.stationery.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gl.adminportal.stationery.dao.TestDao;
import com.gl.adminportal.stationery.model.Test;
import com.gl.adminportal.stationery.service.TestService;

@Service("testService")
public class TestServiceImpl implements TestService {

	@Autowired
	TestDao testDao;

	public void setTestDao(TestDao testDao) {
		this.testDao = testDao;
	}

	@Override
	@Transactional  
	public void save(Test test) {
		testDao.save(test);

	}

	@Override
	public void update(Test test) {
		testDao.update(test);

	}

	@Override
	public void delete(Test test) {
		testDao.delete(test);

	}

}
