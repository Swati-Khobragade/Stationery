package com.gl.adminportal.stationery.utils;

public interface Constants {
	// error codes
	public static String FIVE_THOUSAND_ONE = "5001";
	public static String FIVETHOUSANDTWO = "5002";
	public static String FIVE_THOUSAND_THREE = "5003";
	public static String FIVE_THOUSAND_FOUR = "5004";
	public static String FIVE_THOUSAND_FIVE = "5005";
	public static String FIVE_THOUSAND_SIX = "5006";
	public static String FIVE_THOUSAND_SEVEN = "5007";
	public static String FIVE_THOUSAND_EIGHT = "5008";
	public static String FIVE_THOUSAND_TWELVE = "5012";
	public static String FIVE_THOUSAND_THIRTEEN = "5013";
	public static String FIVE_THOUSAND_NINE = "5009";
	public static String FIVE_THOUSAND_TEN = "5010";
	public static String FIVE_THOUSAND_ELEVEN = "5011";
	public static String FIVE_THOUSAND_FOURTEEN = "5014";
	public static String FIVETHOUSANDFIFTEEN = "5015";
	public static String FIVETHOUSANDSIXTEEN = "5016";
	public static String FIVETHOUSANDSEVENTEEN = "5017";
	public static String FIVETHOUSANDEIGHTEEN = "5018";
	
	// error messages
	public static String MISSING_PARAMETER = "Missing parameter";
	public static String INVALID_PARAMETER = "Invalid parameter";
	public static String INVALID_LOCATION = "Invalid Location";
	public static String ITEM_ALREADY_EXISTS = "Item already present";
	public static String INTERNAL_SERVER_ERROR = "Internal server error";
	public static String ITEM_DOES_NOT_EXIXTS = "Item does not exists in inventory";
	public static String INVALID_START_DATE = "Invalid start date";
	public static String INVALID_END_DATE = "Invalid end date";
	public static String START_DATE_REQUIRED = "If end date is specified then start date is mandatory";
	public static String REQUEST_IN_PENDING_STATE = "The Item %s cannot be disable as requestId %s is Created/Pending Approval/Approved state";
	public static String INVALID_USER_NAME_PASSWORD = "Invalid username or password";
	public static String LOCATION_REQUIRED = "For admin/support login location is manadatory ";
	public static String REQUEST_DOES_NOT_EXIXTS = "Request with request Id and Approver name specified does not exists in database";
	public static String REQUEST_DOES_NOT_EXIXTS_IN_PENDING_STATE = "Request should be in pending state";
	public static String REQUEST_DOES_NOT_EXIXTS_IN_APPROVED_STATE = "Request should be in approved/created state";
	public static String INVALID_REQUEST_STATE = "Invalid Request state";
	public static String DEFAULT_DOMAIN = "@globallogic.com";
	public static String ITEM_ALEADY_IN_ENABLED_STATE = "Item is already in %s state";
	public static String INVALID_REQUEST_ID = "Invalid request id";
	public static String INVALID_ROLE = "Invalid role";
	public static String OLD_PASSWRD_INCORRECT = "Incorrect old password";
	String CONFIRM_NEW_PASSWRD_MISMATCH = "New password and confirm password do no match";
	String NEW_OLD_PASSWRD_SAME = "New password and old password cannot be same";
	public static String ITEM_ALREADY_PLACED="Item Already Placed 2 Times";
	public static String PLACED_SUCCESSFULLY="Item Created";
	// Feedback related constants
	String EMP_FEEDBACK = "empFeedback";
	String EMP_ID = "empId";
	String EMP_NAME = "empName";
	String EMP_MAIL = "mail";

	// Email related constants
	String EMAIL_HOST = "mail.smtp.host";
	String EMAIL_PORT = "mail.smtp.port";
	String EMAIL_AUTH = "mail.smtp.auth";
	String EMAIL_STARTTLS_ENABLE = "mail.smtp.starttls.enable";

	// role
	String ADMINISTRATION = "administration";
	String SUPPORT = "support";
}
