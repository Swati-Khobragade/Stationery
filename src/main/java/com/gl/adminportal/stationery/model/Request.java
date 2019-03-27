package com.gl.adminportal.stationery.model;

import java.util.Date;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "request_")
public class Request {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	long id;
	
	@Column(name = "request_creation_date")
	Date request_creation_date;

	@Column(name = "request_updation_date")
	Date request_updation_date;
	
	@Column(name = "emp_email")
	String employeeEmail;
	
	@Column(name= "emp_name")
	String 	employeeName;
	
	@Column(name = "emp_id")
	int emp_id;
	
	@Column(name = "state")
//	REQUESTSTATE state;
	String state;
	
	@Column(name = "location")
	String location;

	@Column(name = "approved_by")
	String approved_by;
	
	@Column(name = "approval_required")
	boolean approval_required;
	
	@OneToMany(mappedBy = "requestFk", cascade = CascadeType.ALL,fetch = FetchType.LAZY )
	private Set<RequestedItems> requestedItems;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the request_creation_date
	 */
	public Date getRequest_creation_date() {
		return request_creation_date;
	}

	/**
	 * @param request_creation_date the request_creation_date to set
	 */
	public void setRequest_creation_date(Date request_creation_date) {
		this.request_creation_date = request_creation_date;
	}

	/**
	 * @return the request_updation_date
	 */
	public Date getRequest_updation_date() {
		return request_updation_date;
	}

	/**
	 * @param request_updation_date the request_updation_date to set
	 */
	public void setRequest_updation_date(Date request_updation_date) {
		this.request_updation_date = request_updation_date;
	}

	

	public String getEmployeeEmail() {
		return employeeEmail;
	}

	public void setEmployeeEmail(String employeeEmail) {
		this.employeeEmail = employeeEmail;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	/**
	 * @return the emp_id
	 */
	public int getEmp_id() {
		return emp_id;
	}

	/**
	 * @param emp_id the emp_id to set
	 */
	public void setEmp_id(int emp_id) {
		this.emp_id = emp_id;
	}

//	/**
//	 * @return the state
//	 */
//	public REQUESTSTATE getState() {
//		return state;
//	}
//
//	/**
//	 * @param state the state to set
//	 */
//	public void setState(REQUESTSTATE state) {
//		this.state = state;
//	}
	
	

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the approved_by
	 */
	public String getApproved_by() {
		return approved_by;
	}

	/**
	 * @param approved_by the approved_by to set
	 */
	public void setApproved_by(String approved_by) {
		this.approved_by = approved_by;
	}

	/**
	 * @return the approval_required
	 */
	public boolean isApproval_required() {
		return approval_required;
	}

	/**
	 * @param approval_required the approval_required to set
	 */
	public void setApproval_required(boolean approval_required) {
		this.approval_required = approval_required;
	}

	/**
	 * @return the requestedItems
	 */
	public Set<RequestedItems> getRequestedItems() {
		return requestedItems;
	}

	/**
	 * @param requestedItems the requestedItems to set
	 */
	public void setRequestedItems(Set<RequestedItems> requestedItems) {
		this.requestedItems = requestedItems;
	}

}

