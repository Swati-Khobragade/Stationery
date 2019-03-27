package com.gl.adminportal.stationery.dao;

import java.util.List;

import com.gl.adminportal.stationery.model.Item;
import com.gl.adminportal.stationery.model.Request;
import com.gl.adminportal.stationery.model.RequestedItems;
import com.gl.adminportal.stationery.response.ItemResponse;
import com.gl.adminportal.stationery.response.ValidateItemResponse;


public interface RequestDao {
	
	Long placeRequest(Request request );
	
	List<Request> fetchRequestByItemId(Item items);
	
	public List<Request> getRequestByEmpId(String empId, String locationName, String fromDate, String toDate);
	
	public List<Request> getAllPendingRequests(String locationName, String fromDate, String toDate);

	void updateRequestedItemState(List<Request> request, String locationName);
	
	Request fetchRequestById(RequestedItems requestedItem);

	List<Request> getPendingRequestByApprover(String approverName, String locationName);

	Request getRequestByReqIdAndApprover(long requestId, String approvedBy,
			String location);

	void updateRequestState(List<Request> request,String state, String locationName);

	Request getRequestById(String requestId, String locationName);
	
	void deleteRequest(String requestId, String locationName);
	
	Long getItemCount(int empId,String locationName, int itemId);
	
	Request getLastRequestedItem(int itemId, int empId);

	List<Request> getAllRequestedItem(String locationName);
}
