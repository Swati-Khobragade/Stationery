package com.gl.adminportal.stationery.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "requestedItems_")
public class RequestedItems {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	long id;
	
	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "request_id") 
    Request requestFk;

	@ManyToOne
    @JoinColumn(name = "item_id")
	Item itemFk;
	
	@Column(name = "quantity")
	int quantityOrderd;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the requestFk
	 */
	public Request getRequestFk() {
		return requestFk;
	}

	/**
	 * @param requestFk the requestFk to set
	 */
	public void setRequestFk(Request requestFk) {
		this.requestFk = requestFk;
	}

	/**
	 * @return the itemFk
	 */
	public Item getItemFk() {
		return itemFk;
	}

	/**
	 * @param itemFk the itemFk to set
	 */
	public void setItemFk(Item itemFk) {
		this.itemFk = itemFk;
	}

	/**
	 * @return the quantityOrderd
	 */
	public int getQuantityOrderd() {
		return quantityOrderd;
	}

	/**
	 * @param quantityOrderd the quantityOrderd to set
	 */
	public void setQuantityOrderd(int quantityOrderd) {
		this.quantityOrderd = quantityOrderd;
	}

	@Override
	public String toString() {
		return "RequestedItems [id=" + id + ", requestFk=" + requestFk
				+ ", itemFk=" + itemFk + ", quantityOrderd=" + quantityOrderd
				+ "]";
	}
	
	
	
}

