package com.asl.enums;

import com.asl.service.importexport.GenericImportExportColumns;

public enum CodesImportExportColumns implements GenericImportExportColumns {
	XTYPE(0, "Code Type", "Column A", ImportExportColumnType.REQUIRED, "Code Type"),
	XCODE(1, "Code", "Column B", ImportExportColumnType.REQUIRED, "Code"),
	XDESCDET(2, "Description", "Column C", ImportExportColumnType.OPTIONAL, "Description"),
	SEQN(3, "Sequence", "Column D", ImportExportColumnType.OPTIONAL, "Sequence (Default value 0)");

	private int columnIndex;
	private String columnName;
	private String column;
	private ImportExportColumnType iect;
	private String columnDescription;

	private CodesImportExportColumns(int position, String code, String column, ImportExportColumnType iect, String description) {
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
