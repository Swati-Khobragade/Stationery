package com.gl.adminportal.stationery.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gl.adminportal.stationery.exception.SystemException;
import com.gl.adminportal.stationery.request.ResetPasswordRequest;
import com.gl.adminportal.stationery.response.ErrorMessage;
import com.gl.adminportal.stationery.service.PasswordService;
import com.gl.adminportal.stationery.utils.Constants;
import com.gl.adminportal.stationery.utils.ValidationUtils;

@RestController
@CrossOrigin(origins = "*")
public class PasswordResetController {

	private static final Logger LOGGER = Logger
			.getLogger(PasswordResetController.class);

	@Autowired
	private ValidationUtils validationUtil;

	/*
	 * @Autowired private ErrorMessage errMsg;
	 */

	@Autowired
	private PasswordService resetPasswordService;

	@RequestMapping(value = "/v1/resetPassword", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ErrorMessage> resetPassword(
			@RequestHeader("x-location-name") String locationName,
			@RequestBody ResetPasswordRequest resetPasswordReq)
			throws SystemException {
		LOGGER.info("Inside resetPassword for role "
				+ resetPasswordReq.getRole());
		ErrorMessage errMsg = new ErrorMessage();

		if (!validationUtil.isValidateLocation(locationName, errMsg)) {
			throw new SystemException(errMsg.getErrCode(), errMsg.getErrMsg(),
					HttpStatus.BAD_REQUEST);
		}

		if (!validationUtil.ValidatepswdRequest(resetPasswordReq, errMsg,
				locationName)) {
			throw new SystemException(errMsg.getErrCode(), errMsg.getErrMsg(),
					HttpStatus.BAD_REQUEST);
		}

		try {
			resetPasswordService.resetPassword(resetPasswordReq, locationName);
			LOGGER.info("Password reset completed successfully");
		} catch (Exception e) {
			LOGGER.error("Error occured while adding item: ", e);
			return new ResponseEntity<ErrorMessage>(new ErrorMessage(
					Constants.FIVE_THOUSAND_SEVEN,
					Constants.INTERNAL_SERVER_ERROR),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(HttpStatus.OK);

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
}
