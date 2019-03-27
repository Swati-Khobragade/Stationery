package com.gl.adminportal.stationery.request;

import com.fasterxml.jackson.annotation.JsonProperty;


public class ResetPasswordRequest {
	
	@JsonProperty("role")
	private String role;
	
	@JsonProperty("oldPassword")
	private String oldPassword;
	
	@JsonProperty("newPassword")
	private String newPassword;
	
	@JsonProperty("confirmPassword")
	private String confirmPassword;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

}
