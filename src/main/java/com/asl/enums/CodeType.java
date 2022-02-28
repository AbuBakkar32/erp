
package com.asl.enums;

/**
 * 
 * @author Zubayer Ahamed
 * @since Feb 28, 2021
 */
public enum CodeType {
	
	ADJUSTMENT_TYPE("Adjustment Type"),
	ADJUSTMENT_WITH("Adjustment With"),

	BANK_STATUS("Bank Status"),
	BLOOD_GROUP("Blood Group"),
	BRANCH("Branch"),
	BRAND("Brand"),
	BIN_NUMBER("Bin Number"),

	CURRENCY_OF_COST("Currency of Cost"),
	CURRENCY_OF_PRICE("Currency of Price"),
	CURRENCY("Currency"),
	CUSTOMER_GROUP("Customer Group"),
	CUSTOMER_STATUS("Customer Status"),
	CHEQUE_STATUS("Cheque Status"),
	COMMITEECONTRIBUTION_TYPE("Committee Contribution Type"),
	COMMITEEMEMBER_TYPE("Committee Member Type"),
	
	

	DAG_TYPE("Dag Type"),
	DISCOUNT_TYPE("Discount Type"),

	EXPENSE_PURPOSE("Cost_Expense"),
	EMPLOYEE_CATEGORY("Employee Category"),
	EMPLOYEE_DEPARTMENT("Employee Department"),
	EMPLOYEE_DESIGNATION("Employee Designation"),
	EMPLOYEE_STATUS("Employee Status"),
	EMPLOYEE_TYPE("Employee Type"),
	EVENTMEMBER_TYPE("Event Member Type"),
	EVENTCOMMITEE_TYPE("Event Committee Type"),

	GRN_STATUS("GRN Status"),
	GRN_RETURN_STATUS("GRN Return Status"),

	JOURNAL_VOUCHER_STATUS("Journal Voucher Status"),

	ITEM_GROUP("Item Group"),
	//Land Experience Person Start
	EXPERIENCE_TRANSACTION_TYPE("Experience Transaction type"),
	EXPERIENCE_TYPE("Experience type"),
	//Land Experience Person End
	
	DEPARTMENT("Department Name"),
	DESIGNATION("Designation"),
	DISTRICT("District"),
	//Land Document Start
	DOCUMENT_TYPE("Document type"),
	//Land Document End
	DELIVERY_LOCATION("Delivery Location"),
	
	EMPLOYESS_STATUS("Employee Status"),
	EMPLOYESS_CATEGORY("Employee Category"),
	
	ITEM_CATEGORY("Item Category"),
	ITEM_SUB_CATEGORY("Item Sub-Category"),
	CONSUMPTION_PROCESS("Inventory Consumption Process"),
	ITEM_SIZE("Item Size"),
	
	GRADE("Grade"),
	
	HOLIDAY_APPLICABLE("Holiday Applicable"),
	HS_CODE("HS Code"),
	
	LAND_UNIT("Land Unit"),
	LAND_TYPE("Land Type"),
	LANDITEM_CATEGORY("Item Categories"),
	LEAVE_PLAN("Leave Plan"),
	
	MARITAL_STATUS("Marital Status"),
	MEMBER_GROUP("Member Group"),
	

	OBJECT_TYPE("Object Type"),
	OWNER_TYPE("Owner Type"),
	OTHEREVENT_TYPE("Other Event Type"),

	PURCHASE_UNIT("Unit of Measurement"),
	PURCHASE_ORDER_STATUS("Purchase Order Status"),
	PAYMENT_TYPE("Payment Type"),
	PAYMENT_MODE("Payment Mode"),
	PRIORITY_TYPE("Priority Type"),
	PURPOSE("Purpose"),
	PURCHASE_TYPE("Purchase Type"),
	PROJECT("Project"),

	REQUISITION_ORDER_STATUS("Requisition Order Status"),
	RELIGION("Religion"),
	ROLE_TYPE("Role Type"),

	SALARY_TYPE("PD Transaction Type"),
	SELLING_UNIT("Selling Unit"),
	SITE("Site"),
	STOCK_TYPE("Stock Type"),
	STOCKING_UNIT("Stocking Unit"),
	SUPPLIER_GROUP("Supplier Group"),
	SUPPLIER_STATUS("Supplier Status"),
	STATUS("Status"),
	STATUS_TYPE("Land Status"),
	SALES_AND_INVOICE_STATUS("Sales & Invoice Status"),
	SEX("Sex"),
	STORE("Store"),

	TAX_CATEGORY("Tax Category"),
	TRANSFER_ORDER_STATUS("Transfer Order Status"),
	TAG_STATUS("TAG Status"),
	TRANSFER_TO("Transfer To"),
	TERMS_AND_CONDITION("PO Terms & Condition"),
	TERMS_TYPE("Terms Type"),

	WEEK_DAY("Week Day"),
	WAREHOUSE("Warehouse");

	private String code;

	private CodeType(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}
}
