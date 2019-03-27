package com.gl.adminportal.stationery.utils;

import java.util.ArrayList;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.gl.adminportal.stationery.model.Item;
import com.gl.adminportal.stationery.model.ItemAuditTrail;
import com.gl.adminportal.stationery.response.ItemAuditTrailResponse;
import com.gl.adminportal.stationery.response.ItemResponse;

@Component
public class ResponseConvertor {
	private static final Logger LOGGER = Logger
			.getLogger(ResponseConvertor.class);

	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	public List convertToItemAuditTrailResponse(
			List<ItemAuditTrail> itemAuditTrialList) {
		List itemAuditTrailList = new ArrayList<>();
		Map<String, List<ItemAuditTrail>> operationMap = new HashMap<>();
		List<ItemAuditTrail> itemAuditrails = null;

		for (ItemAuditTrail itemAuditTrail : itemAuditTrialList) {
			Date itemAddedDate = itemAuditTrail.getTimestamp();
			String date = DateUtils.formatDateUsing(itemAddedDate);

			if (operationMap.containsKey(date)) {
				// fetch all existing corrosponding list and add new one
				List<ItemAuditTrail> existingItems = operationMap.get(date);
				existingItems.add(itemAuditTrail);
				operationMap.put(date, existingItems);
			} else {
				List<ItemAuditTrail> existingItems = new ArrayList<>();
				existingItems.add(itemAuditTrail);
				operationMap.put(date, existingItems);
			}
		}

		for (Entry<String, List<ItemAuditTrail>> entry : operationMap
				.entrySet()) {
			ItemAuditTrailResponse itemResponse = new ItemAuditTrailResponse();
			itemResponse.setOperationDate(entry.getKey());
			itemResponse.setItemAuditTrail(entry.getValue());
			List<ItemAuditTrail> itemList = entry.getValue();
			int totalExpenditure = 0;
			for (ItemAuditTrail i : itemList) {
				totalExpenditure += i.getTotalPrice();
			}
			itemResponse.setExpenditure(totalExpenditure);
			itemAuditTrailList.add(itemResponse);
		}
		LOGGER.info("Items list " + operationMap);
		return itemAuditTrailList;

	}

	public static ItemResponse getItemRespone(Item item) {
		ItemResponse itemResponse = new ItemResponse();
		itemResponse.setId(item.getId());
		itemResponse.setName(item.getName());
		itemResponse.setPrice(item.getPrice());
		itemResponse.setLocation(item.getLocation());
		itemResponse.setNeedApproval(item.isNeedApproval());
		itemResponse.setItemCreationDate(DateUtils.convertDateToString(item
				.getItemCreationDate()));
		itemResponse.setItemUpdationDate(DateUtils.convertDateToString(item
				.getItemUpdationDate()));
		itemResponse.setEnabled(item.isEnabled());
		itemResponse.setImageStream(item.getImage_stream());
		return itemResponse;
	}
}
