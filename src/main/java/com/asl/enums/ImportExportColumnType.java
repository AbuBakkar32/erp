package com.asl.enums;

/**
 * @author Zubayer Ahamed
 *
 */
public enum ImportExportColumnType {

	REQUIRED("Required", "label label-danger"), 
	OPTIONAL("Optional", "label label-default"), 
	CONDITIONAL("Conditional", "label label-warning");

	private String code;
	private String cssClass;

	private ImportExportColumnType(String code, String cssClass) {
		this.code = code;
		this.cssClass = cssClass;
	}

	public String getCode() {
		return this.code;
	}

	public String getCssClass() {
		return this.cssClass;
	}
}
