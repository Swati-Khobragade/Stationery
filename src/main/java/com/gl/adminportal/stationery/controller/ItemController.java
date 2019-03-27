package com.gl.adminportal.stationery.controller;

import com.gl.adminportal.stationery.enums.ITEMOPERATION;
import com.gl.adminportal.stationery.exception.SystemException;
import com.gl.adminportal.stationery.model.Item;
import com.gl.adminportal.stationery.model.ItemAuditTrail;
import com.gl.adminportal.stationery.request.ItemRequest;
import com.gl.adminportal.stationery.response.ErrorMessage;
import com.gl.adminportal.stationery.response.ItemResponse;
import com.gl.adminportal.stationery.service.ItemService;
import com.gl.adminportal.stationery.utils.Constants;
import com.gl.adminportal.stationery.utils.ResponseConvertor;
import com.gl.adminportal.stationery.utils.ValidationUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class ItemController
{

	private static final Logger LOGGER = Logger.getLogger(ItemController.class);

	@Autowired
	ItemService itemService;

	@Autowired
	ValidationUtils validationUtils;

	@Autowired
	ResponseConvertor respConv;

	/**
	 * Add item in inventory
	 * @param itemRequest
	 * @param locationName
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value = "/v1/addItem", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ErrorMessage> addItem(
			@RequestBody List<ItemRequest> itemRequest,
			@RequestHeader("x-location-name") String locationName)
			throws SystemException
	{
		LOGGER.info("Entered the itemcontroller - addItem()");

		ErrorMessage errMsg = new ErrorMessage();
		// validation of item request
		if (!validationUtils.validateItemRequest(itemRequest, locationName,
		                                         errMsg))
		{
			throw new SystemException(errMsg.getErrCode(), errMsg.getErrMsg(),
			                          HttpStatus.BAD_REQUEST);
		}

		// validate Location
		if (!validationUtils.isValidateLocation(locationName, errMsg))
		{
			throw new SystemException(errMsg.getErrCode(), errMsg.getErrMsg(),
			                          HttpStatus.BAD_REQUEST);
		}
		try
		{
			int itemId = itemService.addItem(itemRequest, locationName);
			// add one entry to audit trail
			itemService.addItemToAuditTrail(itemRequest, locationName,
			                                ITEMOPERATION.ADD, itemId);
		}
		catch (Exception e)
		{
			LOGGER.error("Error occured while adding item: ", e);
			return new ResponseEntity<ErrorMessage>(new ErrorMessage(
					Constants.FIVE_THOUSAND_SEVEN,
					Constants.INTERNAL_SERVER_ERROR),
			                                        HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.CREATED);

	}

	/**
	 * Update item in inventory
	 * @param itemRequest
	 * @param locationName
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value = "/v1/updateItem", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ErrorMessage> updateItem(
			@RequestBody List<ItemRequest> itemRequest,
			@RequestHeader("x-location-name") String locationName)
			throws SystemException
	{

		ErrorMessage errMsg = new ErrorMessage();
		LOGGER.info("Entered the itemcontroller - updateItem()");

		// validate Location
		if (!validationUtils.isValidateLocation(locationName, errMsg))
		{
			throw new SystemException(errMsg.getErrCode(), errMsg.getErrMsg(),
			                          HttpStatus.BAD_REQUEST);
		}

		// validate update item request
		if (!validationUtils.validateUpdateItemReq(itemRequest, locationName,
		                                           errMsg))
		{
			throw new SystemException(errMsg.getErrCode(), errMsg.getErrMsg(),
			                          HttpStatus.BAD_REQUEST);
		}

		try
		{
			itemService.updateItem(itemRequest, locationName);
			// add an entry in audit Trail
			itemService.updateItemInAuditTrail(itemRequest, locationName,
			                                   ITEMOPERATION.UPDATE);
		}
		catch (Exception e)
		{
			LOGGER.error("Error occured while adding item: ", e);
			return new ResponseEntity<ErrorMessage>(new ErrorMessage(
					Constants.FIVE_THOUSAND_SEVEN,
					Constants.INTERNAL_SERVER_ERROR),
			                                        HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}

	/**
	 * Deletes the item whose it is recieved in request
	 * @param itemRequest
	 * @param locationName
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value = "/v1/enableDisableItem", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ErrorMessage> enableDisableItem(
			@RequestBody List<ItemRequest> itemRequest,
			@RequestHeader("x-location-name") String locationName)
			throws SystemException
	{
		LOGGER.info("Entered the itemcontroller - deleteItem()");
		ErrorMessage errMsg = new ErrorMessage();

		// validate Location
		if (!validationUtils.isValidateLocation(locationName, errMsg))
		{
			throw new SystemException(errMsg.getErrCode(), errMsg.getErrMsg(),
			                          HttpStatus.BAD_REQUEST);
		}

		// validate delete item request
		if (!validationUtils.validateDeleteItemReq(itemRequest, locationName,
		                                           errMsg))
		{
			throw new SystemException(errMsg.getErrCode(), errMsg.getErrMsg(),
			                          HttpStatus.BAD_REQUEST);
		}

		try
		{
			itemService.disableItem(itemRequest, locationName);
		}
		catch (Exception e)
		{
			LOGGER.error("Error occured while deleting item: ", e);
			if (e instanceof SystemException)
			{
				SystemException se = (SystemException) e;
				return new ResponseEntity<ErrorMessage>(new ErrorMessage(
						se.getErrorCode(), se.getErrorMessage()),
				                                        se.getHttpStatus());
			}
			else
			{
				return new ResponseEntity<ErrorMessage>(new ErrorMessage(
						Constants.FIVE_THOUSAND_SEVEN,
						Constants.INTERNAL_SERVER_ERROR),
				                                        HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Gets all items from DB
	 * @param role
	 * @param locationName
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value = "/v1/getAllItems", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Item>> getAllItems(
			@RequestParam(value = "role", required = false) String role,
			@RequestHeader("x-location-name") String locationName)
			throws SystemException
	{
		LOGGER.info("Entered the itemcontroller - getAllItems()");
		ErrorMessage errMsg = new ErrorMessage();

		// validate location name
		if (!validationUtils.isValidateLocation(locationName, errMsg))
		{
			throw new SystemException(errMsg.getErrCode(), errMsg.getErrMsg(),
			                          HttpStatus.BAD_REQUEST);
		}
		List<Item> items = new ArrayList<Item>();
		try
		{
			items = itemService.getAllItem(locationName, role);
			if (role == null || role == "")
			{
				role = "Employee";
				items.forEach(itemsPrice -> itemsPrice.setPrice(0));
			}

		}
		catch (Exception e)
		{
			LOGGER.error("Exception getting all items from inventory " + e);
			throw new SystemException(Constants.FIVE_THOUSAND_SEVEN,
			                          Constants.INTERNAL_SERVER_ERROR,
			                          HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<Item>>(items, HttpStatus.ACCEPTED);

	}

	/**
	 * Get item from DB based on the Item ID
	 * @param itemId
	 * @param role
	 * @param locationName
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value = "/v1/getItemById/{itemId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ItemResponse> getItemById(
			@PathVariable(value = "itemId") String itemId,
			@RequestParam(value = "role", required = false) String role,
			@RequestHeader("x-location-name") String locationName)
			throws SystemException
	{
		ErrorMessage errMsg = new ErrorMessage();
		Item item = null;
		LOGGER.info("Entered the itemcontroller - getItemsById() ");

		// validate locationname passed in request
		if (!validationUtils.isValidateLocation(locationName, errMsg))
		{
			throw new SystemException(errMsg.getErrCode(), errMsg.getErrMsg(),
			                          HttpStatus.BAD_REQUEST);
		}

		try
		{
			item = itemService.getItemById(Integer.parseInt(itemId),
			                               locationName, role);
			if (role == null || role == "")
			{
				role = "Employee";
				item.setPrice(0);
			}

			LOGGER.info("name of Item " + item.getName());
		}
		catch (Exception e)
		{
			LOGGER.error("Exception fetching item by id from inventory", e);
			if (e instanceof NullPointerException)
			{
				throw new SystemException(Constants.FIVE_THOUSAND_EIGHT,
				                          Constants.ITEM_DOES_NOT_EXIXTS, HttpStatus.NOT_FOUND);
			}
			else
			{
				throw new SystemException(Constants.FIVE_THOUSAND_SEVEN,
				                          Constants.INTERNAL_SERVER_ERROR,
				                          HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		;
		return new ResponseEntity<ItemResponse>(ResponseConvertor.getItemRespone(item),
		                                        HttpStatus.ACCEPTED);
	}

	/**
	 * Get AuditTrail
	 * @param startDate
	 * @param endDate
	 * @param locationName
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value = "/v1/getAuditTrail", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ErrorMessage> getAuditTrail(
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestHeader("x-location-name") String locationName)
			throws SystemException
	{
		LOGGER.info("Entered the itemcontroller - getAuditTrail()");
		ErrorMessage errMsg = new ErrorMessage();
		List itemAuditTrailResponse = null;

		// validate location name passed in request
		if (!validationUtils.isValidateLocation(locationName, errMsg))
		{
			throw new SystemException(errMsg.getErrCode(), errMsg.getErrMsg(),
			                          HttpStatus.BAD_REQUEST);
		}

		// validating dates if passed in request
		if (!validationUtils.validateDates(startDate, endDate, errMsg))
		{
			throw new SystemException(errMsg.getErrCode(), errMsg.getErrMsg(),
			                          HttpStatus.BAD_REQUEST);
		}
		try
		{
			List<ItemAuditTrail> itemAuditTrailResp = itemService
					                                          .getAuditTrail(locationName, startDate, endDate);
			itemAuditTrailResponse = respConv
					                         .convertToItemAuditTrailResponse(itemAuditTrailResp);
		}
		catch (Exception e)
		{
			LOGGER.error("Error processing request to get data for audit trail"
			             + e);
			throw new SystemException(Constants.FIVE_THOUSAND_SEVEN,
			                          Constants.INTERNAL_SERVER_ERROR,
			                          HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(itemAuditTrailResponse, HttpStatus.ACCEPTED);

	}

	@RequestMapping(value = "/v1/searchItem/{itemName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Item>> searchItem(
			@RequestHeader("x-location-name") String locationName,
			@PathVariable("itemName") String itemName) throws SystemException
	{
		LOGGER.info("In Itemcontroller - searchItem()");
		List<Item> itemList = new ArrayList<>();
		ErrorMessage errMsg = new ErrorMessage();
		// validate location name
		if (!validationUtils.isValidateLocation(locationName, errMsg))
		{
			throw new SystemException(errMsg.getErrCode(), errMsg.getErrMsg(),
			                          HttpStatus.BAD_REQUEST);
		}
		try
		{
			itemList = itemService.serachItem(itemName, locationName);
		}
		catch (Exception e)
		{
			LOGGER.error("Exception while searching for item ");
			throw new SystemException(Constants.FIVE_THOUSAND_SEVEN,
			                          Constants.INTERNAL_SERVER_ERROR,
			                          HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(itemList, HttpStatus.OK);
	}

	@RequestMapping(value = "/v1/getLatestItems", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Item>> latestItem(
			@RequestHeader("x-location-name") String locationName)
			throws SystemException
	{
		LOGGER.info("In ItemController - latestItem()");
		ErrorMessage errMsg = new ErrorMessage();
		List<Item> itemList;
		List<ItemResponse> itemResponseList = new ArrayList<>();
		// validate location
		if (!validationUtils.isValidateLocation(locationName, errMsg))
		{
			throw new SystemException(errMsg.getErrCode(), errMsg.getErrMsg(),
			                          HttpStatus.BAD_REQUEST);
		}

		try
		{
			itemList = itemService.getLatestItem(locationName);
			for (Item i : itemList)
			{
				ItemResponse convertedItem = ResponseConvertor.getItemRespone(i);
				itemResponseList.add(convertedItem);
			}
			LOGGER.info("Done fetching latest items " + itemList);
		}
		catch (Exception e)
		{
			LOGGER.error("Exception processing request to get latest items", e);
			throw new SystemException(Constants.FIVE_THOUSAND_SEVEN,
			                          Constants.INTERNAL_SERVER_ERROR,
			                          HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(itemResponseList, HttpStatus.OK);
	}

	/**
	 * Exception handler
	 * @param e
	 * @return
	 */
	@ExceptionHandler(SystemException.class)
	public ResponseEntity<ErrorMessage> handle(SystemException e)
	{
		ErrorMessage errResponse = new ErrorMessage();
		errResponse.setErrCode(e.getErrorCode());
		errResponse.setErrMsg(e.getErrorMessage());
		return new ResponseEntity<ErrorMessage>(new ErrorMessage(
				e.getErrorCode(), e.getErrorMessage()), e.getHttpStatus());
	}

}
