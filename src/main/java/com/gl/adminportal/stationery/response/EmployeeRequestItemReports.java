package com.gl.adminportal.stationery.response;

import java.sql.Blob;
import java.util.Date;
import java.util.List;


public class EmployeeRequestItemReports {
	private String empName;
	private String empEmail;
	private Integer empId;
	private String approvedBy;
	private String location;
	private Date requestDate;
	
	private List<RequestedItems> requestedItems;
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EmployeeRequestItemReports [empName=" + empName + ", empEmail="
				+ empEmail + ", empId=" + empId + ", approvedBy=" + approvedBy
				+ ", location=" + location + ", requestDate=" + requestDate
				+ ", requestedItems=" + requestedItems + "]";
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
	 * @return the empId
	 */
	public Integer getEmpId() {
		return empId;
	}
	/**
	 * @param empId the empId to set
	 */
	public void setEmpId(Integer empId) {
		this.empId = empId;
	}
	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	/**
	 * @return the requestDate
	 */
	public Date getRequestDate() {
		return requestDate;
	}
	/**
	 * @param requestDate the requestDate to set
	 */
	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}
	/**
	 * @return the requestedItems
	 */
	public List<RequestedItems> getRequestedItems() {
		return requestedItems;
	}
	/**
	 * @param requestedItems the requestedItems to set
	 */
	public void setRequestedItems(List<RequestedItems> requestedItems) {
		this.requestedItems = requestedItems;
	}

	/**
	 * @return the approvedBy
	 */
	public String getApprovedBy() {
		return approvedBy;
	}
	/**
	 * @param approvedBy the approvedBy to set
	 */
	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public static class RequestedItems{
		
		private String itemName;
		private Integer quantity;
		private Integer price;
		private Blob imageStream;
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "RequestedItems [itemName=" + itemName + ", quantity="
					+ quantity + ", price=" + price + ", imageStream="
					+ imageStream + "]";
		}
		/**
		 * @return the itemName
		 */
		public String getItemName() {
			return itemName;
		}
		/**
		 * @param itemName the itemName to set
		 */
		public void setItemName(String itemName) {
			this.itemName = itemName;
		}
		/**
		 * @return the quantity
		 */
		public Integer getQuantity() {
			return quantity;
		}
		/**
		 * @param quantity the quantity to set
		 */
		public void setQuantity(Integer quantity) {
			this.quantity = quantity;
		}
		/**
		 * @return the price
		 */
		public Integer getPrice() {
			return price;
		}
		/**
		 * @param price the price to set
		 */
		public void setPrice(Integer price) {
			this.price = price;
		}
		/**
		 * @return the imageStream
		 */
		public Blob getImageStream() {
			return imageStream;
		}
		/**
		 * @param imageStream the imageStream to set
		 */
		public void setImageStream(Blob imageStream) {
			this.imageStream = imageStream;
		}
	}
}