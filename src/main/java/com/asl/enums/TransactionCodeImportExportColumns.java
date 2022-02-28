package com.asl.enums;

import com.asl.service.importexport.GenericImportExportColumns;

public enum TransactionCodeImportExportColumns implements GenericImportExportColumns {
	XTYPETRN(0, "Transaction Type", "Column A", ImportExportColumnType.REQUIRED, "Transaction Type"),
	XTRN(1, "Code", "Column B", ImportExportColumnType.REQUIRED, "Code"),
	XDESC(2, "Description", "Column C", ImportExportColumnType.OPTIONAL, "Description"),
	XNUM(3, "Starting", "Column D", ImportExportColumnType.OPTIONAL, "Starting (Default value 0)"),
	XINC(4, "Increment", "Column E", ImportExportColumnType.OPTIONAL, "Increment (Default value 1)");

	private int columnIndex;
	private String columnName;
	private String column;
	private ImportExportColumnType iect;
	private String columnDescription;

	private TransactionCodeImportExportColumns(int position, String code, String column, ImportExportColumnType iect, String description) {
		this.columnIndex = position;
		this.columnName = code;
		this.column = column;
		this.iect = iect;
		this.columnDescription = description;
	}

	@Override
	public int getColumnIndex() {
		return columnIndex;
	}

	@Override
	public String getColumnName() {
		return columnName;
	}

	@Override
	public String getColumn() {
		return column;
	}

	@Override
	public ImportExportColumnType getIect() {
		return iect;
	}

	@Override
	public String getColumnDescription() {
		return columnDescription;
	}

}
