package com.gl.adminportal.stationery.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gl.adminportal.stationery.dao.ReportDao;
import com.gl.adminportal.stationery.dao.impl.ReportDaoImpl;
import com.gl.adminportal.stationery.model.Item;
import com.gl.adminportal.stationery.model.Request;
import com.gl.adminportal.stationery.model.RequestedItems;
import com.gl.adminportal.stationery.response.EmployeeRequestItemReports;
import com.gl.adminportal.stationery.response.ItemResponse;
import com.gl.adminportal.stationery.service.ReportService;

@Service("reportService")
@Transactional
public class ReportServiceImpl implements ReportService {

	private static final Logger LOGGER = Logger.getLogger(ReportDaoImpl.class);
	
	@Autowired
	private ReportDao reportDao;

	@Override
	public List<ItemResponse> geItemsListbyLocations(String locationName) {
		LOGGER.info("Request in ReportServiceImpl ==> geItemsListbyLocations() with location "+locationName);
		List<Item> itemInventoryList = reportDao.geItemsListbyLocations(locationName);
		List<ItemResponse> itemResponses = null;
		if(!itemInventoryList.isEmpty()){
			itemResponses = new ArrayList<ItemResponse>();
			for(Item item : itemInventoryList){
				ItemResponse itemResponse = new ItemResponse();
				itemResponse.setId(item.getId());
				itemResponse.setName(item.getName());
				itemResponse.setPrice(item.getPrice());
				itemResponse.setQuantity(item.getQuantity());
				itemResponse.setImageStream(item.getImage_stream());
				itemResponses.add(itemResponse);
			}
		}
		LOGGER.info("Response from ReportServiceImpl ==> geItemsListbyLocations() is "+((itemResponses!=null)?itemResponses.size():null));
		return itemResponses;
	}

	@Override
	public List<EmployeeRequestItemReports> getEmpolyeeRequestReport(String empName,
			String startDate, String endDate) {
		LOGGER.info("Request in ReportServiceImpl ==> getEmpolyeeRequestReport() with empName :: "+
			empName+", startDate :: "+startDate+" endDate :: "+endDate);
		List<EmployeeRequestItemReports> employeeRequestedItemReports = new ArrayList<EmployeeRequestItemReports>();
		try{
			List<Request> requestList = reportDao.getRequestByFilters(empName, startDate, endDate);
			if(!requestList.isEmpty()){
				for(Request request : requestList){
					EmployeeRequestItemReports requestItemReports = new EmployeeRequestItemReports();
					requestItemReports.setEmpId(request.getEmp_id());
					requestItemReports.setApprovedBy(request.getApproved_by());
					requestItemReports.setEmpName(request.getEmployeeName());
					requestItemReports.setEmpEmail(request.getEmployeeEmail());
					requestItemReports.setLocation(request.getLocation());
					requestItemReports.setRequestDate(request.getRequest_creation_date());
					if(null!=request.getRequestedItems()){
						List<EmployeeRequestItemReports.RequestedItems> requestItemList = new ArrayList<EmployeeRequestItemReports.RequestedItems>();
						for(RequestedItems itemN : request.getRequestedItems()){
							EmployeeRequestItemReports.RequestedItems requestedItems = new EmployeeRequestItemReports.RequestedItems();
							requestedItems.setItemName(itemN.getItemFk().getName());
							requestedItems.setPrice(itemN.getItemFk().getPrice());
							requestedItems.setQuantity(itemN.getItemFk().getQuantity());
							requestedItems.setImageStream(itemN.getItemFk().getImage_stream());
							requestItemList.add(requestedItems);
						}
						requestItemReports.setRequestedItems(requestItemList);
					}
					employeeRequestedItemReports.add(requestItemReports);
				}
			}
		} catch (ParseException e) {
			LOGGER.info("Parsing Exception in ReportServiceImpl ==> getEmpolyeeRequestReport(). Exception caused :"+e.getMessage());
		}
		LOGGER.info("Response from ReportServiceImpl ==> getEmpolyeeRequestReport() is "+employeeRequestedItemReports.size());
		return employeeRequestedItemReports;
	}
}