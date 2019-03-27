package com.gl.adminportal.stationery.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gl.adminportal.stationery.controller.AuthenticationController;
import com.gl.adminportal.stationery.dao.PasswordDao;
import com.gl.adminportal.stationery.request.ResetPasswordRequest;
import com.gl.adminportal.stationery.service.PasswordService;

@Service("PasswordService")
public class PasswordServiceImpl implements PasswordService {

	@Autowired
	PasswordDao passwordDao;

	@Override
	public boolean validateAdminSupport(String userName, String password,
			String location) {

		return passwordDao.validateAdminSupport(userName, password, location);
	}

	@Override
	public void resetPassword(ResetPasswordRequest resetPasswrdReq,
			String locationName) {
		passwordDao.resetPassword(resetPasswrdReq, locationName);

	}

}
