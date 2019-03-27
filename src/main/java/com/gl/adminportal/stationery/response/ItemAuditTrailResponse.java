package com.gl.adminportal.stationery.response;

import java.util.List;
import java.util.Map;

import com.gl.adminportal.stationery.model.ItemAuditTrail;

public class ItemAuditTrailResponse {
	String operationDate;
	List<ItemAuditTrail> itemAuditTrail;
	long expenditure;

	public String getOperationDate() {
		return operationDate;
	}

	public void setOperationDate(String operationDate) {
		this.operationDate = operationDate;
	}

	public List<ItemAuditTrail> getItemAuditTrail() {
		return itemAuditTrail;
	}

	public void setItemAuditTrail(List<ItemAuditTrail> itemAuditTrail) {
		this.itemAuditTrail = itemAuditTrail;
	}

	public long getExpenditure() {
		return expenditure;
	}

	public void setExpenditure(long expenditure) {
		this.expenditure = expenditure;
	}

}
