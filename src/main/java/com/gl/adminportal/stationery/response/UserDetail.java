package com.gl.adminportal.stationery.response;

import com.gl.adminportal.stationery.enums.USERROLE;

public class UserDetail {

	String empId;

	String empName;

	USERROLE role;

	String empEmail;

	String managerMailOne;

	String managerMailTwo;

	String managerMailThree;

	String location;

	String empImage;

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public USERROLE getRole() {
		return role;
	}

	public void setRole(USERROLE role) {
		this.role = role;
	}

	public String getEmpEmail() {
		return empEmail;
	}

	public void setEmpEmail(String empEmail) {
		this.empEmail = empEmail;
	}

	public String getManagerMailOne() {
		return managerMailOne;
	}

	public void setManagerMailOne(String managerMailOne) {
		this.managerMailOne = managerMailOne;
	}

	public String getManagerMailTwo() {
		return managerMailTwo;
	}

	public void setManagerMailTwo(String managerMailTwo) {
		this.managerMailTwo = managerMailTwo;
	}

	public String getManagerMailThree() {
		return managerMailThree;
	}

	public void setManagerMailThree(String managerMailThree) {
		this.managerMailThree = managerMailThree;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getEmpImage() {
		return empImage;
	}

	public void setEmpImage(String empImage) {
		this.empImage = empImage;
	}

}
