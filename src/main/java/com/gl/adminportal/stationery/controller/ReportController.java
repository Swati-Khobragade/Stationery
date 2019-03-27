package com.gl.adminportal.stationery.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gl.adminportal.stationery.exception.SystemException;
import com.gl.adminportal.stationery.response.EmployeeRequestItemReports;
import com.gl.adminportal.stationery.response.ItemResponse;
import com.gl.adminportal.stationery.service.ReportService;
import com.gl.adminportal.stationery.utils.Constants;

@RestController
@CrossOrigin(origins = "*")
public class ReportController {

	private static final Logger LOGGER = Logger
			.getLogger(ReportController.class);

	@Autowired
	private ReportService reportService;

	@RequestMapping(value = "/v1/itemInventoryReport", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ItemResponse>> inventoryReportList(@RequestHeader("x-location-name") String locationName)
			throws SystemException {
		LOGGER.info("Invoking ItemReport api...");

		List<ItemResponse> itemReportList = new ArrayList<ItemResponse>();
		try {
			itemReportList = reportService
					.geItemsListbyLocations(locationName);
			
				LOGGER.info("Item List size :"
						+ itemReportList.size());
		} catch (Exception e) {
			LOGGER.error("Exception occured while fetching employee orderes ",
					e);
				throw new SystemException(Constants.DEFAULT_DOMAIN,
						Constants.INTERNAL_SERVER_ERROR,
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		LOGGER.info("Report data for item inventory fetched successfully!");
		return new ResponseEntity<List<ItemResponse>>(itemReportList,
				HttpStatus.OK);
	}

	/**
	 * Fetch Requested Items for specific employee or for All. 
	 * Provide start date and end date for fetching request for specific date range
	 * @param empName mandatory
	 * @param startDate non-mandatory
	 * @param endDate non-mandatory
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value = "/v1/employeeRequestReport", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<EmployeeRequestItemReports>> getEmpolyeeRequestReport(@RequestParam(value="empname", required=false) String empName, 
			@RequestParam(value="startdate", required=false) String startDate, @RequestParam(value="enddate", required=false) String endDate)
			throws SystemException {
		LOGGER.info("Invoking Employee Request Report api...");
		List<EmployeeRequestItemReports> employeeRequestList = new ArrayList<EmployeeRequestItemReports>();
		try {
			employeeRequestList = reportService.getEmpolyeeRequestReport(empName, startDate, endDate);
			LOGGER.info("Employee Request Item List size : "+ employeeRequestList.size());
		} catch (Exception e) {
			LOGGER.error("Exception occured while fetching employee request list ",
					e);
				throw new SystemException(Constants.DEFAULT_DOMAIN,
						Constants.INTERNAL_SERVER_ERROR,
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		LOGGER.info("Report data for item inventory fetched successfully!");
		return new ResponseEntity<List<EmployeeRequestItemReports>>(employeeRequestList, HttpStatus.OK);
	}
	
}
