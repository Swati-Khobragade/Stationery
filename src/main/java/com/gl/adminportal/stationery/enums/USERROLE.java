package com.gl.adminportal.stationery.enums;

/**
 * This enum will be used to validate the user role which will be corresponding
 * to the entry in LDAP. Based on this enum only the user accessibility for the
 * application will be defined.
 * 
 * @author anand.kadhi
 *
 */
public enum USERROLE {

	/**
	 * User role for employee.
	 */
	EMPLOYEE("employee"),
	/**
	 * User role for employee.
	 */
	MANAGER("manager"),
	/**
	 * User role for Administrator.
	 */
	ADMINISTRATION(".Administration.Management"),
	/**
	 * User role for Employee services.
	 */
	EMPSERVICES(".Emp Services."),
	/**
	 * User role for Support.
	 */
	SUPPORT("support");

	private final String role;

	/**
	 * Private enum constructor (as we cannot invoke ourself)
	 * 
	 * @param role
	 */
	private USERROLE(String role) {
		this.role = role;
	}

	/**
	 * Validate the department and return the role accordingly.
	 * 
	 * @param department
	 * @return {@link USERROLE} or <code>null</code> accordingly.
	 */
	public static USERROLE validateUserRole(String department) {
		/*for (USERROLE userrole : USERROLE.values()) {
			if (userrole.getRole().equals(department)) {
				return userrole;
			}
		}*/
		return EMPLOYEE;
	}

	/**
	 * Return the role as String.
	 * 
	 * @return
	 */
	public String getRole() {
		return role;
	}

}
