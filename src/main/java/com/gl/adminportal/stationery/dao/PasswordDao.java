package com.gl.adminportal.stationery.dao;

import com.gl.adminportal.stationery.model.AdminSupportPassword;
import com.gl.adminportal.stationery.request.ResetPasswordRequest;

public interface PasswordDao {
	
	boolean  validateAdminSupport(String userName, String password, String location);
	
	AdminSupportPassword getOldPassword(String role, String locationName);
	
	void resetPassword(ResetPasswordRequest resetpasswrdReq, String locationName);
	
}
