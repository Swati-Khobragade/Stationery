package com.gl.adminportal.stationery.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gl.adminportal.stationery.dao.EmpFeedbackDao;
import com.gl.adminportal.stationery.model.EmpFeedback;
import com.gl.adminportal.stationery.service.EmpFeedbackService;

/**
 * service class for Employee feedback
 * 
*/
@Service("EmpFeedbackService")
public class EmpFeedbackServiceImpl implements EmpFeedbackService {

	@Autowired
	EmpFeedbackDao empFeedbackDao;

	@Override
	public int saveFeedback(EmpFeedback empFeedback, String locationName) {
		return empFeedbackDao.saveFeedback(empFeedback, locationName);
	}

	@Override
	public List<EmpFeedback> getEmpFeedbackByEmpId(String locationName,
			String empId) {
		return empFeedbackDao.getEmpFeedbackByEmpId(locationName, empId);
	}

	@Override
	public EmpFeedback getEmpFeedbackById(String locationName, String feedbackId) {
		return empFeedbackDao.getEmpFeedbackById(locationName, feedbackId);
	}

	@Override
	public List<EmpFeedback> getEmpFeedbackByStatus(String locationName, String status) {
		return empFeedbackDao.getEmpFeedbackByStatus(locationName, status);
	}

	@Override
	public void changeFeedbackStatus(String locationName, String feedbackId,
			String status) {
		empFeedbackDao.changeFeedbackStatus(locationName, feedbackId, status);
		
	}

	@Override
	public List<EmpFeedback> getEmpFeedback(String locationName, Integer pageNo, String status) {
		return empFeedbackDao.getEmpFeedback(locationName,pageNo,status);
	}
}
