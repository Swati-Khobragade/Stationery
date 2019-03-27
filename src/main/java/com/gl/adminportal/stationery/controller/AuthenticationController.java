package com.gl.adminportal.stationery.controller;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.gl.adminportal.stationery.enums.USERROLE;
import com.gl.adminportal.stationery.exception.SystemException;
import com.gl.adminportal.stationery.response.ErrorMessage;
import com.gl.adminportal.stationery.response.UserDetail;
import com.gl.adminportal.stationery.service.PasswordService;
import com.gl.adminportal.stationery.utils.Constants;
import com.gl.adminportal.stationery.utils.ValidationUtils;

/**
 * This controller is used to authenticate the user using GLO API.
 * 
 * @author neeta.kumbhare
 *
 */
@RestController
@CrossOrigin(origins = "*")
public class AuthenticationController {

	private static final Logger LOGGER = Logger
			.getLogger(AuthenticationController.class);
	
	@Autowired
	PasswordService passwordService;
	
	@Autowired
	ValidationUtils validationUtils;

	/**
	 * Request Mapping to login using GLO Api.
	 * 
	 * @param locationId
	 * @param authBytes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/v1/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDetail> login(
			@RequestHeader("x-authBytes") String authBytes,
			@RequestParam(value = "location", required= false) String location) throws Exception {
		LOGGER.info("Entered Authentication controller");
		UserDetail userDetails;
		Long starTime = System.currentTimeMillis();
		LOGGER.info("Entered Authentication controller ");
		
		// encode data on your side using BASE64

		byte[] tokens = Base64.decodeBase64(authBytes.getBytes());
		String[] userTokens = (new String(tokens)).split(":");
		String userName = userTokens[0];
		String password = userTokens[1];

		if (userName.equalsIgnoreCase("support")
				|| userName.equalsIgnoreCase("admin")) {
			ErrorMessage errMsg = new ErrorMessage();
			// validate location if passed in request
			if(null == location){
				throw new SystemException(Constants.FIVE_THOUSAND_TEN, Constants.LOCATION_REQUIRED,
						HttpStatus.BAD_REQUEST);
			}
			if(!validationUtils.isValidateLocation(location, errMsg)){
				throw new SystemException(errMsg.getErrCode(), errMsg.getErrMsg(),
						HttpStatus.BAD_REQUEST);
			}
			if(!passwordService.validateAdminSupport(userName, password, location)){
				return new ResponseEntity<UserDetail>(HttpStatus.UNAUTHORIZED);
			}else{
				return new ResponseEntity<UserDetail>(getAdminSupportLogin(
						userName, password,location), HttpStatus.OK);
			}
		} else {

			userDetails = authenticateFromGlo(userName, password);
			
			if(userDetails.getEmpId().equals("321118")) {
				userDetails.setRole(USERROLE.ADMINISTRATION);
			}
			
			
			LOGGER.info("Time taken to complete the request "+(System.currentTimeMillis() - starTime));
			return new ResponseEntity<UserDetail>(userDetails, HttpStatus.OK);
		}
	}

	/**
	 * This method is used to authenticate employee using the GLO API
	 * 
	 * @param userName
	 * @param password
	 * @return
	 * @throws JSONException
	 * @throws SystemException 
	 */
	@SuppressWarnings("null")
	private UserDetail authenticateFromGlo(String userName, String password)
			throws JSONException, SystemException {
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		RestTemplate restTemplate = new RestTemplate(requestFactory);
		RestTemplate searchRestTemplate = new RestTemplate(requestFactory);

		UserDetail userDetails = new UserDetail();

		String token = userName + ":" + password;
		byte[] tokenBytes = Base64.encodeBase64(token.getBytes());
		LOGGER.info("ecncoded value is " + new String(tokenBytes));

		HttpHeaders headers = new HttpHeaders();
		HttpHeaders searchUserheaders = new HttpHeaders();
		headers.add("Authorization", "Basic " + new String(tokenBytes));

		ResponseEntity<String> response = null;

		ResponseEntity<String> userSearchResponse = null;
		ResponseEntity<String> directreportsResponse = null;
		try {
			response = restTemplate
					.exchange(
							"https://gloapis.globallogic.com/e80d6b8cbea3754bab60a51a6e72b35329df377b/gloapis/login.js",
							HttpMethod.GET, new HttpEntity<String>(headers),
							String.class);
		} catch (Exception e) {
			if(response == null || response.getStatusCode().equals(HttpStatus.UNAUTHORIZED)){
				LOGGER.error("Invalid userName or password " + e);
				throw new SystemException(Constants.FIVE_THOUSAND_NINE, Constants.INVALID_USER_NAME_PASSWORD,
						HttpStatus.UNAUTHORIZED);
			}
			
		}
		String loginResponse = response.getBody();
		LOGGER.info("Response from Login API: " + loginResponse);
		JSONObject jsonObject = new JSONObject(loginResponse);
		String app_token = jsonObject.getString("app_token");
		LOGGER.info(app_token + " app token");

		/**
		 * get the user details
		 */
		userSearchResponse = searchRestTemplate.exchange(
				"http://gloapis.globallogic.com/e80d6b8cbea3754bab60a51a6e72b35329df377b/"
						+ app_token + "/gloapis/" + userName
						+ "/get_user_info_by_login.js", HttpMethod.GET,
				new HttpEntity<String>(searchUserheaders), String.class);

		String getUserResponse = userSearchResponse.getBody();
		LOGGER.info("Get user Response: " + getUserResponse);
		JSONObject jsonObjectUser = new JSONObject(getUserResponse);
		String ecode = jsonObjectUser.getString("ecode");
		String displayname = jsonObjectUser.getString("displayname");
		String email = jsonObjectUser.getString("email");
		String pictureUrl = jsonObjectUser.getString("small_avatar");
	
		String location = jsonObjectUser.getString("working_location");
		JSONObject managerJSONObj = jsonObjectUser.getJSONObject("manager");
		String managerMail = managerJSONObj.getString("login");
		LOGGER.debug("ecode: " + ecode + " , displayname: " + displayname
				+ " , employee email: " + email + " , pictureUrl: "
				+ pictureUrl + " , location: " + location
				+ "+ ,  manager email : " + managerMail);
		
		// get direct reports of an employee to decide the role (Manager /
		// Employee)

		directreportsResponse = searchRestTemplate.exchange(
				"http://gloapis.globallogic.com/e80d6b8cbea3754bab60a51a6e72b35329df377b/"
						+ app_token + "/gloapis/"
						+ jsonObjectUser.getString("login")
						+ "/get_user_direct_reports", HttpMethod.GET,
				new HttpEntity<String>(searchUserheaders), String.class);
		String getDirectReportsResponse = directreportsResponse.getBody();
		JSONObject directReportsJsonObj = XML
				.toJSONObject(getDirectReportsResponse);
		LOGGER.info("Get user Response: " + directReportsJsonObj);

		String noOfReports = directReportsJsonObj.getJSONObject(
				"direct_reports").getString("total_count");

		if (Integer.parseInt(noOfReports) > 0) {
			userDetails.setRole(USERROLE.MANAGER);
		} else {
			userDetails.setRole(USERROLE.EMPLOYEE);
		}

		// get second level manager
		userSearchResponse = searchRestTemplate.exchange(
				"http://gloapis.globallogic.com/e80d6b8cbea3754bab60a51a6e72b35329df377b/"
						+ app_token + "/gloapis/" + managerMail
						+ "/get_user_info_by_login.js", HttpMethod.GET,
				new HttpEntity<String>(searchUserheaders), String.class);
		getUserResponse = userSearchResponse.getBody();
		LOGGER.info("Get user Response: " + getUserResponse);
		jsonObjectUser = new JSONObject(getUserResponse);
		managerJSONObj = jsonObjectUser.getJSONObject("manager");
		String managerTwoMail = managerJSONObj.getString("login");
		LOGGER.debug("Manager Two mail " + managerTwoMail);	
		
		// get third level manager
		userSearchResponse = searchRestTemplate.exchange(
				"http://gloapis.globallogic.com/e80d6b8cbea3754bab60a51a6e72b35329df377b/"
						+ app_token + "/gloapis/" + managerTwoMail
						+ "/get_user_info_by_login.js", HttpMethod.GET,
				new HttpEntity<String>(searchUserheaders), String.class);
		getUserResponse = userSearchResponse.getBody();
		LOGGER.info("Get user Response: " + getUserResponse);
		jsonObjectUser = new JSONObject(getUserResponse);
		managerJSONObj = jsonObjectUser.getJSONObject("manager");
		String managerThreeMail = managerJSONObj.getString("login");
		LOGGER.debug("Manager Two mail " + managerThreeMail);			

		
		if (response.getStatusCode().equals(HttpStatus.OK)) {
			// set the userdetails for empName and empId
			userDetails.setEmpId(ecode);
			userDetails.setEmpName(displayname);
			userDetails.setEmpEmail(email);
			userDetails.setEmpImage(pictureUrl);
			userDetails.setLocation(location);
			userDetails.setManagerMailOne(managerMail + Constants.DEFAULT_DOMAIN);
			userDetails.setManagerMailTwo(managerTwoMail + Constants.DEFAULT_DOMAIN);
			userDetails.setManagerMailThree(managerThreeMail + Constants.DEFAULT_DOMAIN);
		}
		return userDetails;
	}
	
	public UserDetail getAdminSupportLogin(String username, String password, String location){
		UserDetail userDetail = new UserDetail();
		if(username.equalsIgnoreCase("admin")){
			userDetail.setRole(USERROLE.ADMINISTRATION);
		}else{
			userDetail.setRole(USERROLE.SUPPORT);
		}
		userDetail.setEmpName(username);
		userDetail.setLocation(location);
		return userDetail;
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
