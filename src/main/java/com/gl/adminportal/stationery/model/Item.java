package com.gl.adminportal.stationery.model;

import java.sql.Blob;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * item model
 * 
 * @author neeta.kumbhare
 *
 */
@Entity
@Table(name = "item_")
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	int id;

	@Column(name = "name")
	String name;

	@Column(name = "price")
	int price;

	@Column(name = "location")
	String location;

	@Column(name = "quantity")
	int quantity;

	@Column(name = "need_approval")
	boolean needApproval;

	@Column(name = "creation_date")
	Date itemCreationDate;

	@Column(name = "updation_date")
	Date itemUpdationDate;

	@Column(name = "enabled")
	boolean enabled;

	@Column(name = "image_stream")
	private Blob image_stream;

	@OneToMany(mappedBy = "itemFk", cascade = CascadeType.ALL)
	private Set<RequestedItems> requestedItems;
	
	public Item() {
		super();
	}

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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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

	public Date getItemCreationDate() {
		return itemCreationDate;
	}

	public void setItemCreationDate(Date itemCreationDate) {
		this.itemCreationDate = itemCreationDate;
	}

	public Date getItemUpdationDate() {
		return itemUpdationDate;
	}

	public void setItemUpdationDate(Date itemUpdationDate) {
		this.itemUpdationDate = itemUpdationDate;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Blob getImage_stream() {
		return image_stream;
	}

	public void setImage_stream(Blob image_stream) {
		this.image_stream = image_stream;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", name=" + name + ", price=" + price
				+ ", location=" + location + ", quantity=" + quantity
				+ ", needApproval=" + needApproval + ", itemCreationDate="
				+ itemCreationDate + ", itemUpdationDate=" + itemUpdationDate
				+ ", enabled=" + enabled + ", image_stream=" + image_stream
				+ "]";
	}

}
