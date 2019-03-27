package com.gl.adminportal.stationery.request;

import java.sql.Blob;

/**
 * This is the item request class
 * 
 * @author neeta.kumbhare
 *
 */
public class ItemRequest {

	/** Id of item **/
	int id;

	/** Name of item **/
	String name;

	/** Price of item **/
	int price;

	/** Whether it need approval **/
	boolean needApproval;

	/** Quantity available **/
	int quantity;

	/** ImageStream of item **/
	Blob imageStream;

	/**state of Item **/
	boolean enable;

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

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public boolean isNeedApproval() {
		return needApproval;
	}

	public void setNeedApproval(boolean needApproval) {
		this.needApproval = needApproval;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Blob getImageStream() {
		return imageStream;
	}

	public void setImageStream(Blob imageStream) {
		this.imageStream = imageStream;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	@Override
	public String toString() {
		return "ItemRequest [id=" + id + ", name=" + name + ", price=" + price
				+ ", needApproval=" + needApproval + ", quantity=" + quantity
				+ ", imageStream=" + imageStream + "]";
	}

}
