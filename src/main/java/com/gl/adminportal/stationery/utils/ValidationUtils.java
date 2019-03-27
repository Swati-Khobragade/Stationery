package com.gl.adminportal.stationery.utils;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gl.adminportal.stationery.dao.ItemDao;
import com.gl.adminportal.stationery.dao.PasswordDao;
import com.gl.adminportal.stationery.dao.RequestDao;
import com.gl.adminportal.stationery.enums.LOCATION;
import com.gl.adminportal.stationery.enums.REQUESTSTATE;
import com.gl.adminportal.stationery.model.AdminSupportPassword;
import com.gl.adminportal.stationery.model.Item;
import com.gl.adminportal.stationery.model.Request;
import com.gl.adminportal.stationery.request.ItemRequest;
import com.gl.adminportal.stationery.request.ResetPasswordRequest;
import com.gl.adminportal.stationery.response.ErrorMessage;

@Component
public class ValidationUtils {

	private static final Logger LOGGER = Logger
			.getLogger(ValidationUtils.class);

	@Autowired
	private ItemDao itemDao;

	@Autowired
	private RequestDao requestDao;
	
	@Autowired
	private PasswordDao pswdResetdao;

	public boolean validateItemRequest(List<ItemRequest> itemRequest,
			String locationName, ErrorMessage errMsg) {
		LOGGER.info("Validating the add item request ");
		// check for mandatory parameters
		for (ItemRequest itemReq : itemRequest) {
			// validate missing parameters
			if (null == itemReq.getName()) {
				LOGGER.info("Mandatory parameter name missing");
				errMsg.setErrCode(Constants.FIVE_THOUSAND_ONE);
				errMsg.setErrMsg(Constants.MISSING_PARAMETER);
				return false;
			}

			// validate parameter dateTypes
			String itemName = itemReq.getName();
			itemName = itemName.replace("-", " ");
			if (!validateItemForSpecialCharacters(itemName)) {
				LOGGER.error("Invalid item name provided.");
				errMsg.setErrMsg(Constants.INVALID_PARAMETER);
				errMsg.setErrCode(Constants.FIVETHOUSANDTWO);
				return false;
			}

			// validate for duplicate item entries
			if (isItemDuplicate(itemReq, locationName)) {
				LOGGER.info("Dulipcate item entry");
				errMsg.setErrCode(Constants.FIVE_THOUSAND_SIX);
				errMsg.setErrMsg(Constants.ITEM_ALREADY_EXISTS);
				return false;
			}
		}
		return true;
	}

	/**
	 * This method validated the request for update item request
	 * 
	 * @param itemReq
	 * @param location
	 * @return
	 */
	public boolean validateUpdateItemReq(List<ItemRequest> itemReq,
			String location, ErrorMessage errMsg) {

		for (ItemRequest itemRequest : itemReq) {
			// comparing with zero, when no id is padded for an item its id will
			// be zero
			if (itemRequest.getId() == 0) {
				LOGGER.info("Mandatory parameter if missing");
				errMsg.setErrCode(Constants.FIVE_THOUSAND_ONE);
				errMsg.setErrMsg(Constants.MISSING_PARAMETER);
				return false;
			}

			// comparing with zero, when no quantity is padded for an item its
			// quantity in request will
			// be zero, and an item cannot be updated with quantiy zero
			if (itemRequest.getQuantity() == 0) {
				LOGGER.info("Mandatory parameter if missing");
				errMsg.setErrCode(Constants.FIVE_THOUSAND_ONE);
				errMsg.setErrMsg(Constants.MISSING_PARAMETER);
				return false;
			}

			// check if the id providedin request for item upadtion exists in
			// database
			Item item = itemDao.getItemById(itemRequest.getId(), location);
			if (null == item) {
				LOGGER.info("Item with id specified does not exists");
				errMsg.setErrCode(Constants.FIVE_THOUSAND_EIGHT);
				errMsg.setErrMsg(Constants.ITEM_DOES_NOT_EXIXTS);
				return false;
			}

		}
		return true;
	}

	/**
	 * This method validated the location name, it should be either of the four
	 * values
	 * 
	 * @param locationName
	 * @param errMsg
	 * @return
	 */
	public boolean isValidateLocation(String locationName, ErrorMessage errMsg) {
		// validate location
		if (!locationName.equals(LOCATION.NAGPUR.name())
				&& !locationName.equals(LOCATION.NOIDA.name())
				&& !locationName.equals(LOCATION.BANGLORE.name())
				&& !locationName.equals(LOCATION.HYDERABAD.name())) {
			LOGGER.info("Invalid location");
			errMsg.setErrCode(Constants.FIVE_THOUSAND_THREE);
			errMsg.setErrMsg(Constants.INVALID_LOCATION);
			return false;
		}
		return true;
	}

	/**
	 * This methd validates the item name for special characters
	 * 
	 * @param itemName
	 * @return
	 */
	public boolean validateItemForSpecialCharacters(String itemName) {
		Pattern pattern = Pattern
				.compile("[\\^$#&%\\+{}:\\.\\',~@\"a-zA-Z0-9 ]+$");
		Matcher matcher = pattern.matcher(itemName);
		boolean matches = matcher.matches();
		return matches;
	}

	/**
	 * This method validates the delete item request
	 * 
	 * @param itemREqList
	 * @return
	 */
	public boolean validateDeleteItemReq(List<ItemRequest> itemREqList,
			String location, ErrorMessage errMsg) {
		for (ItemRequest itemReq : itemREqList) {
			if (itemReq.getId() == 0) {
				errMsg.setErrCode(Constants.FIVE_THOUSAND_ONE);
				errMsg.setErrMsg(Constants.MISSING_PARAMETER);
				return false;
			}
			Item item = null;
			item = itemDao.getItemById(itemReq.getId(), location);

			if (item == null) {
				errMsg.setErrCode(Constants.FIVE_THOUSAND_EIGHT);
				errMsg.setErrMsg(Constants.ITEM_DOES_NOT_EXIXTS);
				return false;
			}
		}
		return true;
	}

	/**
	 * checks if the item alreay exists in DB
	 * 
	 * @param itemReq
	 * @param locationName
	 * @return
	 */
	public boolean isItemDuplicate(ItemRequest itemReq, String locationName) {
		List<Item> itemList = itemDao.getItemByNameAndLocation(itemReq,
				locationName);
		if (itemList != null) {
			return itemList.size() == 0 ? false : true;
		} else {
			return false;
		}

	}

	public Boolean validateDates(String startDate, String endDate,
			ErrorMessage errMsg) {
		if (null != endDate && null == startDate) {
			errMsg.setErrCode(Constants.FIVE_THOUSAND_TWELVE);
			errMsg.setErrMsg(Constants.START_DATE_REQUIRED);
			return false;
		}
		if (null != startDate) {
			Date sDate;
			try {
				sDate = DateUtils.getDateFromString(startDate);
				if (sDate.after(DateUtils.getCurrentDate())) {
					errMsg.setErrCode(Constants.FIVE_THOUSAND_FOUR);
					errMsg.setErrMsg(Constants.INVALID_START_DATE);
					return false;
				}
			} catch (ParseException e) {
				LOGGER.error("Error parsing date : " + e);
			}

		}

		if (null != endDate) {
			Date eDate;
			try {
				eDate = DateUtils.getDateFromString(endDate);
				if (eDate.before(DateUtils.getCurrentDate())) {
					errMsg.setErrCode(Constants.FIVE_THOUSAND_FIVE);
					errMsg.setErrMsg(Constants.INVALID_END_DATE);
					return false;
				}
			} catch (ParseException e) {
				LOGGER.error("Exception parsing date " + e);
			}

		}
		return true;
	}

	/**
	 * This method validated the request for update request state
	 * 
	 * @param request
	 * @param location
	 * @param errMsg
	 * @return
	 */
	public boolean validateUpdateRequestState(List<Request> request,
			String location, ErrorMessage errMsg) {

		for (Request req : request) {
			// checking request id must not be zero
			if (req.getId() == 0) {
				LOGGER.info("Mandatory parameter if missing");
				errMsg.setErrCode(Constants.FIVE_THOUSAND_ONE);
				errMsg.setErrMsg(Constants.MISSING_PARAMETER);
				return false;
			}

			// Checking if the approver name must not be null or empty
			if (req.getApproved_by() == null) {
				LOGGER.info("Mandatory parameter if missing");
				errMsg.setErrCode(Constants.FIVE_THOUSAND_ONE);
				errMsg.setErrMsg(Constants.MISSING_PARAMETER);
				return false;
			}

			// check if the request id and approver name provided in request for
			// request upadtion exists in
			// database
			Request reqObj = requestDao.getRequestByReqIdAndApprover(
					req.getId(), req.getApproved_by(), location);
			if (null == reqObj) {
				LOGGER.info("Request with request Id and Approver name specified does not exists");
				errMsg.setErrCode(Constants.FIVE_THOUSAND_EIGHT);
				errMsg.setErrMsg(Constants.REQUEST_DOES_NOT_EXIXTS);
				return false;
			} else if (!REQUESTSTATE.PENDING_APPROVAL.name().equals(
					reqObj.getState())) {
				LOGGER.info("Request with request Id and Approver name specified does not exists with states PENDING_APPROVAL");
				errMsg.setErrCode(Constants.FIVE_THOUSAND_EIGHT);
				errMsg.setErrMsg(Constants.REQUEST_DOES_NOT_EXIXTS_IN_PENDING_STATE);
				return false;
			}

		}
		return true;
	}

	/**
	 * This method validated the request for reject or approve
	 * 
	 * @param state
	 * @param errMsg
	 * @return
	 */

	public boolean isValidateState(String state, ErrorMessage errMsg) {
		// validate state
		if (!state.equals("approve") && !state.equals("reject")) {
			LOGGER.info("Invalid state");
			errMsg.setErrCode(Constants.FIVE_THOUSAND_THREE);
			errMsg.setErrMsg(Constants.INVALID_REQUEST_STATE);
			return false;
		}
		return true;
	}

	/**
	 * This method will validate if the given request ID exists in Database
	 * 
	 * @param requestId
	 * @param locationName
	 * @return
	 */
	public boolean isValidRequestId(String requestId, String locationName) {
		LOGGER.info("validating request Id " + requestId);
		Request request = requestDao.getRequestById(requestId, locationName);
		return request== null ? false : true;
	}
	
	/**
	 * This method validated the request for update request state
	 * 
	 * @param request
	 * @param location
	 * @param errMsg
	 * @return
	 */
	public boolean validateRequestDeliveredState(List<Request> request,
			String location, ErrorMessage errMsg) {

		for (Request req : request) {
			//checking request id must not be zero 
			if (req.getId() == 0) {
				LOGGER.info("Mandatory parameter if missing");
				errMsg.setErrCode(Constants.FIVE_THOUSAND_ONE);
				errMsg.setErrMsg(Constants.MISSING_PARAMETER);
				return false;
			}

			//Checking if the approver name must not be null or empty
			if (req.getApproved_by() == null) {
				LOGGER.info("Mandatory parameter if missing");
				errMsg.setErrCode(Constants.FIVE_THOUSAND_ONE);
				errMsg.setErrMsg(Constants.MISSING_PARAMETER);
				return false;
			}

			Request reqObj = requestDao.getRequestByReqIdAndApprover(req.getId(),req.getApproved_by(), location);
			if (null == reqObj) {
				LOGGER.info("Request with request Id and Approver name specified does not exists");
				errMsg.setErrCode(Constants.FIVE_THOUSAND_EIGHT);
				errMsg.setErrMsg(Constants.REQUEST_DOES_NOT_EXIXTS);
				return false;
			}else if(!(REQUESTSTATE.APPROVED.name().equals(reqObj.getState()) || REQUESTSTATE.CREATED.name().equals(reqObj.getState()))){
				LOGGER.info("Request with request Id and Approver name specified does not exists in APPROVED/CREATED state ");
				errMsg.setErrCode(Constants.FIVE_THOUSAND_EIGHT);
				errMsg.setErrMsg(Constants.REQUEST_DOES_NOT_EXIXTS_IN_APPROVED_STATE);
				return false;
			}

		}
		return true;
	}
	
	/**
	 * validates the request received for reset password
	 * 
	 * @param resetPassword
	 * @param errMsg
	 * @param locationName
	 * @return
	 */
	public boolean ValidatepswdRequest(ResetPasswordRequest resetPassword,
			ErrorMessage errMsg, String locationName) {
		if (!resetPassword.getRole().equalsIgnoreCase(Constants.ADMINISTRATION)
				&& !resetPassword.getRole().equalsIgnoreCase(Constants.SUPPORT)) {
			LOGGER.info("Invalid Role ");
			errMsg.setErrCode(Constants.FIVETHOUSANDFIFTEEN);
			errMsg.setErrMsg(Constants.INVALID_ROLE);
			return false;
		}

		AdminSupportPassword passwordFromDb = pswdResetdao.getOldPassword(
				resetPassword.getRole(), locationName);
		if (!resetPassword.getOldPassword().equals(
				passwordFromDb.getPassword())) {
			LOGGER.info("Invalid old password ");
			errMsg.setErrCode(Constants.FIVETHOUSANDSIXTEEN);
			errMsg.setErrMsg(Constants.OLD_PASSWRD_INCORRECT);
			return false;
		}

		if (!resetPassword.getNewPassword().equals(
				resetPassword.getConfirmPassword())) {
			LOGGER.info("confirm passwors and  new password do not match ");
			errMsg.setErrCode(Constants.FIVETHOUSANDSEVENTEEN);
			errMsg.setErrMsg(Constants.CONFIRM_NEW_PASSWRD_MISMATCH);
			return false;
		}
		
		if (resetPassword.getNewPassword().equals(
				resetPassword.getOldPassword())) {
			LOGGER.info("old passwors and  new password cannot be same ");
			errMsg.setErrCode(Constants.FIVETHOUSANDEIGHTEEN);
			errMsg.setErrMsg(Constants.NEW_OLD_PASSWRD_SAME);
			return false;
		}

		return true;

	}
}
