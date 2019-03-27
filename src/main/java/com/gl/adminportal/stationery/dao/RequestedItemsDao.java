package com.gl.adminportal.stationery.dao;

import java.util.List;
import java.util.Set;

import com.gl.adminportal.stationery.model.Item;
import com.gl.adminportal.stationery.model.RequestedItems;

/**
 * This class provides data access layer for requested items 
 * table.
 * 
 * @author anand.kadhi
 *
 */
public interface RequestedItemsDao {

	List<RequestedItems> getRequestedItemsByItemId(Item item);
	
	void addBulkRequestedItems(Set<RequestedItems> requestedItem);
}
