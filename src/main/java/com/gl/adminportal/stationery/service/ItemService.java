package com.gl.adminportal.stationery.service;

import java.util.List;

import com.gl.adminportal.stationery.enums.ITEMOPERATION;
import com.gl.adminportal.stationery.exception.SystemException;
import com.gl.adminportal.stationery.model.Item;
import com.gl.adminportal.stationery.model.ItemAuditTrail;
import com.gl.adminportal.stationery.request.ItemRequest;

public interface ItemService {
	int addItem(List<ItemRequest> items, String locatioName);

	void updateItem(List<ItemRequest> itemRequest, String locationName);

	void deleteItem(List<ItemRequest> itemRequest, String locationName);

	List<Item> getAllItem(String locationName, String role);

	Item getItemById(int id, String locationName, String role);

	void addItemToAuditTrail(List<ItemRequest> items, String locatioName,
			ITEMOPERATION itemOperation, int itemId);

	void updateItemInAuditTrail(List<ItemRequest> itemRequest,
			String locationName, ITEMOPERATION itemOperation);

	List<ItemAuditTrail> getAuditTrail(String locationName,String startDate, String endDate);
	
	void disableItem(List<ItemRequest> itemRequest, String locationName) throws SystemException;
	
	List<Item> serachItem(String itemName, String location);
	
	List<Item> getLatestItem(String locationName);

}
