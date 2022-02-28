package com.asl.enums;

/**
 * @author Zubayer Ahamed
 * @since Dec 1, 2020
 */
public enum ProfileType {

	U("U", "User Profile"),
	M("M", "Menu Profile"),
	R("R", "Report Profile"),
	MOD("MOD","Module Profile"),
	
	//for pos business
	C("C", "Casual Dine"),
	F("F", "Fine Dine"),
	
	//for Adjustment With
	AP("AP", "AP"),
	AR("AR", "AR"),
	LE("LE", "Ledger");

	private String code;
	private String description;

	private ProfileType(String code, String des) {
		this.code = code;
		this.description = des;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}
}
