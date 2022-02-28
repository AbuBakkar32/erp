package com.asl.enums;

/**
 * @author Zubayer Ahamed
 * @since Dec 27, 2020
 */
public enum ReportType {

	PDF("PDF","PDF"), EXCEL("EXCEL", "Excel"), EXCEL_DATA("EXCEL_DATA", "Excel Data Only");

	private String code;
	private String description;

	private ReportType(String code, String des) {
		this.code = code;
		this.description = des;
	}

	public String getCode() {
		return this.code;
	}

	public String getDescription() {
		return this.description;
	}
}
