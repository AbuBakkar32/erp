package com.asl.enums;

/**
 * @author Zubayer Ahamed
 * @since Dec 04, 2020
 */
public enum RecordStatus {

	L("Live"), D("Deleted");

	private String description;

	private RecordStatus(String desc) {
		this.description = desc;
	}

	public String getDescription() {
		return description;
	}

	public String title() {
		return description;
	}

	/**
	 * Parse string value into {@link RecordStatus}.
	 * 
	 * @param value D or L
	 * @return {@link RecordStatus}
	 */
	public static RecordStatus parse(String value) {
		if (value == null) return null;
		value = value.trim();
		value = value.replace(" ", "");
		return value.isEmpty() ? null : valueOf(value);
	}
}
