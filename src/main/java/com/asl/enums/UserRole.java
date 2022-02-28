package com.asl.enums;

/**
 * @author Zubayer Ahamed
 * @since 27-11-2020
 */
public enum UserRole {

	SYSTEM_ADMIN("ROLE_SYSTEM_ADMIN", "System Admin"),
	ADMIN("ROLE_ADMIN", "Admin"),
	APPUSER("ROLE_APPUSER", "App User"),
	DRIVER("ROLE_DRIVER", "Driver"),
	SUBSCRIBER("ROLE_SUBSCRIBER", "Subscriber"),
	ROLE1("ROLE_ROLE1", "Role 1"),
	ROLE2("ROLE_ROLE2", "Role 2"),
	ROLE3("ROLE_ROLE3", "Role 3"),
	ROLE4("ROLE_ROLE4", "Role 4"),
	ROLE5("ROLE_ROLE5", "Role 5"),
	ROLE6("ROLE_ROLE6", "Role 6"),
	ROLE7("ROLE_ROLE7", "Role 7"),
	ROLE8("ROLE_ROLE8", "Role 8"),
	ROLE9("ROLE_ROLE9", "Role 9"),
	ROLE10("ROLE_ROLE10", "Role 10");

	private String code;
	private String roleName;

	private UserRole(String code, String roleName) {
		this.code = code;
		this.roleName = roleName;
	}

	public String getCode() {
		return code;
	}

	public String getRoleName() {
		return roleName;
	}
}
