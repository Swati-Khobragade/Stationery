package com.gl.adminportal.stationery.dao;

import java.text.ParseException;
import java.util.List;

import com.gl.adminportal.stationery.model.Item;
import com.gl.adminportal.stationery.model.Request;

public interface ReportDao {

	public List<Item> geItemsListbyLocations(String locationName);
	
	List<Request> getRequestByFilters(String empName, String startDate, String endDate) throws ParseException;
}
