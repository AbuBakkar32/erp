package com.asl.enums;

import com.asl.service.importexport.GenericImportExportColumns;

/**
 * @author Zubayer Ahamed
 * @since Jan 16, 2022
 */
public enum VoucherImportExportColumns implements GenericImportExportColumns {
	ROW_TYPE(0, "Row Type", "Column A", ImportExportColumnType.REQUIRED, "Row Type - (H - Header, D - Detail)"),
	VOUCHER_CODE(1, "Code/Voucher Number", "Column B", ImportExportColumnType.REQUIRED, "Voucher Code/Voucher Number"),
	VOUCHER_DATE(2, "Date/Account", "Column C", ImportExportColumnType.REQUIRED, "For H - (Voucher Date), D - (Account Number)"),
	VOUCHER_REFERENCE(3, "Reference/Sub Account", "Column D", ImportExportColumnType.OPTIONAL, "For H - (Voucher Reference), D - (Sub Account)"),
	LC_NO(4, "LC No./Note", "Column E", ImportExportColumnType.OPTIONAL, "For H - (LC No.), D - (Note) "),
	CHEQUE_NO(5, "Cheque No./Debit", "Column F", ImportExportColumnType.OPTIONAL, "For H - (Cheque No.), D - (Debit)"),
	NOTE(6, "Note/Credit", "Column G", ImportExportColumnType.OPTIONAL, "For H - (Note), D - (Credit)");

	private int columnIndex;
	private String columnName;
	private String column;
	private ImportExportColumnType iect;
	private String columnDescription;
	
	private VoucherImportExportColumns(int position, String code, String column, ImportExportColumnType iect, String description) {
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
