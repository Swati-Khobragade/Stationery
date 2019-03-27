package com.gl.adminportal.stationery.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gl.adminportal.stationery.exception.SystemException;
import com.gl.adminportal.stationery.service.RequestService;

@RestController
@CrossOrigin(origins = "*")
public class ItemValidatorController {

	private static final Logger LOGGER = Logger.getLogger(ItemValidatorController.class);

	@Autowired
	private RequestService requestService;

	@RequestMapping(value = "/v1/itemCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> itemCount(@RequestParam (value = "itemId", required = false) int itemId ,
			@RequestParam(value ="empId", required = true) int empId, @RequestParam(value = "location", required = true) String location)
			throws SystemException {
		LOGGER.info("Inside ItemCount Method");
		Long totalQuantity = requestService.getItemCount(empId, location, itemId);
		return new ResponseEntity<>(totalQuantity,HttpStatus.OK);
	}
}
