package com.gl.adminportal.stationery.response;

import java.util.Date;

public class ValidateItemResponse {

	
	private Date request_creation_date;
	private int id;
	Long quantityOrderd;
	String location;
	public Date getRequest_creation_date() {
		return request_creation_date;
	}
	public void setRequest_creation_date(Date request_creation_date) {
		this.request_creation_date = request_creation_date;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public Long getQuantityOrderd() {
		return quantityOrderd;
	}
	public void setQuantityOrderd(Long quantityOrderd) {
		this.quantityOrderd = quantityOrderd;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	@Override
	public String toString() {
		return "ValidateItemResponse [request_creation_date=" + request_creation_date + ", id=" + id
				+ ", quantityOrderd=" + quantityOrderd + ", location=" + location + "]";
	}
	
	
	
	
}
