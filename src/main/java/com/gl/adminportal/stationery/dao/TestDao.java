package com.gl.adminportal.stationery.dao;

import com.gl.adminportal.stationery.model.Test;

public interface TestDao {

	Test save(Test test);
	void update(Test test);
	void delete(Test test);
}
