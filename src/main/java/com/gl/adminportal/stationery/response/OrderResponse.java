package com.gl.adminportal.stationery.response;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gl.adminportal.stationery.model.Item;

public class OrderResponse {

	@JsonProperty("_id")
	private String id;

	@JsonProperty("empId")
	private String empId;

	@JsonProperty("empName")
	private String empName;

	@JsonProperty("locationName")
	private String locationName;

	@JsonProperty("status")
	private String status;

	@JsonProperty("amt")
	private Long amt;

	@JsonProperty("itemList")
	private ArrayList<Item> itemList;

	@JsonProperty("order_created_date")
	private String orderCreatedDate;

	@JsonProperty("order_updated_date")
	private String orderUpdatedDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
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

	public Long getAmt() {
		return amt;
	}

	public void setAmt(Long amt) {
		this.amt = amt;
	}

	public ArrayList<Item> getItemList() {
		return itemList;
	}

	public void setItemList(ArrayList<Item> itemList) {
		this.itemList = itemList;
	}

	public String getOrderCreatedDate() {
		return orderCreatedDate;
	}

	public void setOrderCreatedDate(String orderCreatedDate) {
		this.orderCreatedDate = orderCreatedDate;
	}

	public String getOrderUpdatedDate() {
		return orderUpdatedDate;
	}

	public void setOrderUpdatedDate(String orderUpdatedDate) {
		this.orderUpdatedDate = orderUpdatedDate;
	}

	@Override
	public String toString() {
		return "OrderResponse [id=" + id + ", empId=" + empId + ", empName="
				+ empName + ", locationName=" + locationName + ", status="
				+ status + ", amt=" + amt + ", itemList=" + itemList
				+ ", orderCreatedDate=" + orderCreatedDate
				+ ", orderUpdatedDate=" + orderUpdatedDate + "]";
	}
	
}

