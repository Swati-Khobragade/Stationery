package com.gl.adminportal.stationery.response;

import java.sql.Blob;


public class ItemResponse {

	int id;

	String name;

	float price;

	String location;

	int quantity;

	boolean needApproval;

	String itemCreationDate;

	String itemUpdationDate;
	
	boolean enabled;
	
	Blob imageStream;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public boolean isNeedApproval() {
		return needApproval;
	}

	public void setNeedApproval(boolean needApproval) {
		this.needApproval = needApproval;
	}

	public String getItemCreationDate() {
		return itemCreationDate;
	}

	public void setItemCreationDate(String itemCreationDate) {
		this.itemCreationDate = itemCreationDate;
	}

	public String getItemUpdationDate() {
		return itemUpdationDate;
	}

	public void setItemUpdationDate(String itemUpdationDate) {
		this.itemUpdationDate = itemUpdationDate;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Blob getImageStream() {
		return imageStream;
	}

	public void setImageStream(Blob imageStream) {
		this.imageStream = imageStream;
	}
	
	
}
