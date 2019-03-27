package com.gl.adminportal.stationery.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gl.adminportal.stationery.exception.SystemException;
import com.gl.adminportal.stationery.model.Request;
import com.gl.adminportal.stationery.request.PlaceRequest;
import com.gl.adminportal.stationery.response.ErrorMessage;
import com.gl.adminportal.stationery.response.MyRequestReportResponse;
import com.gl.adminportal.stationery.response.RequestedItemResponse;
import com.gl.adminportal.stationery.service.RequestService;
import com.gl.adminportal.stationery.utils.Constants;
import com.gl.adminportal.stationery.utils.ValidationUtils;

@RestController
@CrossOrigin(origins = "*")
public class RequestController {
	private static final Logger LOGGER = Logger
			.getLogger(RequestController.class);

	@Autowired
	private ValidationUtils validationUtils;
	@Autowired
	private RequestService requestService;

	@RequestMapping(value = "/v1/placeRequest", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> placeRequest(
			@RequestBody PlaceRequest placeRequest,
			@RequestHeader("x-location-name") String locationName)
			throws SystemException {

		LOGGER.info("[PLACE-REQUEST] Going to place Request from "
				+ placeRequest.getEmpName());
		List<RequestedItemResponse> requestedItemResponses = new ArrayList<RequestedItemResponse>();
		try {
			List<RequestedItemResponse> rejectedRequestedItem =  requestService.validateItems(placeRequest,locationName);
	        if(!rejectedRequestedItem.isEmpty()) {
	        	requestedItemResponses.addAll(rejectedRequestedItem);
	        }
		}catch(Exception e) {
			LOGGER.error("Fail to check request"+ e.getMessage());
		}
		try {
			if(!placeRequest.getItemDetails().isEmpty()) {
				List<RequestedItemResponse> response = requestService.placeRequest(placeRequest,
						locationName);
				requestedItemResponses.addAll(response);
			}
			return new ResponseEntity<>(requestedItemResponses,HttpStatus.CREATED);

		} catch (Exception e) {
			LOGGER.error("Exception occured while creating request Object :: "
					+ e);
			return new ResponseEntity<ErrorMessage>(
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/v1/myrequests/{empId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<MyRequestReportResponse>> myRequest(
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@PathVariable("empId") String empId,
			@RequestHeader("x-location-name") String locationName)
			throws SystemException {
		LOGGER.info("Invoking myRequest report api...");
		LOGGER.info("Entered empId :" + empId);
		LOGGER.info("Entered locationName :" + locationName);
		ErrorMessage errMsg = new ErrorMessage();
		List<MyRequestReportResponse> myRequestReportResponses = null;

		// validate location name
		if (!validationUtils.isValidateLocation(locationName, errMsg)) {
			throw new SystemException(errMsg.getErrCode(), errMsg.getErrMsg(),
					HttpStatus.BAD_REQUEST);
		}

		// validating dates if passed in request
		if (!validationUtils.validateDates(fromDate, toDate, errMsg)) {
			throw new SystemException(errMsg.getErrCode(), errMsg.getErrMsg(),
					HttpStatus.BAD_REQUEST);
		}

		try {

			myRequestReportResponses = requestService.getRequestedItemByEmpId(
					empId, locationName, fromDate, toDate);
		} catch (Exception e) {
			LOGGER.error("Error getting while processing request to get requestedItem from inventory "
					+ e);
			throw new SystemException(Constants.FIVE_THOUSAND_SEVEN,
					Constants.INTERNAL_SERVER_ERROR,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<MyRequestReportResponse>>(
				myRequestReportResponses, HttpStatus.ACCEPTED);
	}

	@RequestMapping(value = "/v1/getPendingRequest/{approverName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<MyRequestReportResponse>> getPendingRequest(
			@PathVariable("approverName") String approverName,
			@RequestHeader("x-location-name") String locationName)
			throws SystemException {
		LOGGER.info("Invoking pending request report api...");
		LOGGER.info("Entered approverName :" + approverName);
		LOGGER.info("Entered locationName :" + locationName);
		ErrorMessage errMsg = new ErrorMessage();
		List<MyRequestReportResponse> myRequestReportResponses = null;

		// validate location name
		if (!validationUtils.isValidateLocation(locationName, errMsg)) {
			throw new SystemException(errMsg.getErrCode(), errMsg.getErrMsg(),
					HttpStatus.BAD_REQUEST);
		}

		try {

			myRequestReportResponses = requestService
					.getPendingRequestByApprover(approverName, locationName);
		} catch (Exception e) {
			LOGGER.error("Error getting while processing request to get pending request from inventory "
					+ e);
			throw new SystemException(Constants.FIVE_THOUSAND_SEVEN,
					Constants.INTERNAL_SERVER_ERROR,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<MyRequestReportResponse>>(
				myRequestReportResponses, HttpStatus.ACCEPTED);
	}

	/**
	 * Update request state in inventory
	 * 
	 * @param requests
	 * @param locationName
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value = "/v1/updateRequestState/{state}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateApproveRequest(
			@RequestBody List<Request> requests,
			@PathVariable("state") String state,
			@RequestHeader("x-location-name") String locationName)
			throws SystemException {

		ErrorMessage errMsg = new ErrorMessage();
		LOGGER.info("Entered the RequestController - updateApproveRequest()");
		try {
			// validate Location
			if (!validationUtils.isValidateLocation(locationName, errMsg)) {
				throw new SystemException(errMsg.getErrCode(),
						errMsg.getErrMsg(), HttpStatus.BAD_REQUEST);
			}

			// validate state
			if (!validationUtils.isValidateState(state, errMsg)) {
				throw new SystemException(errMsg.getErrCode(),
						errMsg.getErrMsg(), HttpStatus.BAD_REQUEST);
			}

			// validate update item request
			if (!validationUtils.validateUpdateRequestState(requests,
					locationName, errMsg)) {
				throw new SystemException(errMsg.getErrCode(),
						errMsg.getErrMsg(), HttpStatus.BAD_REQUEST);
			}

			requestService.updateRequestState(requests, state, locationName);

		} catch (SystemException e) {
			LOGGER.error("Error occured while updating request state: ", e);
			return new ResponseEntity<ErrorMessage>(new ErrorMessage(
					e.getErrorCode(), e.getErrorMessage()), e.getHttpStatus());
		} catch (Exception e) {
			LOGGER.error("Error occured while updating request state: ", e);
			return new ResponseEntity<ErrorMessage>(new ErrorMessage(
					Constants.FIVE_THOUSAND_SEVEN,
					Constants.INTERNAL_SERVER_ERROR),
					HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}

	@RequestMapping(value = "/v1/deleteRequest/{requestId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteRequest(
			@RequestHeader("x-location-name") String locationName,
			@PathVariable("requestId") String requestId) throws SystemException {
		LOGGER.info("In RequestController - deleteRequest()");

		ErrorMessage errMsg = new ErrorMessage();
		// validate location
		if (!validationUtils.isValidateLocation(locationName, errMsg)) {
			throw new SystemException(errMsg.getErrCode(), errMsg.getErrMsg(),
					HttpStatus.BAD_REQUEST);
		}

		if (!validationUtils.isValidRequestId(requestId, locationName)) {
			throw new SystemException(Constants.FIVE_THOUSAND_FOURTEEN,
					Constants.INVALID_REQUEST_ID, HttpStatus.BAD_REQUEST);
		}

		try {
			requestService.deleterequest(requestId, locationName);
		} catch (Exception e) {
			LOGGER.error("Exception deleting record from DB " + e);
			throw new SystemException(Constants.FIVE_THOUSAND_SEVEN,
					Constants.INTERNAL_SERVER_ERROR,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return null;

	}

	/**
	 * Exception handler
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler(SystemException.class)
	public ResponseEntity<ErrorMessage> handle(SystemException e) {
		ErrorMessage errResponse = new ErrorMessage();
		errResponse.setErrCode(e.getErrorCode());
		errResponse.setErrMsg(e.getErrorMessage());
		return new ResponseEntity<ErrorMessage>(new ErrorMessage(
				e.getErrorCode(), e.getErrorMessage()), e.getHttpStatus());
	}

	/**
	 * Get All pending requests
	 * 
	 * @param requests
	 * @param locationName
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value = "/v1/getAllPendingRequests", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<MyRequestReportResponse>> getAllPendingRequests(
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestHeader("x-location-name") String locationName)
			throws SystemException {
		LOGGER.info("Invoking getAllPendingRequests api...");
		LOGGER.info("Entered locationName :" + locationName);
		ErrorMessage errMsg = new ErrorMessage();
		List<MyRequestReportResponse> myRequestReportResponses = null;

		// validate location name
		if (!validationUtils.isValidateLocation(locationName, errMsg)) {
			throw new SystemException(errMsg.getErrCode(), errMsg.getErrMsg(),
					HttpStatus.BAD_REQUEST);
		}

		// validating dates if passed in request
		if (!validationUtils.validateDates(fromDate, toDate, errMsg)) {
			throw new SystemException(errMsg.getErrCode(), errMsg.getErrMsg(),
					HttpStatus.BAD_REQUEST);
		}

		try {

			myRequestReportResponses = requestService.getAllPendingRequests(
					locationName, fromDate, toDate);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error getting while processing request to get allPendingRequests from inventory "
					+ e);
			throw new SystemException(Constants.FIVE_THOUSAND_SEVEN,
					Constants.INTERNAL_SERVER_ERROR,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<MyRequestReportResponse>>(
				myRequestReportResponses, HttpStatus.ACCEPTED);
	}

	/**
	 * Update request state from Created/Approved to delivered in
	 * inventory(Support role/admin role)
	 * 
	 * @param requests
	 * @param locationName
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value = "/v1/updateRequestedItemState", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateRequestedItemState(
			@RequestBody List<Request> requests,
			@RequestHeader("x-location-name") String locationName)
			throws SystemException {

		ErrorMessage errMsg = new ErrorMessage();
		LOGGER.info("Entered the RequestController - updateApproveRequest()");
		try {
			// validate Location
			if (!validationUtils.isValidateLocation(locationName, errMsg)) {
				throw new SystemException(errMsg.getErrCode(),
						errMsg.getErrMsg(), HttpStatus.BAD_REQUEST);
			}

			// validate update item request
			/*
			 * if (!validationUtils.validateRequestDeliveredState(requests,
			 * locationName, errMsg)) { throw new
			 * SystemException(errMsg.getErrCode(), errMsg.getErrMsg(),
			 * HttpStatus.BAD_REQUEST); }
			 */

			requestService.updateRequestedItemState(requests, locationName);

		} catch (SystemException e) {
			LOGGER.error("Error occured while updating request state: ", e);
			return new ResponseEntity<ErrorMessage>(new ErrorMessage(
					e.getErrorCode(), e.getErrorMessage()), e.getHttpStatus());
		} catch (Exception e) {
			LOGGER.error("Error occured while updating request state: ", e);
			return new ResponseEntity<ErrorMessage>(new ErrorMessage(
					Constants.FIVE_THOUSAND_SEVEN,
					Constants.INTERNAL_SERVER_ERROR),
					HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
}
