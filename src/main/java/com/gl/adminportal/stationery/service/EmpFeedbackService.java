package com.gl.adminportal.stationery.service;

import java.util.List;

import com.gl.adminportal.stationery.model.EmpFeedback;

public interface EmpFeedbackService {
	
	int saveFeedback(EmpFeedback empFeedback, String locationName);

	List<EmpFeedback> getEmpFeedbackByEmpId(String  locationName, String empId);
	
	List<EmpFeedback> getEmpFeedbackByStatus(String locationName, String status);
	
	EmpFeedback getEmpFeedbackById(String locationName, String feedbackId);

	void changeFeedbackStatus(String locationName, String feedbackId,String status);

	List<EmpFeedback> getEmpFeedback(String locationName, Integer pageNo, String status);
}
