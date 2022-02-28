package com.asl.enums;

import java.util.Map;

import com.asl.model.ReportParamMap;

/**
 * @author Zubayer Ahamed
 * @since Dec 26, 2020
 */
public enum ReportMenu {
	
	//Branch Activities Report
	RM0101(999, "100", "Branch Activites Report", "Requisition Listing Report", ReportParamMap.RM0101, "Y", false, false, 1000, 8),
	RM0102(999, "100", "Branch Activites Report", "Sales Listing Report", ReportParamMap.RM0102, "Y", false, false, 1000, 8),
	RM0103(999, "100", "Branch Activites Report", "Customer Ledger Details Report", ReportParamMap.RM0103, "Y", false, false, 1000, 8),
	RM0104(999, "100", "Branch Activites Report", "Assest Transfer Listing Report", ReportParamMap.RM0104, "Y", false, false, 1000, 8),
	RM0105(999, "100", "Branch Activites Report", "Sales Return Listing Report", ReportParamMap.RM0105, "Y", false, false, 1000, 8),
	RM0106(999, "100", "Branch Activites Report", "Requisition Report", ReportParamMap.RM0106, "Y", false, false, 1000,8),

	// Procurements  -- M0300
	RM0301(999, "300", "Procurements", "Purchase Order Listing", ReportParamMap.RM0301, "Y", false, false, 1000, 1),
	RM0305(999, "300", "Procurements", "GRN Listing", ReportParamMap.RM0305, "Y", false, false, 1000, 1),
	RM0312(999, "300", "Procurements", "Purchase Return Listing", ReportParamMap.RM0312, "Y", false, false, 1000, 1),
	//RM0304(999, "300", "Procurements", "Purchase Deviation Listing", ReportParamMap.RM0304, "Y", false, false, 1000, 1),

	// Accounts Payable
	RM0307(999, "300", "Accounts Payable", "Supplier Ledger Detail", ReportParamMap.RM0307, "Y", false, false, 1000, 2),
	RM0308(999, "300", "Accounts Payable", "Supplier Ledger Summary", ReportParamMap.RM0308, "Y", false, false, 1000, 2),
	RM0309(999, "300", "Accounts Payable", "Supplier Payment Listing", ReportParamMap.RM0309, "Y", false, false, 1000, 2),
	RM0311(999, "300", "Accounts Payable", "Supplier List", ReportParamMap.RM0311, "Y", false, false, 1000, 2),

	// Sales and Invoice
	RM0401(999, "400", "Sales & Invoicing", "Sales Order Listing", ReportParamMap.RM0401, "Y", false, false, 1000, 3),
	RM0402(999, "400", "Sales & Invoicing", "Invoice Listing Detail Report", ReportParamMap.RM0402, "Y", false, false, 1000, 3),
	RM0403(999, "400", "Sales & Invoicing", "Sales Listing Summary", ReportParamMap.RM0403, "Y", false, false, 1000, 3),
	RM0407(999, "400", "Sales & Invoicing", "Sales Requisition Report", ReportParamMap.RM0407, "Y", false, false, 1000, 3),
	RM0410(999, "400", "Sales & Invoicing", "Sales Return Listing", ReportParamMap.RM0410, "Y", false, false, 1000, 3),
	RM0411(999, "400", "Sales & Invoicing", "Requisition Report", ReportParamMap.RM0411, "Y", false, false, 1000,3),

	// Accounts Receivable
	RM0314(999, "300", "Accounts Receivable", "Customer/Member/Director List", ReportParamMap.RM0314, "Y", false, false, 1000, 4),
	RM0404(999, "400", "Accounts Receivable", "Money Receipt Listing", ReportParamMap.RM0404, "Y", false, false, 1000, 4),
	RM0405(999, "400", "Accounts Receivable", "Customer Ledger Detail", ReportParamMap.RM0405, "Y", false, false, 1000, 4),
	RM0406(999, "400", "Accounts Receivable", "Customer Ledger Summary", ReportParamMap.RM0406, "Y", false, false, 1000, 4),

	// Inventory
	RM0601(999, "600", "Inventory", "Current Stock", ReportParamMap.RM0601, "Y", false, false, 1000, 5),
	RM0602(999, "600", "Inventory", "Date Wise Stock Status", ReportParamMap.RM0602, "Y", false, false, 1000, 5),
	RM0603(999, "600", "Inventory", "Stock Low Status", ReportParamMap.RM0603, "Y", false, false, 1000, 5),
	RM0605(999, "600", "Inventory", "Stock Movement Report", ReportParamMap.RM0605, "Y", false, false, 1000, 5),
	RM0606(999, "600", "Inventory", "Item Transaction Summary", ReportParamMap.RM0606, "Y", false, false, 1000, 5),
	RM0607(999, "600", "Inventory", "Item List", ReportParamMap.RM0607, "Y", false, false, 1000, 5),
	RM0608(999, "600", "Inventory", "Inventory Management Report", ReportParamMap.RM0608, "Y", false, false, 1000, 6),
	RM0609(999, "600", "Inventory", "Employee Listing Report", ReportParamMap.RM0609, "Y", false, false, 1000, 6),
	RM0610(999, "600", "Inventory", "User Listing Report", ReportParamMap.RM0610, "Y", false, false, 1000, 6),
	RM0611(999, "600", "Inventory", "Transfer order Listing Report", ReportParamMap.RM0611, "Y", false, false, 1000, 5),
	//RM0306(999, "600", "Inventory", "Branch Wise Asset Transfer Listing Report", ReportParamMap.RM0306, "Y", false, false, 1000, 5),

	// General Accounts
	RM0801(999, "800", "General Accounts", "Balance Sheet Report", ReportParamMap.RM0801, "Y", false, false, 1000,7),
	//RM0802(999, "800", "General Accounts", "Print Voucher Report", ReportParamMap.RM0802, "Y", false, false, 1000,7),
	RM0803(999, "800", "General Accounts", "Profit And Lost Report", ReportParamMap.RM0803, "Y", false, false, 1000,7),
	RM0804(999, "800", "General Accounts", "Cash/Bank Book Report", ReportParamMap.RM0804, "Y", false, false, 1000,7),
	RM0805(999, "800", "General Accounts", "Date Wise Account Ledger Report", ReportParamMap.RM0805, "Y", false, false, 1000,7),
	RM0806(999, "800", "General Accounts", "Date Wise Trial Balance Report", ReportParamMap.RM0806, "Y", false, false, 1000,7),
	RM0807(999, "800", "General Accounts", "Trial Balance By Group Report", ReportParamMap.RM0807, "Y", false, false, 1000,7),
	RM0808(999, "800", "General Accounts", "General Journal", ReportParamMap.RM0808, "Y", false, false, 1000,7),
	RM0809(999, "800", "General Accounts", "Chart of Account Summary Report", ReportParamMap.RM0809, "Y", false, false, 1000,7),
	RM0810(999, "800", "General Accounts", "Chart of Account Details Report", ReportParamMap.RM0810, "Y", false, false, 1000,7),


	// Production
	RM0501(999, "500", "Production", "BOM List", ReportParamMap.RM0501, "Y", false, false, 1000, 6),
	RM0502(999, "500", "Production", "Batch wise production", ReportParamMap.RM0502, "Y", false, false, 1000, 6),
	RM0503(999, "500", "Production", "Batch wise material consumptions", ReportParamMap.RM0503, "Y", false, false, 1000, 6),

	// Land Report
	RM0700(999, "700", "Land Management", "Print Land Info", ReportParamMap.RM0700, "Y", false, false, 1000,8),
	RM0701(999, "700", "Land Management", "Land Details Report", ReportParamMap.RM0701, "Y", false, false, 1000,8),
	RM0702(999, "700", "Land Management", "Land Information Report", ReportParamMap.RM0702, "Y", false, false, 1000,8),
	RM0703(999, "700", "Land Management", "Sales Listing Detail", ReportParamMap.RM0703, "Y", false, false, 1000, 8),
	RM0704(999, "700", "Land Management", "Sales Listing Summary", ReportParamMap.RM0704, "Y", false, false, 1000, 8),
	RM0705(999, "700", "Land Management", "Dag Wise Land Information Details", ReportParamMap.RM0705, "Y", false, false, 1000,8),
	RM0706(999, "700", "Land Management", "Member Wise Land Information Details", ReportParamMap.RM0706, "Y", false, false, 1000,8),
	RM0707(999, "700", "Land Management", "Money Receipt Listing (Land)", ReportParamMap.RM0707, "Y", false, false, 1000,8),
	RM0708(999, "700", "Land Management", "Receivable And Collection Statement Report", ReportParamMap.RM0708, "Y", false, false, 1000,8),

	// Project Management
	RM0901(999, "900", "Project Management", "Site Budget and Actual Cost/Expense Report", ReportParamMap.RM0901, "Y", false, false, 1000,9),
	RM0902(999, "900", "Project Management", "Item Consumption  Report", ReportParamMap.RM0902, "Y", false, false, 1000,9);

	private int seqn;
	private String group;
	private String groupName;
	private String description;
	private Map<String, String> paramMap;
	private String defaultAccess;
	private boolean fopEnabled;
	private boolean chunkDownload;
	private int chunkLimit;
	private int groupseqn;

	private ReportMenu(int seqn, String group, String groupName, String des, Map<String, String> paramMap, String defaultAccess, boolean fopEnabled, boolean chunkDownload, int chunkLimit, int groupseqn) {
		this.seqn = seqn;
		this.group = group;
		this.groupName = groupName;
		this.description = des;
		this.paramMap = paramMap;
		this.defaultAccess = defaultAccess;
		this.fopEnabled = fopEnabled;
		this.chunkDownload = chunkDownload;
		this.chunkLimit = chunkLimit;
		this.groupseqn = groupseqn;
	}

	public int getSeqn() {
		return this.seqn;
	}

	public String getGroup() {
		return this.group;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public String getDescription() {
		return this.description;
	}

	public Map<String, String> getParamMap() {
		return this.paramMap;
	}

	public String getDefaultAccess() {
		return this.defaultAccess;
	}

	public boolean isFopEnabled() {
		return this.fopEnabled;
	}

	public boolean isChunkDownload() {
		return this.chunkDownload;
	}

	public int getChunkLimit() {
		return this.chunkLimit;
	}
	
	public int getGroupseqn() {
		return this.groupseqn;
	}
}
