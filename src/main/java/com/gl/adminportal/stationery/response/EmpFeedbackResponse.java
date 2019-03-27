package com.gl.adminportal.stationery.response;

import java.util.List;

import com.gl.adminportal.stationery.model.EmpFeedback;

public class EmpFeedbackResponse {

	int count;
	
	List<EmpFeedback> empFeedback;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<EmpFeedback> getEmpFeedback() {
		return empFeedback;
	}

	public void setEmpFeedback(List<EmpFeedback> empFeedback) {
		this.empFeedback = empFeedback;
	}
	
	

}
