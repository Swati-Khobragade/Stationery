package com.gl.adminportal.stationery.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "itemaudittrail_")
public class ItemAuditTrail {

	@Id
	@GeneratedValue
	@Column(name = "id")
	int id;

	@Column(name = "itemId")
	int itemId;

	@Column(name = "name")
	String name;

	@Column(name = "locationName")
	String locationName;

	@Column(name = "price")
	int price;

	@Column(name = "quantity")
	int quantity;

	@Column(name = "timestamp")
	Date timestamp;

	@Column(name = "itemOperation")
	String itemOperation;

	@Column(name = "totalPrice")
	int totalPrice;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
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

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getItemOperation() {
		return itemOperation;
	}

	public void setItemOperation(String itemOperation) {
		this.itemOperation = itemOperation;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	@Override
	public String toString() {
		return "AuditTrail [id=" + id + ", itemId=" + itemId + ", name=" + name
				+ ", locationName=" + locationName + ", price=" + price
				+ ", quantity=" + quantity + ", timestamp=" + timestamp
				+ ", itemOperation=" + itemOperation + ", totalPrice="
				+ totalPrice + "]";
	}

}
