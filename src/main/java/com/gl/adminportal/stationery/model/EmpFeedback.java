package com.gl.adminportal.stationery.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "feedback_")
public class EmpFeedback {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	 long id;

	@Column(name = "emp_id")
	 String empId;

	@Column(name ="emp_name")
	 String empName;

	@Column(name ="feedback")
	 String feedback;

	@Column(name ="location_name")
	 String locationName;

	@Column(name ="status")
	 String status;

	@Column(name ="email")
	 String email;

	@Column(name ="feedback_creation_date")
	 Date feedbackCreationDate;

	@Column(name ="feedback_updation_date")
	 Date feedbackUpdatedDate;


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getFeedbackCreationDate() {
		return feedbackCreationDate;
	}

	public void setFeedbackCreationDate(Date feedbackCreationDate) {
		this.feedbackCreationDate = feedbackCreationDate;
	}

	public Date getFeedbackUpdatedDate() {
		return feedbackUpdatedDate;
	}

	public void setFeedbackUpdatedDate(Date feedbackUpdatedDate) {
		this.feedbackUpdatedDate = feedbackUpdatedDate;
	}

	@Override
	public String toString() {
		return "{ \n" + "_id :  " + getId() + "\n empId : " + getEmpId()
				+ "\n empName : " + getEmpName() + "\n feedBack : "
				+ getFeedback() + "\n locationName : " + getLocationName()
				+ "\n status : " + getStatus() + "\n email : " + getEmail()
				+ "\n}";
	}
}
