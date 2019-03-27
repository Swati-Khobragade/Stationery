package com.gl.adminportal.stationery.dao;

import java.util.List;

import com.gl.adminportal.stationery.enums.ITEMOPERATION;
import com.gl.adminportal.stationery.exception.SystemException;
import com.gl.adminportal.stationery.model.Item;
import com.gl.adminportal.stationery.model.ItemAuditTrail;
import com.gl.adminportal.stationery.request.ItemRequest;
import com.gl.adminportal.stationery.response.ItemAuditTrailResponse;

public interface ItemDao {
	int addItem(List<ItemRequest> items, String locationName);

	List<Item> getItemByNameAndLocation(ItemRequest itemReq, String locationName);

	Item getItemById(int id, String locationName);

	Item getItemByIdRole(int id, String locationName, String role);

	void updateItem(List<ItemRequest> itemRequest, String locationName);

	void deleteItem(List<ItemRequest> itemRequest, String locationName);

	List<Item> getAllItems(String locationName, String role);

	void addItemToAuditTrail(List<ItemRequest> items, String locationName,
			ITEMOPERATION itemOperation, int itemId);

	void updateItemInAuditTrail(List<ItemRequest> itemRequest,
			String locationName, ITEMOPERATION itemOperation);

	List<ItemAuditTrail> getAuditTrail(String locationName,String startDate, String endDate);
	
	public void disableItem(List<ItemRequest> itemRequest, String locationName) throws SystemException ;
	
	List<Item> serachItem(String itemName, String location);
	
	List<Item> getLatestItem(String locationName);
}
