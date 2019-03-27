package com.gl.adminportal.stationery.service;

import java.util.List;

import org.json.JSONObject;

import com.gl.adminportal.stationery.model.Item;
import com.gl.adminportal.stationery.model.Request;
import com.gl.adminportal.stationery.request.PlaceRequest;
import com.gl.adminportal.stationery.response.MyRequestReportResponse;
import com.gl.adminportal.stationery.response.OrderResponse;
import com.gl.adminportal.stationery.response.RequestedItemResponse;


public interface RequestService {
	
	public List<RequestedItemResponse> placeRequest(PlaceRequest placeRequest,String location);
	
	public List<Request> fetchRequestByItemId(Item items);
	
	public Request fatchRequestById(Long requestId);
	
	List<MyRequestReportResponse> getRequestedItemByEmpId(String empId, String locationName, String fromDate, String toDate);
	
	public List<MyRequestReportResponse> getAllPendingRequests(String locationName, String fromDate, String toDate);

	public List<MyRequestReportResponse> getPendingRequestByApprover(String approverName, String locationName);

	public void updateRequestState(List<Request> request,String state, String locationName);
	
	void deleterequest(String requestId, String locationName);
	
	public void updateRequestedItemState(List<Request> request, String locationName);
	
	public List<RequestedItemResponse> validateItems(PlaceRequest placeRequest,String locationName);
	
    public Long getItemCount(int empId,String locationName, int itemId);
	
	public Request getLastRequestedItem(int itemId, int empId)  ;

	List<OrderResponse> getAllRequestedItem(String locationName);
}
