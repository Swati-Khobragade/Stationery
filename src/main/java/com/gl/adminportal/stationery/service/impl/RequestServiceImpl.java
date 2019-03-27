package com.gl.adminportal.stationery.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gl.adminportal.stationery.dao.ItemDao;
import com.gl.adminportal.stationery.dao.RequestDao;
import com.gl.adminportal.stationery.dao.RequestedItemsDao;
import com.gl.adminportal.stationery.enums.REQUESTSTATE;
import com.gl.adminportal.stationery.model.Item;
import com.gl.adminportal.stationery.model.Request;
import com.gl.adminportal.stationery.model.RequestedItems;
import com.gl.adminportal.stationery.request.ItemRequest;
import com.gl.adminportal.stationery.request.PlaceRequest;
import com.gl.adminportal.stationery.response.MyRequestReportResponse;
import com.gl.adminportal.stationery.response.OrderResponse;
import com.gl.adminportal.stationery.response.RequestedItemResponse;
import com.gl.adminportal.stationery.service.RequestService;
import com.gl.adminportal.stationery.utils.ItemEnum;

@Service
public class RequestServiceImpl implements RequestService {

	private static final Logger LOGGER = Logger.getLogger(RequestServiceImpl.class);

	@Autowired
	private RequestDao requestDao;

	@Autowired
	private ItemDao itemDao;

	@Autowired
	private RequestedItemsDao requestedItemsDao;

	@Override
	public List<RequestedItemResponse> placeRequest(PlaceRequest placeRequest, String locationName) {

		// List for separating approval required and non-required
		Set<RequestedItems> approvalReqSet = new LinkedHashSet<RequestedItems>();
		Set<RequestedItems> noApprovalSet = new LinkedHashSet<RequestedItems>();
        List<RequestedItemResponse> requestedItemResponses = new ArrayList<RequestedItemResponse>();
		// boolean approveRecs=f;alse;
		boolean nonApproveRecs = false, approveRec = false;

		Request approvalRequest = new Request();
		Request nonApprovalRequest = new Request();
		List<Long> generatedRequestIds = new LinkedList<Long>();

		for (ItemRequest itemRequest : placeRequest.getItemDetails()) {
			if (itemRequest.isNeedApproval()) {
				approveRec = true;
			} else {
				nonApproveRecs = true;
			}
		}
		if (approveRec) {
			approvalRequest.setApproval_required(true);
			approvalRequest.setApproved_by(placeRequest.getApprover());
			approvalRequest.setEmp_id((int) placeRequest.getEmpId());
			approvalRequest.setLocation(locationName);
			approvalRequest.setRequest_creation_date(new Date());
			approvalRequest.setEmployeeName(placeRequest.getEmpName());
			approvalRequest.setEmployeeEmail(placeRequest.getEmpEmail());
			approvalRequest.setState(REQUESTSTATE.CREATED.name());
			generatedRequestIds.add(requestDao.placeRequest(approvalRequest));

		}

		if (nonApproveRecs) {
			nonApprovalRequest.setApproval_required(true);
			nonApprovalRequest.setApproved_by(placeRequest.getApprover());
			nonApprovalRequest.setEmp_id((int) placeRequest.getEmpId());
			nonApprovalRequest.setLocation(locationName);
			nonApprovalRequest.setRequest_creation_date(new Date());
			nonApprovalRequest.setEmployeeName(placeRequest.getEmpName());
			nonApprovalRequest.setEmployeeEmail(placeRequest.getEmpEmail());
			// nonApprovalRequest.setState(REQUESTSTATE.PENDING_APPROVAL);
			nonApprovalRequest.setState(REQUESTSTATE.PENDING_APPROVAL.name());
			generatedRequestIds.add(requestDao.placeRequest(nonApprovalRequest));
		}

		
		
		// Separate List of Items on basis of Approval Required
		String date=new SimpleDateFormat("dd/MM/yyyy").format(new Date()) ;
		for (ItemRequest itemRequest : placeRequest.getItemDetails()) {
			RequestedItems item = new RequestedItems();
			if (itemRequest.isNeedApproval()) {
				item.setItemFk(itemDao.getItemById(itemRequest.getId(), locationName));
				item.setQuantityOrderd(itemRequest.getQuantity());
				item.setRequestFk(approvalRequest);
				requestedItemResponses.add(new RequestedItemResponse(itemRequest.getId(), itemRequest.getName(), locationName, itemRequest.getQuantity(),date, REQUESTSTATE.CREATED));
				approvalReqSet.add(item);
			} else {
				item.setItemFk(itemDao.getItemById(itemRequest.getId(), locationName));
				item.setQuantityOrderd(itemRequest.getQuantity());
				item.setRequestFk(nonApprovalRequest);
				requestedItemResponses.add(new RequestedItemResponse(itemRequest.getId(), itemRequest.getName(), locationName, itemRequest.getQuantity(), date, REQUESTSTATE.CREATED));
				noApprovalSet.add(item);
			}
		}

		if(approvalReqSet.size()>0){
		requestedItemsDao.addBulkRequestedItems(approvalReqSet);
		}
		if(noApprovalSet.size()>0){
		requestedItemsDao.addBulkRequestedItems(noApprovalSet);
		}
		// nonApprovalRequest.setRequestedItems(noApprovalSet);
		// approvalRequest.setRequestedItems(approvalReqSet);
        
		return requestedItemResponses;
	}

	@Override
	public List<Request> fetchRequestByItemId(Item items) {

		return null;
	}

	@Override
	public Request fatchRequestById(Long requestId) {

		return null;
	}

	@Override
	public List<MyRequestReportResponse> getRequestedItemByEmpId(String empId, String locationName, String fromDate, String toDate) {
		LOGGER.info("Entered into RequestedItemServiceImpl - getRequestedItemByEmpId()");
		List<MyRequestReportResponse> requestReportResponses = new ArrayList<>();
		List<Request> requests = requestDao.getRequestByEmpId(empId, locationName, fromDate, toDate);
		if (requests != null && !requests.isEmpty()) {
		//	requestReportResponses = new ArrayList<MyRequestReportResponse>();
			for (Request request : requests) {
				MyRequestReportResponse requestReportResponse = new MyRequestReportResponse();
				requestReportResponse.setId(request.getId());
				List<RequestedItemResponse> itemResponses = new ArrayList<RequestedItemResponse>();
				Set<RequestedItems> requestedItems = request.getRequestedItems();
				for (RequestedItems requestedItem : requestedItems) {
					RequestedItemResponse itemResponse = new RequestedItemResponse();
					Item item = itemDao.getItemById(requestedItem.getItemFk().getId(), request.getLocation());
					itemResponse.setItemId(requestedItem.getItemFk().getId());
					itemResponse.setName(item.getName());
					itemResponse.setLocationName(request.getLocation());
					itemResponse.setQuantity(requestedItem.getQuantityOrderd());
					itemResponse.setItemCreatedDate(item.getItemCreationDate());
					itemResponse.setItemUpdatedDate(item.getItemUpdationDate());
				//	itemResponse.setAvailbleQty(item.getQuantity());
					itemResponses.add(itemResponse);
				}
				requestReportResponse.setItemList(itemResponses);
				requestReportResponse.setRequestCreatedDate(request.getRequest_creation_date());
				requestReportResponse.setStatus(REQUESTSTATE.PENDING_APPROVAL.name());
				requestReportResponse.setEmpId(request.getEmp_id());
				requestReportResponse.setEmpEmail(request.getEmployeeEmail());
				requestReportResponse.setApprovedBy(request.getApproved_by());
				requestReportResponses.add(requestReportResponse);
			}
		}
	//	LOGGER.info("My request report response: " + requestReportResponses);
	//	return requestReportResponses;
	return requestReportResponses;
	}
	
	@Override
	public List<MyRequestReportResponse> getAllPendingRequests(String locationName, String fromDate, String toDate) {
		LOGGER.info("Entered into RequestedItemServiceImpl - getAllPendingRequests()");
		List<MyRequestReportResponse> requestReportResponses = new ArrayList<>();
		List<Request> requests = requestDao.getAllPendingRequests(locationName, fromDate, toDate);
				
		 	if (requests != null && !requests.isEmpty()) {
			for (Request request : requests) {
				MyRequestReportResponse requestReportResponse = new MyRequestReportResponse();
				requestReportResponse.setId(request.getId());
				List<RequestedItemResponse> itemResponses = new ArrayList<RequestedItemResponse>();
				Set<RequestedItems> requestedItems = request.getRequestedItems();
				for (RequestedItems requestedItem : requestedItems) {
					RequestedItemResponse itemResponse = new RequestedItemResponse();
					Item item = itemDao.getItemById(requestedItem.getItemFk().getId(), request.getLocation());
					itemResponse.setItemId(requestedItem.getItemFk().getId());
					itemResponse.setName(item.getName());
					itemResponse.setLocationName(request.getLocation());
					itemResponse.setQuantity(requestedItem.getQuantityOrderd());
					itemResponse.setItemCreatedDate(item.getItemCreationDate());
					itemResponse.setItemUpdatedDate(item.getItemUpdationDate());
				//	itemResponse.setAvailbleQty(item.getQuantity());
					itemResponses.add(itemResponse);
				}
				requestReportResponse.setItemList(itemResponses);
				requestReportResponse.setRequestCreatedDate(request.getRequest_creation_date());
				requestReportResponse.setStatus(request.getState());
				requestReportResponse.setEmpId(request.getEmp_id());
				requestReportResponse.setEmpEmail(request.getEmployeeEmail());
				requestReportResponse.setApprovedBy(request.getApproved_by());
				requestReportResponse.setEmpName(request.getEmployeeName());
				requestReportResponses.add(requestReportResponse);
			}
		}
		LOGGER.info("My request report response: " + requestReportResponses);
		return requestReportResponses;
	}


	@Override
	public List<MyRequestReportResponse> getPendingRequestByApprover(String approverName, String locationName) {
		LOGGER.info("Entered into RequestedItemServiceImpl - getPendingRequestByApprover()");
		List<MyRequestReportResponse> requestReportResponses = null;
		List<Request> requests = requestDao.getPendingRequestByApprover(approverName, locationName);
		if (requests != null && !requests.isEmpty()) {
			requestReportResponses = this.getMyRequestReportResponse(requests);
		}
		LOGGER.info("Pending request report response: "+requestReportResponses);
		return requestReportResponses;
	}
	
	public  List<MyRequestReportResponse> getMyRequestReportResponse(List<Request> requests){
		List<MyRequestReportResponse> requestReportResponses = null;
		for (Request request : requests) {
			MyRequestReportResponse requestReportResponse = new MyRequestReportResponse();
			requestReportResponse.setId(request.getId());
			List<RequestedItemResponse> itemResponses = new ArrayList<RequestedItemResponse>();
			Set<RequestedItems> requestedItems=request.getRequestedItems();
			for (RequestedItems requestedItem : requestedItems) {
				RequestedItemResponse itemResponse = new RequestedItemResponse();
				Item item = itemDao.getItemById(requestedItem.getItemFk().getId(), request.getLocation());
				itemResponse.setItemId(requestedItem.getItemFk().getId());
				itemResponse.setName(item.getName());
				itemResponse.setLocationName(request.getLocation());
				itemResponse.setQuantity(requestedItem.getQuantityOrderd());
				itemResponse.setItemCreatedDate(item.getItemCreationDate());
				itemResponse.setItemUpdatedDate(item.getItemUpdationDate());
				itemResponses.add(itemResponse);
			}
			requestReportResponse.setItemList(itemResponses);
			requestReportResponse.setRequestCreatedDate(request.getRequest_creation_date());
			requestReportResponse.setStatus(request.getState());
			requestReportResponse.setEmpId(request.getEmp_id());
			requestReportResponse.setEmpEmail(request.getEmployeeEmail());
			requestReportResponse.setApprovedBy(request.getApproved_by());
			requestReportResponses.add(requestReportResponse);
		}
		return requestReportResponses;
	}

	@Override
	public void updateRequestState(List<Request> request,String state, String locationName) {
		requestDao.updateRequestState(request,state, locationName);
	
	}

	@Override
	public void deleterequest(String requestId, String locationName) {
		requestDao.deleteRequest(requestId, locationName);
	}

	
	@Override
	public void updateRequestedItemState(List<Request> request, String locationName){
		requestDao.updateRequestedItemState(request, locationName);
	}

	@Override
	public List<RequestedItemResponse> validateItems(PlaceRequest placeRequest,String locationName) {
		
		List<RequestedItemResponse> responseList = new ArrayList<RequestedItemResponse>();
		Request request = new Request();
		List<ItemRequest> removeItemList = new ArrayList<ItemRequest>();
		List<ItemRequest>requestedItem = placeRequest.getItemDetails();
		
		for(ItemRequest itemRequest: placeRequest.getItemDetails()) {
			System.out.println(itemRequest.getName().trim().toUpperCase().replaceAll("\\s+",""));
			if(Arrays.asList(ItemEnum.values()).toString().contains(itemRequest.getName().trim().toUpperCase().replaceAll("\\s+",""))) {
				LOGGER.info("Item Found");
			   Long itemQuantity  = requestDao.getItemCount((int) placeRequest.getEmpId(), locationName, itemRequest.getId());
			   if(itemQuantity < 2  || itemQuantity.equals(0l)) {
				 return null;
			  }
			  removeItemList.add(itemRequest);
			  request =  requestDao.getLastRequestedItem(itemRequest.getId(),(int) placeRequest.getEmpId());
			  String date=new SimpleDateFormat("yyyy/MM/dd").format(request.getRequest_creation_date()) ;
			  responseList.add(new RequestedItemResponse(itemRequest.getId(), itemRequest.getName(), locationName, itemQuantity.intValue(),date,REQUESTSTATE.REJECTED));
			} else {
			    	 LOGGER.info("Continue");
			     }
				}
				
		requestedItem.removeAll(removeItemList);
		return responseList;
	}

	@Override
	public Long getItemCount(int empId, String locationName, int itemId) {
		return requestDao.getItemCount(empId, locationName, itemId);
	}

	@Override
	public Request getLastRequestedItem(int itemId, int empId) {
		return requestDao.getLastRequestedItem(itemId, empId);
	}

	@Override
	public List<OrderResponse> getAllRequestedItem(String locationName) {
		List<OrderResponse> myRequestResponse = new ArrayList<OrderResponse>();
		
		List<Request> requestedItm =  requestDao.getAllRequestedItem(locationName);
		for (Request request : requestedItm) {
			OrderResponse orderInfo = new OrderResponse();
			orderInfo.setEmpName(request.getEmployeeName());
			orderInfo.setLocationName(locationName);
			orderInfo.setOrderCreatedDate(request.getRequest_creation_date().toLocaleString());
			ArrayList<Item> itemList = new ArrayList<Item>();
			 for (RequestedItems requestItem : request.getRequestedItems()) {
				  Item item = new Item();
				  item.setName(requestItem.getItemFk().getName());
				  item.setPrice(requestItem.getItemFk().getPrice());
				  item.setQuantity(requestItem.getQuantityOrderd());
				  itemList.add(item);
			}
			orderInfo.setItemList(itemList);
			orderInfo.setStatus(request.getState());
			String empId = String.valueOf(request.getEmp_id());
			orderInfo.setEmpId(empId);
			myRequestResponse.add(orderInfo);
		}
		return myRequestResponse;
	}
	

}
