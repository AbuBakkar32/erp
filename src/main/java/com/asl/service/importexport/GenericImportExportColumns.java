package com.asl.service.importexport;

import com.asl.enums.ImportExportColumnType;

/**
 * @author Zubayer Ahamed
 *
 */
public interface GenericImportExportColumns {

	public int getColumnIndex();
	public String getColumnName();
	public String getColumn();
	public ImportExportColumnType getIect();
	public String getColumnDescription();
}
