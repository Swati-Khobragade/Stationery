package com.gl.adminportal.stationery.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gl.adminportal.stationery.exception.SystemException;
import com.gl.adminportal.stationery.model.EmpFeedback;
import com.gl.adminportal.stationery.response.EmpFeedbackResponse;
import com.gl.adminportal.stationery.response.EmployeeRequestItemReports;
import com.gl.adminportal.stationery.response.ErrorMessage;
import com.gl.adminportal.stationery.service.EmailService;
import com.gl.adminportal.stationery.service.EmpFeedbackService;
import com.gl.adminportal.stationery.utils.Constants;
import com.gl.adminportal.stationery.utils.ValidationUtils;


@RestController
@CrossOrigin(origins = "*")
public class FeedBackController {
	private static final Logger LOGGER = Logger.getLogger(FeedBackController.class);

	@Autowired
	EmpFeedbackService empFeedbackService;

	@Autowired
	EmailService emailService;

	@Autowired
	ValidationUtils validationUtils;

	/**
	 * save the feedback given by employee in database
	 * 
	 * @param empFeedback
	 * @param locationName
	 * @throws SystemException
	 */
	@RequestMapping(value = "/v1/empFeedback/{empId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ErrorMessage> empFeedback(
			@RequestBody EmpFeedback empFeedback,
			@PathVariable("empId") String empId,
			@RequestHeader("x-location-name") String locationName)
			throws SystemException {
		LOGGER.info("Inside Employee feedback.. feedback received from employee is "+ empFeedback.getFeedback());
		ErrorMessage errMsg = new ErrorMessage();
		
		// validate Location
		if (!validationUtils.isValidateLocation(locationName, errMsg)) {
			throw new SystemException(errMsg.getErrCode(), errMsg.getErrMsg(),
					HttpStatus.BAD_REQUEST);
		}
		
		empFeedback.setEmpId(empId);
		try {
			int feedbackId = empFeedbackService.saveFeedback(empFeedback, locationName);
			System.out.println(" feedbackId "+feedbackId);
		} catch (Exception e) {
			LOGGER.error("Error occured while saving feedback : ", e);
			return new ResponseEntity<ErrorMessage>(new ErrorMessage(
					Constants.FIVE_THOUSAND_SEVEN,
					Constants.INTERNAL_SERVER_ERROR),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		final EmpFeedback empFeedbackfinal = empFeedback;
		Thread thread = new Thread("mailThread") {
			public void run() {
				LOGGER.info("Making async call to send mail ");
				emailService.sendFeedbackNotifiction(empFeedbackfinal);
			}
		};
		thread.start();
		
		return new ResponseEntity<ErrorMessage>(HttpStatus.CREATED);
	}

	/**
	 * Get employee feedback from DB based on empId
	 * 
	 * @param empId
	 * @param locationName
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value = "/v1/viewFeedbackByEmpId/{empId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<EmpFeedback>> viewFeedbackByEmpId(
			@PathVariable(value = "empId") String empId,
			@RequestHeader("x-location-name") String locationName)
			throws SystemException {
		ErrorMessage errMsg = new ErrorMessage();
		List<EmpFeedback> empFeedbacks = new ArrayList<EmpFeedback>();
		LOGGER.info("Inside Employee feedback.. - viewFeedbackByEmpId()");
		try{
		// validate location name passed in request
		if (!validationUtils.isValidateLocation(locationName, errMsg)) {
			throw new SystemException(errMsg.getErrCode(), errMsg.getErrMsg(),
					HttpStatus.BAD_REQUEST);
		}
			empFeedbacks = empFeedbackService.getEmpFeedbackByEmpId(locationName, empId);
		} catch (Exception e) {
			LOGGER.error("Exception while fetching employee feedbacks by employee id" + e);
			throw new SystemException(Constants.FIVE_THOUSAND_SEVEN,
					Constants.INTERNAL_SERVER_ERROR,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<EmpFeedback>>(empFeedbacks, HttpStatus.ACCEPTED);
	}
	
	/**
	 * Get employee feedback from DB based on the feedback status
	 * 
	 * @param status
	 * @param locationName
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value = "/v1/viewEmpFeedbackByStatus/{status}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<EmpFeedback>> viewEmpFeedbackByStatus(
			@PathVariable(value = "status") String status,
			@RequestHeader("x-location-name") String locationName)
			throws SystemException {
		ErrorMessage errMsg = new ErrorMessage();
		List<EmpFeedback> empFeedbacks = new ArrayList<EmpFeedback>();
		LOGGER.info("Inside Employee feedback.. - viewEmpFeedbackByStatus() ");
		try{
		// validate location name passed in request
		if (!validationUtils.isValidateLocation(locationName, errMsg)) {
			throw new SystemException(errMsg.getErrCode(), errMsg.getErrMsg(),
					HttpStatus.BAD_REQUEST);
		}
			empFeedbacks = empFeedbackService.getEmpFeedbackByStatus(locationName, status);
		} catch (Exception e) {
			LOGGER.error("Exception while fetching employee feedbacks by status" + e);
			throw new SystemException(Constants.FIVE_THOUSAND_SEVEN,
					Constants.INTERNAL_SERVER_ERROR,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<EmpFeedback>>(empFeedbacks, HttpStatus.ACCEPTED);
	}
	
	/**
	 * Get employee feedback from DB based on the feedback id
	 * 
	 * @param id
	 * @param locationName
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value = "/v1/viewEmpFeedbackById/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EmpFeedback> viewEmpFeedbackById(
			@PathVariable(value = "id") String id,
			@RequestHeader("x-location-name") String locationName)
			throws SystemException {
		ErrorMessage errMsg = new ErrorMessage();
		EmpFeedback empFeedback = null;
		LOGGER.info("Inside Employee feedback.. - viewEmpFeedbackById() ");

		// validate location name passed in request
		if (!validationUtils.isValidateLocation(locationName, errMsg)) {
			throw new SystemException(errMsg.getErrCode(), errMsg.getErrMsg(),
					HttpStatus.BAD_REQUEST);
		}
		try {
			empFeedback = empFeedbackService.getEmpFeedbackById(locationName,id);
			if(empFeedback != null){
				LOGGER.info(" Status of feedback " + empFeedback.getStatus());
			}
		} catch (Exception e) {
			LOGGER.error("Exception fetching employee feedback from inventory", e);
			if (e instanceof NullPointerException) {
				throw new SystemException(Constants.FIVE_THOUSAND_EIGHT,
						Constants.ITEM_DOES_NOT_EXIXTS, HttpStatus.NOT_FOUND);
			} else {
				throw new SystemException(Constants.FIVE_THOUSAND_SEVEN,
						Constants.INTERNAL_SERVER_ERROR,
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		};
		return new ResponseEntity<EmpFeedback>(empFeedback,HttpStatus.ACCEPTED);
	}
	
	/**
	 * Update employee feedback status
	 * 
	 * @param itemRequest
	 * @param locationName
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value = "/v1/changeFeedbackStatus/{id}/{status}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ErrorMessage> changeFeedbackStatus(
			@PathVariable(value = "id") String id,
			@PathVariable(value = "status") String status,
			@RequestHeader("x-location-name") String locationName)
			throws SystemException {

		ErrorMessage errMsg = new ErrorMessage();
		LOGGER.info("Inside Employee feedback.. - changeFeedbackStatus() ");

		// validate Location
		if (!validationUtils.isValidateLocation(locationName, errMsg)) {
			throw new SystemException(errMsg.getErrCode(), errMsg.getErrMsg(),
					HttpStatus.BAD_REQUEST);
		}
		try {
			empFeedbackService.changeFeedbackStatus(locationName, id, status);
		} catch (Exception e) {
			LOGGER.error("Error occured while updating feedback status: ", e);
			return new ResponseEntity<ErrorMessage>(new ErrorMessage(
					Constants.FIVE_THOUSAND_SEVEN,
					Constants.INTERNAL_SERVER_ERROR),
					HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return new ResponseEntity<ErrorMessage>(HttpStatus.ACCEPTED);
	}
	
	/**
	 * gets the feedback of employee to be displayed on the admin screen
	 * 
	 * @param locationName
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value = "/v1/viewEmpFeedback", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EmpFeedbackResponse> getFeedbackByEmp(
			@RequestHeader("x-location-name") String locationName,
			@RequestParam(value = "pageNo", required = false) Integer pageNo,
			@RequestParam(value = "status", required = false) String status)
			throws SystemException {
		LOGGER.info("Entered into get empFeedback ");

		List<EmpFeedback> empFeedback =  new ArrayList<EmpFeedback>();
		//List<EmpFeedbackResponse> empFeedbackrespList = new ArrayList<>();
		ErrorMessage errMsg = new ErrorMessage();
		LOGGER.info("Inside Employee feedback.. - changeFeedbackStatus() ");

		// validate Location
		if (!validationUtils.isValidateLocation(locationName, errMsg)) {
			throw new SystemException(errMsg.getErrCode(), errMsg.getErrMsg(),
					HttpStatus.BAD_REQUEST);
		}

		EmpFeedbackResponse empFeedbackResp = new EmpFeedbackResponse();
		pageNo = null == pageNo ? 1 : pageNo;
		try {
			empFeedback = empFeedbackService.getEmpFeedback(locationName,
					pageNo, status);
			
			empFeedbackResp.setEmpFeedback(empFeedback);
			
			/*
			 * for (EmpFeedback empFeed : empFeedback) {
			 * empFeedbackResp.setEmpFeedback(empFeedback); }
			 * 
			 * empFeedbackResp.setCount(empFeedbackDao.getCollectionSize( locationName,
			 * null)); empFeedbackResp.setEmpFeedback(empFeedbackrespList);
			 */

		} catch (Exception e) {
			LOGGER.error("Exception occurred while updating the items ", e);
			/*
			 * throw new SystemException(Constants.FIVETHOUSANDSEVEN,
			 * Constants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
			 */
		}

		if (empFeedback != null && !empFeedback.isEmpty()) {
			LOGGER.info("fetchinf feedback done successfully");
			return new ResponseEntity<>(empFeedbackResp, HttpStatus.OK);
		} else {
			LOGGER.error("No feedback avilable !");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

}
