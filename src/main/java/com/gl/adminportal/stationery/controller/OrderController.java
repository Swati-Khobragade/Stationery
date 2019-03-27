package com.gl.adminportal.stationery.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gl.adminportal.stationery.exception.SystemException;
import com.gl.adminportal.stationery.model.Request;
import com.gl.adminportal.stationery.response.ErrorMessage;
import com.gl.adminportal.stationery.response.MyRequestReportResponse;
import com.gl.adminportal.stationery.response.OrderResponse;
import com.gl.adminportal.stationery.service.RequestService;
import com.gl.adminportal.stationery.utils.ValidationUtils;

/**
 * 
 * @author ragini.jain
 *
 */

@RestController
public class OrderController {
	private static final Logger LOGGER = Logger.getLogger(OrderController.class);

	@Autowired
	private ValidationUtils validationUtils;
	
	@Autowired
	private RequestService requestService;
	
	@RequestMapping(value = "/v1/viewOrders", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<OrderResponse>> viewOrders(
			@RequestHeader("x-location-name") String locationName)
			throws SystemException {
		LOGGER.info("Invoking viewOrders api...");
		LOGGER.info("Entered locationName :" + locationName);
		ErrorMessage errMsg = new ErrorMessage();
		if (!validationUtils.isValidateLocation(locationName, errMsg)) {
			throw new SystemException(errMsg.getErrCode(), errMsg.getErrMsg(),
					HttpStatus.BAD_REQUEST);
		}
		List<OrderResponse> requestedItem = new ArrayList<OrderResponse>();
		try {
			requestedItem = requestService.getAllRequestedItem(locationName);
		} catch (Exception e) {
			LOGGER.error("Exception occurred while invoking viewOrder api", e);
		}
		LOGGER.info("Order with 'ORDERED' state fetched successfully!");
		return new ResponseEntity<>(requestedItem,
				HttpStatus.OK);
	}
}
