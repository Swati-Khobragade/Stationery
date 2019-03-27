/**
 * 
 */
package com.gl.adminportal.stationery.response;

import java.util.Date;
import java.util.List;

/**
 * @author sudhir.ahirkar
 *
 */
public class MyRequestReportResponse {

	long id;

	List<RequestedItemResponse> itemList;

	String status;

	Date requestCreatedDate;

	String empName;

    int empId;

	String empEmail;

	String approvedBy;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<RequestedItemResponse> getItemList() {
		return itemList;
	}

	public void setItemList(List<RequestedItemResponse> itemList) {
		this.itemList = itemList;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getRequestCreatedDate() {
		return requestCreatedDate;
	}

	public void setRequestCreatedDate(Date requestCreatedDate) {
		this.requestCreatedDate = requestCreatedDate;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	
	public String getEmpEmail() {
		return empEmail;
	}

	public void setEmpEmail(String empEmail) {
		this.empEmail = empEmail;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	@Override
	public String toString() {
		return "MyRequestReportResponse [id=" + id + ", itemList=" + itemList + ", status=" + status
				+ ", requestCreatedDate=" + requestCreatedDate + ", empName=" + empName + ", empId=" + empId
				+ ", empEmail=" + empEmail + ", approvedBy=" + approvedBy + ", getId()=" + getId() + ", getItemList()="
				+ getItemList() + ", getStatus()=" + getStatus() + ", getRequestCreatedDate()="
				+ getRequestCreatedDate() + ", getEmpName()=" + getEmpName() + ", getEmpEmail()=" + getEmpEmail()
				+ ", getApprovedBy()=" + getApprovedBy() + ", getEmpId()=" + getEmpId() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

	

	

}
