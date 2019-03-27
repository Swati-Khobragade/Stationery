package com.gl.adminportal.stationery.service;

import java.util.List;

import com.gl.adminportal.stationery.model.Request;
import com.gl.adminportal.stationery.response.EmployeeRequestItemReports;
import com.gl.adminportal.stationery.response.ItemResponse;


public interface ReportService {

	public List<EmployeeRequestItemReports> getEmpolyeeRequestReport(String empName, String startDate, String endDate);

	public List<ItemResponse> geItemsListbyLocations(String locationName);
	
}
