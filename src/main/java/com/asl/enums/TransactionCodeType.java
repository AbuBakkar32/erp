package com.asl.enums;

/**
 * @author Zubayer Ahamed
 * @since Feb 28, 2021
 */

public enum TransactionCodeType {

	ACCOUNT_PAYABLE("AR Transactions", "AP--"),
	ACCOUNT_PAYMENT("AR Transactions", "PM--"),
	ACCOUNT_OBAP("AR Transactions", "OBAP"),
	ACCOUNT_OBAR("AR Transactions", "OBAR"),
	ACCOUNT_ADAP("AR Transactions", "ADAP"),
	ACCOUNT_ADAR("AR Transactions", "ADAR"),
	ACCOUNT_AR("AR Transactions", "AR--"),
	ASSET_TRANSFER_ORDER("ASTO Number", "ASTO"),

	BOM_NUMBER("BOM Number", "BOM-"),
	BATCH_NUMBER("Batch Number", "BN--"),
	BANK_CODE("Bank Code","BNK-"),

	COMMITTEEINFO_ID("Committee ID", "CMT-"),
	CUSTOMER_NUMBER("Customer Number", "CUS-"),
	CHALAN_NUMBER("CH Number", "CH--"),

	DOCUMENT_NAME("Document ID", "DOCM"),

	//EMPLOYEE_ID("Employee ID", "EID-", TransactionCodes.EMPLOYEE_ID),

	GRN_NUMBER("GRN Number", "GRN-"),
	GL_VOUCHER("GL Voucher", "JV--"),

	HALL_BOOKING_SALES_ORDER("Hall Booking SO", "HBSO"),
	HR_EMPLOYEE_ID("HR Employee ID", "EID-"),

	ISU_NUMBER("ISU Number", "ISU-"),
	ITEM_NUMBER("Item Number", "IC--"),
	INVENTORY_NUMBER("Inventory Number", "IMT-"),
	INVENTORY_TRANSACTION("Inventory Transaction", "PO--"),
	INVENTORY_TRANSACTION2("Inventory Transaction", "RE--"),
	INVENTORY_TRANSACTION3("Inventory Transaction", "DMIS"),
	INVENTORY_TRANSACTION4("Inventory Transaction", "MOIS"),
	INVENTORY_TRANSFER_ORDER("Store Transfer", "PTO--"),
	INVENTORY_STORE_REQUISITION("Store Requisition", "SRO-"),
	INVOICE_NO("Invoice No", "GRN-"),

	EMPLOYEE_NUMBER("Employee Number", "EID-"),
	ENTER_PROJECT_TRANSFER("Project Transfer","PTO-"),
	
	LAND_ID("Land ID", "LND-"),
	LANDMEMBER_ID("Member & Director ID", "MEM-"),
	LANDDIRECTOR_ID("Member & Director ID", "DIR-"),
	
	LANDCOMEVENT_ID("Land Com Event", "EVCM"),
	LANDOTHEREVENT_ID("Land Other Event", "EVOH"),
	LIST_CODE("List Code","LIST"),
	LOAN_NUMBER("Loan Number","LOAN"),

	MONEY_RECEIPTS("AR Transactions", "MR--"),

	PAYROLL_EMPLOYEE_ID("Payroll Employee ID", "EID-"),
	PERSON_ID("Person ID", "PRSN"),
	PURCHASE_ORDER("PO Number", "PO--"),
	PURCHASE_ORDER_LCM("LCM PO Number", "PO--"),
	PURCHASE_RETURN("PRN Number", "PR--"),
	PROC_ERROR("PROC Error", "ERROR"),
	PROFILE_LINE("Profile Line", "PRL-"),
	PROFILE_ALLOCATION("Profile Allocation", "PAL-"),
	PROJECT_NUMBER("Project Number","PROJ"),
	PROJECT_BUDGET("Project Budget","PBGT"),
	PROJECT_EXPENSE("Project Expense","PEXP"),

	QC_NUMBER("QC Number","QC--" ),

	PURCHASE_REQUISITION("Purchase Requisition", "PRRQ"),
	ROOM_BOOKING_SALES_ORDER("Room Booking SO", "RBSO"),

	SUPPLIER_NUMBER("Supplier Number", "SUP-"),
	SURVEYOR_ID("Surveyor ID", "SRVR"),
	STOCK_TAKE("TAG Number", "TAG-"),
	SALES_ORDER("SO Number", "SO--"),
	SALES_AND_INVOICE_NUMBER("DO Number", "DO--"),
	SALES_RETURN("CRN Number", "SR--"),
	SALES_REQUESTION_ORDER("SRQ Number", "SRQ-"),
	SVRQ_NUMBER("SVRQ Number", "SVRQ"),

	TRANSACTION_TRANSFER("Transfer Transaction", "MOFN"),
	TEST_NUMBER("Test Number","TST-"),
	TSK_NUMBER("TSK Number","TSK-"),


	VOUCHER_NUMBER("Voucher Number", "VCH-"),

	WO_NUMBER("WO Number", "WO--");
	
	///

	private String code;
	private String defaultCode;
	//private Map<String, String> xtrnCodes;

	private TransactionCodeType(String code, String defaultCode) {
		this.code = code;
		this.defaultCode = defaultCode;
	}

	public String getCode() {
		return this.code;
	}
	
	public String getdefaultCode() {
		return this.defaultCode;
	}
}
