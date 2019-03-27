package com.gl.adminportal.stationery.service;

import com.gl.adminportal.stationery.request.ResetPasswordRequest;

public interface PasswordService {

	boolean validateAdminSupport(String userName, String password, String location);
	
	void resetPassword(ResetPasswordRequest resetPasswrdReq, String locationName);
}
