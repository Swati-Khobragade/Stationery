package com.gl.adminportal.stationery.request;

import java.util.List;

/**
 * This class is meant for holding the request for 
 * placing the order/request.
 * 
 * @author anand.kadhi
 *
 */
public class PlaceRequest {
	
	/** {@link ItemRequest ItemDeatils} which are being ordered **/
	private List<ItemRequest> itemDetails;
	
	/** Id of Employee who has ordered these items **/
	private long empId;

	/** Name of Employee who has ordered these items **/
	private String empName;
	
	/** Email of Employee who has ordered these items **/
	private String empEmail;
	
	/** Email of Person (usually a Manager) who is going to approve this order **/
	private String approver;

	/**
	 * @return the itemDetails
	 */
	public List<ItemRequest> getItemDetails() {
		return itemDetails;
	}

	/**
	 * @param itemDetails the itemDetails to set
	 */
	public void setItemDetails(List<ItemRequest> itemDetails) {
		this.itemDetails = itemDetails;
	}

	/**
	 * @return the empId
	 */
	public long getEmpId() {
		return empId;
	}

	/**
	 * @param empId the empId to set
	 */
	public void setEmpId(long empId) {
		this.empId = empId;
	}

	/**
	 * @return the empName
	 */
	public String getEmpName() {
		return empName;
	}

	/**
	 * @param empName the empName to set
	 */
	public void setEmpName(String empName) {
		this.empName = empName;
	}

	/**
	 * @return the empEmail
	 */
	public String getEmpEmail() {
		return empEmail;
	}

	/**
	 * @param empEmail the empEmail to set
	 */
	public void setEmpEmail(String empEmail) {
		this.empEmail = empEmail;
	}

	/**
	 * @return the approver
	 */
	public String getApprover() {
		return approver;
	}

	/**
	 * @param approver the approver to set
	 */
	public void setApprover(String approver) {
		this.approver = approver;
	}
	
}
