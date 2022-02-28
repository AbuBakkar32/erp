package com.asl.model;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 *
 */
@Data
public class ImportExportModuleColumn {

	private int columnIndex;
	private String columnName;
	private String column;
	private String columnType;
	private String cssClass;
	private String columnDescription;
}
