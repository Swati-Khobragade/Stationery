package com.gl.adminportal.stationery.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gl.adminportal.stationery.dao.ItemDao;
import com.gl.adminportal.stationery.enums.ITEMOPERATION;
import com.gl.adminportal.stationery.exception.SystemException;
import com.gl.adminportal.stationery.model.Item;
import com.gl.adminportal.stationery.model.ItemAuditTrail;
import com.gl.adminportal.stationery.request.ItemRequest;
import com.gl.adminportal.stationery.service.ItemService;

/**
 * service class for items
 * 
 * @author neeta.kumbhare
 *
 */
@Service("ItemService")
public class ItemServiceImpl implements ItemService {

	@Autowired
	ItemDao itemDao;

	@Override
	public int addItem(List<ItemRequest> items, String locatioName) {
		return itemDao.addItem(items, locatioName);
	}

	@Override
	public void updateItem(List<ItemRequest> itemRequest, String locationName) {
		itemDao.updateItem(itemRequest, locationName);
	}

	@Override
	public void deleteItem(List<ItemRequest> itemRequest, String locationName) {
		itemDao.deleteItem(itemRequest, locationName);

	}

	@Override
	public List<Item> getAllItem(String locationName, String role) {
		return itemDao.getAllItems(locationName, role);
	}

	@Override
	public Item getItemById(int id, String locationName, String role) {
		return itemDao.getItemByIdRole(id, locationName, role);
	}

	@Override
	public void addItemToAuditTrail(List<ItemRequest> items,
			String locationName, ITEMOPERATION itemOperation, int itemId) {
		itemDao.addItemToAuditTrail(items, locationName, itemOperation, itemId);
	}

	@Override
	public void updateItemInAuditTrail(List<ItemRequest> itemRequest,
			String locationName, ITEMOPERATION itemOperation) {
		itemDao.updateItemInAuditTrail(itemRequest, locationName, itemOperation);

	}

	@Override
	public List<ItemAuditTrail> getAuditTrail(String locationName,String startDate, String endDate) {
		return itemDao.getAuditTrail(locationName,startDate, endDate);
	}
	
	@Override
	public void disableItem(List<ItemRequest> itemRequest, String locationName) throws SystemException {
		itemDao.disableItem(itemRequest, locationName);

	}

	@Override
	public List<Item> serachItem(String itemName, String location) {
		return itemDao.serachItem(itemName, location);
	}

	@Override
	public List<Item> getLatestItem(String locationName) {
		return itemDao.getLatestItem(locationName);
	}
}
