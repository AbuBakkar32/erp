package com.asl.enums;

import com.asl.service.importexport.GenericImportExportColumns;

public enum ListImportExportColumns implements GenericImportExportColumns {
	LIST_TYPE(0, "List Type", "Column A", ImportExportColumnType.REQUIRED, "List Head or List Data - (H - Header, D - Detail)"),
	LIST_CODE(1, "List Code", "Column B", ImportExportColumnType.REQUIRED, "List Code"),
	LIST_DESC(2, "List Description", "Column C", ImportExportColumnType.OPTIONAL, "List Description"),
	LIST_PRMPT1(3, "List Prompt 1", "Column D", ImportExportColumnType.OPTIONAL, "List Prompt 1"),
	LIST_PRMPT2(4, "List Prompt 2", "Column E", ImportExportColumnType.OPTIONAL, "List Prompt 2"),
	LIST_PRMPT3(5, "List Prompt 3", "Column F", ImportExportColumnType.OPTIONAL, "List Prompt 3"),
	LIST_PRMPT4(6, "List Prompt 4", "Column G", ImportExportColumnType.OPTIONAL, "List Prompt 4"),
	LIST_PRMPT5(7, "List Prompt 5", "Column H", ImportExportColumnType.OPTIONAL, "List Prompt 5"),
	LIST_PRMPT6(8, "List Prompt 6", "Column I", ImportExportColumnType.OPTIONAL, "List Prompt 6"),
	LIST_PRMPT7(9, "List Prompt 7", "Column J", ImportExportColumnType.OPTIONAL, "List Prompt 7"),
	LIST_PRMPT8(10, "List Prompt 8", "Column K", ImportExportColumnType.OPTIONAL, "List Prompt 8"),
	LIST_PRMPT9(11, "List Prompt 9", "Column L", ImportExportColumnType.OPTIONAL, "List Prompt 9"),
	LIST_PRMPT10(12, "List Prompt 10", "Column M", ImportExportColumnType.OPTIONAL, "List Prompt 10"),
	LIST_PRMPT11(13, "List Prompt 11", "Column N", ImportExportColumnType.OPTIONAL, "List Prompt 11"),
	LIST_PRMPT12(14, "List Prompt 12", "Column O", ImportExportColumnType.OPTIONAL, "List Prompt 12"),
	LIST_PRMPT13(15, "List Prompt 13", "Column P", ImportExportColumnType.OPTIONAL, "List Prompt 13"),
	LIST_PRMPT14(16, "List Prompt 14", "Column Q", ImportExportColumnType.OPTIONAL, "List Prompt 14"),
	LIST_PRMPT15(17, "List Prompt 15", "Column R", ImportExportColumnType.OPTIONAL, "List Prompt 15"),
	LIST_PRMPT16(18, "List Prompt 16", "Column S", ImportExportColumnType.OPTIONAL, "List Prompt 16");

	private int columnIndex;
	private String columnName;
	private String column;
	private ImportExportColumnType iect;
	private String columnDescription;

	private ListImportExportColumns(int position, String code, String column, ImportExportColumnType iect, String description) {
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
