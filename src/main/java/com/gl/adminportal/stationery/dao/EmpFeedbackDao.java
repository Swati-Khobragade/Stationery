package com.gl.adminportal.stationery.dao;

import java.util.List;

import com.gl.adminportal.stationery.model.EmpFeedback;

public interface EmpFeedbackDao {
	
	int saveFeedback(EmpFeedback empFeedback, String locationName);

	List<EmpFeedback> getEmpFeedbackByEmpId(String  locationName, String empId);
	
	EmpFeedback getEmpFeedbackById(String  locationName, String feedbackId);
	
	List<EmpFeedback> getEmpFeedbackByStatus(String locationName, String status);
	
	void changeFeedbackStatus(String locationName, String feedbackId,String status);

	List<EmpFeedback> getEmpFeedback(String locationName, Integer pageNo, String status);
}
