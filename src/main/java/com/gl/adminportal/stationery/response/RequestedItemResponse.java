/**
 * 
 */
package com.gl.adminportal.stationery.response;

import java.io.Serializable;
import java.util.Date;

import com.gl.adminportal.stationery.enums.REQUESTSTATE;

/**
 * @author sudhir.ahirkar
 *
 */
public class RequestedItemResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer itemId;

	private String name;

	private String locationName;

	private Integer quantity;

	private Date itemCreatedDate;

	private Date itemUpdatedDate;
	
	private String lastRequestDate;
	
	private REQUESTSTATE itemStatus;

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public Date getItemCreatedDate() {
		return itemCreatedDate;
	}

	public void setItemCreatedDate(Date itemCreatedDate) {
		this.itemCreatedDate = itemCreatedDate;
	}

	public Date getItemUpdatedDate() {
		return itemUpdatedDate;
	}

	public void setItemUpdatedDate(Date itemUpdatedDate) {
		this.itemUpdatedDate = itemUpdatedDate;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	
	public REQUESTSTATE getItemStatus() {
		return itemStatus;
	}

	public void setItemStatus(REQUESTSTATE itemStatus) {
		this.itemStatus = itemStatus;
	}
	

	public String getLastRequestDate() {
		return lastRequestDate;
	}

	public void setLastRequestDate(String lastRequestDate) {
		this.lastRequestDate = lastRequestDate;
	}

	@Override
	public String toString() {
		return "RequestedItemResponse [itemId=" + itemId + ", name=" + name
				+ ", locationName=" + locationName + ", quantity=" + quantity
				+ ", itemCreatedDate=" + itemCreatedDate + ", itemUpdatedDate="
				+ itemUpdatedDate + "]";
	}

	public RequestedItemResponse(Integer itemId, String name, String locationName, Integer quantity,
			String lastRequestDate, REQUESTSTATE itemStatus) {
		super();
		this.itemId = itemId;
		this.name = name;
		this.locationName = locationName;
		this.quantity = quantity;
		this.lastRequestDate = lastRequestDate;
		this.itemStatus = itemStatus;
	}

	public RequestedItemResponse() {
		super();
	}

	
}
