package com.asl.enums;

import java.util.Map;

import com.asl.model.ReportParamMap;

/**
 * @author Zubayer Ahamed
 * @since Jan 5, 2021
 */
public enum MenuProfile {

	M0100(9999, "M0100", "Administration", "Administration", "Y", "module/M0100", 1, "MASTER","ROLE_ADMIN,ROLE_SYSTEM_ADMIN", true, false, null, false, false,1000),
		M0101(9999, "M0100", "Administration", "Users", "Y", "module/M0100", 1, "SUB-MASTER", "", true, false, null, false, false,1000),
			M0102(9999, "M0101", "Administration", "Manage Users", "Y", "system/xusers", 1, "MENU", "", true, false, null, false, false,1000),
			M0103(9999, "M0101", "Administration", "Manage Access Profile", "Y", "system/profile", 1, "MENU", "", true, false, null, false, false,1000),
			M0104(9999, "M0101", "Administration", "Profile Allocation to Users", "Y", "system/profileallocation", 1, "MENU", "", true, false, null, false, false,1000),
		M0105(9999, "M0100", "Administration", "System Administration", "Y", "module/M0100", 1, "SUB-MASTER", "", true, false, null, false, false,1000),
			M0106(9999, "M0105", "Administration", "Codes & Parameters", "Y", "mastersetup/cap", 1, "MENU", "", true, false, null, false, false,1000),
			M0107(9999, "M0105", "Administration", "Transaction Codes", "Y", "mastersetup/xtrn", 1, "MENU", "", true, false, null, false, false,1000),
			M0119(9999, "M0105", "Administration", "Related Transaction Codes", "Y", "mastersetup/xtrnp", 1, "MENU", "", true, false, null, false, false,1000),
			M0108(9999, "M0105", "Administration", "List Configuration", "Y", "system/list", 1, "MENU", "ROLE_SYSTEM_ADMIN", true, false, null, false, false,1000),
			M0109(9999, "M0105", "Administration", "Business Profile", "Y", "system/businessprof", 1, "MENU", "", true, false, null, false, false,1000),
		M0110(9999, "M0100", "Administration", "Logs", "Y", "module/M0100", 1, "SUB-MASTER", "ROLE_SYSTEM_ADMIN", true, false, null, false, false,1000),
			M0111(9999, "M0110", "Administration", "Error Logs", "Y", "system/aslprocerror", 1, "MENU", "ROLE_SYSTEM_ADMIN", true, false, null, false, false,1000),
		M0112(9999, "M0100", "Administration", "PO to GL Interface", "Y", "system/potoapi", 1, "MENU", "", true, false, null, false, false,1000),
		M0113(9999, "M0100", "Administration", "OP to GL Interface", "Y", "system/optoari", 1, "MENU", "", true, false, null, false, false,1000),
		M0114(9999, "M0100", "Administration", "IM to GL Interface", "Y", "system/imtogli", 1, "MENU", "", true, false, null, false, false,1000),
		M0115(9999, "M0100", "Administration", "Year End Processing", "Y", "account/acyearend", 1, "MENU", "", true, false, null, false, false,1000),
		M0116(9999, "M0100", "Administration", "PD Transfer Types", "Y", "system/pdtranstype", 1, "MENU", "", true, false, null, false, false,1000),
		M0117(9999, "M0100", "Administration", "GL Interfaces", "Y", "module/M0100", 1, "SUB-MASTER", "", true, false, null, false, false,1000),

	M0200(9999, "M0200", "Master Setup", "Master Setup", "Y", "module/M0200", 2, "MASTER", "", true, false, null, false, false,1000),
		M0201(9999, "M0200", "Master Setup", "Item Master", "Y", "mastersetup/caitem", 2, "MENU", "", true, false, null, false, false,1000),
		M0202(9999, "M0200", "Master Setup", "Supplier Master", "Y", "mastersetup/cacus?cacusType=SUP", 2, "MENU", "", true, false, null, false, false,1000),
		M0203(9999, "M0200", "Master Setup", "Customer Master", "Y", "mastersetup/cacus?cacusType=CUS", 2, "MENU", "", true, false, null, false, false,1000),
		M0204(9999, "M0200", "Master Setup", "Employee Information", "Y", "employeeinfo", 2, "MENU", "", true, false, null, false, false,1000),
		M0205(9999, "M0200", "Master Setup", "BOM/Recipe Master", "Y", "production/bom", 2, "MENU", "", true, false, null, false, false,1000),
		M0206(9999, "M0200", "Master Setup", "Bank Entry", "Y", "account/bankentry", 2, "MENU", "", true, false, null, false, false,1000),
		M0207(9999, "M0200", "Master Setup", "Default Terms & Conditions", "Y", "mastersetup/termsdef", 2, "MENU", "", true, false, null, false, false,1000),
		M0208(9999, "M0200", "Master Setup", "Dag Master", "Y", "cadag", 1, "MENU", "", true, false, null, false, false,1000),

	M0300(9999, "M0300", "Procurement", "Procurement", "Y", "module/M0300", 3, "MASTER", "", true, true, null, false, false,1000),
		M0301(9999, "M0300", "Procurement", "Purchase Order", "Y", "procurements/poord/poordlist", 3, "MENU", "", true, false, null, false, false,1000),
		M0302(9999, "M0300", "Procurement", "Goods Receipt Note", "Y", "procurements/pogrn/pogrnlist", 3, "MENU", "", true, false, null, false, false,1000),
		M0303(9999, "M0300", "Procurement", "Purchase Return", "Y", "procurements/purchasereturn", 3, "MENU", "", true, false, null, false, false,1000),
		//M0304(9999, "M0300", "Procurement", "Raw materials requisition", "Y", "procurement/requisition?reqtype=Raw Materials", 3, "MENU", "", true, false, null, false, false,1000),
		M0305(9999, "M0300", "Procurement", "Packaging Item Requisition", "Y", "procurement/requisition?reqtype=Packaging Item", 3, "MENU", "", true, false, null, false, false,1000),
		M0306(9999, "M0300", "Procurement", "All Order Requisitions", "Y", "procurement/requisition/allrequisition", 3, "MENU", "", true, false, null, false, false,1000),
		M0307(9999, "M0300", "Procurement", "Direct Purchase", "Y", "procurements/pogrndirect", 3, "MENU", "", true, false, null, false, false,1000),
		M0308(9999, "M0300", "Procurement", "Accessories Requisition", "Y", "procurement/requisition?reqtype=Accessories", 3, "MENU", "", true, false, null, false, false,1000),
		M0309(9999, "M0300", "Procurement", "Requisition", "Y", "salesninvoice/opreqcus", 3, "MENU", "", true, false, null, false, false,1000),
		M0310(9999, "M0300", "Procurement", "Requisition", "Y", "salesninvoice/opreq", 3, "MENU", "", true, false, null, false, false,1000),
		M0311(9999, "M0300", "Procurement", "Requisition History", "Y", "salesninvoice/opreqcus/requisitionhistory", 3, "MENU", "", false, true, null, false, false,1000),
		M0312(9999, "M0300", "Procurement", "Raw materials requisition", "Y", "salesninvoice/opreqcus?reqtype=Raw Materials", 3, "MENU", "", false, true, null, false, false,1000),
		M0313(9999, "M0300", "Procurement", "Packaging Item Requisition", "Y", "salesninvoice/opreqcus?reqtype=Packaging Item", 3, "MENU", "", false, true, null, false, false,1000),
		M0314(9999, "M0300", "Procurement", "Accessories Requisition", "Y", "salesninvoice/opreqcus?reqtype=Accessories", 3, "MENU", "", false, true, null, false, false,1000),
		M0315(9999, "M0300", "Procurement", "Services", "Y", "module/M0300", 3,"SUB-MASTER", "", true, false, null, false, false,1000), 
			M0316(9999, "M0315", "Procurement","Service Requisition", "Y", "procurement/poreqheader", 3, "MENU", "", true, false, null, false, false,1000),
			M0317(9999, "M0315", "Procurement","Service Requisition Approval", "Y", "procurement/poreqapprove", 3, "MENU", "", true, false, null, false, false,1000),
			M0318(9999, "M0315", "Procurement","Work Order", "Y", "procurement/workorder", 3, "MENU", "", true, false, null, false, false,1000),
			M0319(9999, "M0315", "Procurement","Work Order Approval", "Y", "procurement/woapproval", 3, "MENU", "", true, false, null, false, false,1000),
			M0320(9999, "M0315", "Procurement","Quality Certificate", "Y", "procurement/qcheader", 3, "MENU", "", true, false, null, false, false,1000),
		M0321(9999, "M0300", "Procurement", "Purchase Requisition", "Y", "procurement/purchasereq/purchasereqlist", 3, "MENU", "", true, false, null, false, false,1000),
		M0322(9999, "M0300", "Procurement", "Test", "Y", "test", 3, "MENU", "", false, true, null, false, false,1000),
		//Report
		RM0300(9999, "M0300", "Procurement", "Report", "Y", "module/M0300", 1, "SUB-MASTER", "", true, false, null, false, false,1000),
			RM0301(9999, "RM0300", "Procurement", "Purchase Order Listing", "Y", "report/RM0301", 3, "MENU", "", true, false, ReportParamMap.RM0301, false, true,1000),
			RM0305(9999, "RM0300", "Procurement", "GRN Listing", "Y", "report/RM0305", 3, "MENU", "", true, false, ReportParamMap.RM0305, false, true,1000),
			RM0306(9999, "RM0300", "Procurement", "Purchase Listing Summary", "Y", "report/RM0306", 3, "MENU", "", true, false, ReportParamMap.RM0306, false, true,1000),
			RM0312(9999, "RM0300", "Procurement", "Purchase Return Listing", "Y", "report/RM0312", 3, "MENU", "", true, false, ReportParamMap.RM0312, false, true,1000),
			RM0313(9999, "RM0300", "Procurement", "Purchase Requisition Listing", "Y", "report/RM0313", 3, "MENU", "", true, false, ReportParamMap.RM0313, false, true,1000),

	M0400(9999, "400", "Sales & Invoice", "Sales & Invoice", "Y","module/M0400", 4, "MASTER", "", false, true, null, false, false,1000),
		M0401(9999, "M0400", "Sales & Invoice", "Branch Requisition", "Y", "module/M0400", 4, "SUB-MASTER", "", false, true, null, false, false,1000),
			M0402(9999, "M0401", "Sales & Invoice", "Requisitions Matrix", "Y","purchasing/bqls/details", 4, "MENU", "", false, true, null, false, false,1000),
			M0403(9999, "M0401", "Sales & Invoice", "Requisitions to Sales Order", "Y","purchasing/bqls", 4, "MENU", "", false, true, null, false, false,1000),
			M0412(9999, "M0401", "Sales & Invoice", "Open Requisitions of Branch", "Y","purchasing/bqls/allopenreq", 4, "MENU", "", false, true, null, false, false,1000),
		M0404(9999, "M0400", "Sales & Invoice", "Sales Order", "Y","salesninvoice/opord", 4, "MENU", "", false, true, null, false, false,1000),
		M0405(9999, "M0400", "Sales & Invoice", "Sales Orders Matrix", "Y","salesninvoice/salesorder/detailmatrix", 4, "MENU", "", false, true, null, false, false,1000),
		M0406(9999, "M0400", "Sales & Invoice", "Invoice Entry", "Y","salesninvoice/salesandinvoice", 4, "MENU", "", false, true, null, false, false,1000),
		M0407(9999, "M0400", "Sales & Invoice", "Delivery Chalan (Group)", "Y","salesninvoice/deliveryorderchalan", 4, "MENU", "", false, true, null, false, false,1000),
		M0408(9999, "M0400", "Sales & Invoice", "Sales Return", "Y","salesninvoice/salesreturn", 4, "MENU", "", false, true, null, false, false,1000),
		M0409(9999, "M0400", "Sales & Invoice", "Report", "Y", "module/M0400", 4, "SUB-MASTER", "", false, true, null, false, false,1000),
			M0410(9999, "M0409", "Sales & Invoice", "Daily Distribution Report", "Y", "salesninvoice/ddisr", 4, "MENU", "", false, true, null, false, false,1000),
		M0411(9999, "M0400", "Sales & Invoice", "Sales Entry", "Y","salesninvoice/directsales", 4, "MENU", "", false, true, null, false, false,1000),
		M0413(9999, "M0400", "Sales & Invoice", "Sales Requisition Order", "Y","salesninvoice/opreq", 4, "MENU", "", false, true, null, false, false,1000),
		//Report
		RM0400(9999, "M0400", "Sales & Invoice", "Report", "Y", "module/M0400", 1, "SUB-MASTER", "", true, false, null, false, false,1000),
			RM0401(9999, "RM0400", "Sales & Invoice", "Sales Order Listing", "Y", "report/RM0401", 4, "MENU", "", true, false, ReportParamMap.RM0401, false, true,1000),
			RM0402(9999, "RM0400", "Sales & Invoice", "Invoice Listing Detail Report", "Y", "report/RM0402", 4, "MENU", "", true, false, ReportParamMap.RM0402, false, true,1000),
			RM0403(9999, "RM0400", "Sales & Invoice", "Sales Listing Summary", "Y", "report/RM0403", 4, "MENU", "", true, false, ReportParamMap.RM0403, false, true,1000),
			RM0407(9999, "RM0400", "Sales & Invoice", "Sales Requisition Report", "Y", "report/RM0407", 4, "MENU", "", true, false, ReportParamMap.RM0407, false, true,1000),
			RM0410(9999, "RM0400", "Sales & Invoice", "Sales Return Listing", "Y", "report/RM0410", 4, "MENU", "", true, false, ReportParamMap.RM0410, false, true,1000),
			RM0411(9999, "RM0400", "Sales & Invoice", "Requisition Report", "Y","report/RM0411", 4, "MENU", "", true, false, ReportParamMap.RM0411, false, false, 1000),
		//Branch Activities Report
		RM0420(9999, "M0400", "Sales & Invoice", "Branch Activities Report", "Y", "module/M0400", 1, "SUB-MASTER", "", true, false, null, false, false,1000),
			RM0421(9999, "RM0420", "Sales & Invoice", "Requisition Listing Report", "Y", "report/RM0421", 4, "MENU", "", true, false, ReportParamMap.RM0421, false, true,1000),
			RM0422(9999, "RM0420", "Sales & Invoice", "Sales Listing Report", "Y", "report/RM0422", 4, "MENU", "", true, false, ReportParamMap.RM0422, false, true,1000),
			RM0423(9999, "RM0420", "Sales & Invoice", "Customer Ledger Details Report", "Y", "report/RM0423", 4, "MENU", "", true, false, ReportParamMap.RM0423, false, true,1000),
			RM0424(9999, "RM0420", "Sales & Invoice", "Assest Transfer Listing Report", "Y", "report/RM0424", 4, "MENU", "", true, false, ReportParamMap.RM0424, false, true,1000),
			RM0425(9999, "RM0420", "Sales & Invoice", "Sales Return Listing Report", "Y", "report/RM0425", 4, "MENU", "", true, false, ReportParamMap.RM0425, false, true,1000),
			RM0426(9999, "RM0420", "Sales & Invoice", "Requisition Report", "Y", "report/RM0426", 4, "MENU", "", true, false, ReportParamMap.RM0426, false, true,1000),

	M0500(9999, "M0500", "Production", "Production", "Y","module/M0500", 5, "MASTER", "", false, true, null, false, false,1000),
		M0502(9999, "M0500", "Production", "Production Planning", "Y","production/suggestion",  5, "MENU", "", false, true, null, false, false,1000),
		M0503(9999, "M0500", "Production", "Production Process", "Y","production/batch", 5, "MENU", "", false, true, null, false, false,1000),
		M0504(9999, "M0500", "Production", "Production Chalan", "Y","salesninvoice/salesorderchalan", 5, "MENU", "", false, true, null, false, false,1000),
		M0505(9999, "M0500", "Production", "Sales Order to Production Chalan", "Y","production/sotoprodchalan", 5, "MENU", "", false, true, null, false, false,1000),
		M0506(9999, "M0500", "Production", "List of Production Chalan", "Y","salesninvoice/chalanlist", 5, "MENU", "", false, true, null, false, false,1000),
		M0507(9999, "M0500", "Production", "BOM Entry", "Y", "production/mobomheader", 2, "MENU", "", true, false, null, false, false,1000),
		
		//Report
		RM0500(9999, "M0500", "Production", "Report", "Y", "module/M0500", 1, "SUB-MASTER", "", true, false, null, false, false,1000),
			RM0501(9999, "RM0500", "Production", "BOM List", "Y", "report/RM0501", 4, "MENU", "", true, false, ReportParamMap.RM0501, false, true,1000),
			RM0502(9999, "RM0500", "Production", "Batch wise production", "Y", "report/RM0502", 4, "MENU", "", true, false, ReportParamMap.RM0502, false, true,1000),
			RM0503(9999, "RM0500", "Production", "Batch wise material consumptions", "Y", "report/RM0503", 4, "MENU", "", true, false, ReportParamMap.RM0503, false, true,1000),

	M0600(9999, "M0600", "Inventory", "Inventory", "Y","module/M0600", 6, "MASTER", "", false, true, null, false, false,1000),
		M0601(9999, "M0600", "Inventory", "Stock Opening Entry", "Y","inventory/openingentry", 6, "MENU", "", false, true, null, false, false,1000),
		M0602(9999, "M0600", "Inventory", "Stock Take & Adjustment", "Y","inventory/stocktake", 6, "MENU", "", false, true, null, false, false,1000),
		M0603(9999, "M0600", "Inventory", "Store Transfer Order", "Y","inventory/transferorder", 6, "MENU", "", false, true, null, false, false,1000),
		M0604(9999, "M0600", "Inventory", "Asset Transfer to Branch", "Y","inventory/asset", 6, "MENU", "", false, true, null, false, false,1000),
		M0605(9999, "M0600", "Inventory", "Stock List", "Y","inventory/stocklist", 6, "MENU", "", false, true, null, false, false,1000),
		M0606(9999, "M0600", "Inventory", "Store Requisition", "Y","inventory/storereq", 6, "MENU", "", false, true, null, false, false,1000),
		M0607(9999, "M0600", "Inventory", "Approve Store Requisition", "Y","inventory/approvereq", 6, "MENU", "", false, true, null, false, false,1000),
		M0608(9999, "M0600", "Inventory", "Store Acknowledgement", "Y","inventory/storeacknow", 6, "MENU", "", false, true, null, false, false,1000),
		M0609(9999, "M0600", "Inventory", "Damage/Issue Entry", "Y","inventory/issueentry", 6, "MENU", "", false, true, null, false, false,1000),
		M0610(9999, "M0600", "Inventory", "Requisition From Store To Supply Chain History", "Y","inventory/storereqtables/openreq", 6, "MENU", "", false, true, null, false, false,1000),
		M0611(9999, "M0600", "Inventory", "Waiting For Approve Requisitions History", "Y","inventory/storereqtables/approval", 6, "MENU", "", false, true, null, false, false,1000),
		M0612(9999, "M0600", "Inventory", "Pending For Acknowledgment Requisitions History", "Y","inventory/storereqtables/pending", 6, "MENU", "", false, true, null, false, false,1000),
		M0613(9999, "M0600", "Inventory", "Rejected Requisitions History", "Y","inventory/storereqtables/rejected", 6, "MENU", "", false, true, null, false, false,1000),
		M0614(9999, "M0600", "Inventory", "Disbursed Requisitions History", "Y","inventory/storereqtables/disbursed", 6, "MENU", "", false, true, null, false, false,1000),
		M0615(9999, "M0600", "Inventory", "Entire Project Transfer", "Y","inventory/projecttransfer", 6, "MENU", "", false, true, null, false, false,1000),
		
		//Report
		RM0600(9999, "M0600", "Inventory", "Report", "Y", "module/M0600", 1, "SUB-MASTER", "", true, false, null, false, false,1000),
			RM0601(9999, "RM0600", "Inventory", "Current Stock", "Y", "report/RM0601", 6, "MENU", "", true, false, ReportParamMap.RM0601, false, true,1000),
			RM0602(9999, "RM0600", "Inventory", "Date Wise Stock Status", "Y", "report/RM0602", 6, "MENU", "", true, false, ReportParamMap.RM0602, false, true,1000),
			RM0603(9999, "RM0600", "Inventory", "Stock Low Status", "Y", "report/RM0603", 6, "MENU", "", true, false, ReportParamMap.RM0603, false, true,1000),
			RM0605(9999, "RM0600", "Inventory", "Stock Movement Report", "Y", "report/RM0605", 6, "MENU", "", true, false, ReportParamMap.RM0605, false, true,1000),
			RM0606(9999, "RM0600", "Inventory", "Item Transaction Summary", "Y", "report/RM0606", 6, "MENU", "", true, false, ReportParamMap.RM0606, false, true,1000),
			RM0607(9999, "RM0600", "Inventory", "Item List", "Y", "report/RM0607", 6, "MENU", "", true, false, ReportParamMap.RM0607, false, true,1000),
			RM0608(9999, "RM0600", "Inventory", "Inventory Management Report", "Y", "report/RM0608", 6, "MENU", "", true, false, ReportParamMap.RM0608, false, true,1000),
			RM0609(9999, "RM0600", "Inventory", "Employee Listing Report", "Y", "report/RM0609", 6, "MENU", "", true, false, ReportParamMap.RM0609, false, true,1000),
			RM0610(9999, "RM0600", "Inventory", "User Listing Report", "Y", "report/RM0610", 6, "MENU", "", true, false, ReportParamMap.RM0610, false, true,1000),
			RM0611(9999, "RM0600", "Inventory", "Transfer order Listing Report", "Y", "report/RM0611", 6, "MENU", "", true, false, ReportParamMap.RM0611, false, true,1000),

	//M0700(9999, "M0700", "Report", "Report", "Y", "module/M0700", 100, "MASTER", "", false, true, false, null, false, false,1000),

	M0800(9999, "M0800", "Accounts Receivable", "Accounts Receivable", "Y", "module/M0800", 8, "MASTER", "", false, true, null, false, false,1000),
		M0801(9999, "M0800", "Accounts Receivable", "Money Receipt", "Y", "salesninvoice/moneyreceipt", 8, "MENU", "", false, true,null, false, false,1000),
		M0802(9999, "M0800", "Accounts Receivable", "Customer Adjustment", "Y", "cusadjustment", 8, "MENU", "", false, true, null, false, false,1000),
		M0803(9999, "M0800", "Accounts Receivable", "Customer Opening", "Y", "cusopening", 8, "MENU", "", false, true, null, false, false,1000),
		M0804(9999, "M0800", "Accounts Receivable", "Receivable Entry", "Y", "arentry", 8, "MENU", "", false, true, null, false, false,1000),
		//Report
		RM0800(9999, "M0800", "Accounts Receivable", "Report", "Y", "module/M0800", 1, "SUB-MASTER", "", true, false, null, false, false,1000),
			RM0314(9999, "RM0800", "Accounts Receivable", "Customer Listing", "Y", "report/RM0314", 8, "MENU", "", true, false, ReportParamMap.RM0314, false, true,1000),
			RM0404(9999, "RM0800", "Accounts Receivable", "Money Receipt Listing", "Y", "report/RM0404", 8, "MENU", "", true, false, ReportParamMap.RM0404, false, true,1000),
			RM0405(9999, "RM0800", "Accounts Receivable", "Customer Ledger Detail", "Y", "report/RM0405", 8, "MENU", "", true, false, ReportParamMap.RM0405, false, true,1000),
			RM0406(9999, "RM0800", "Accounts Receivable", "Customer Ledger Summary", "Y", "report/RM0406", 8, "MENU", "", true, false, ReportParamMap.RM0406, false, true,1000),

	M0900(9999, "M0900", "Accounts Payable", "Accounts Payable", "Y", "module/M0900", 9, "MASTER", "", false, true, null, false, false,1000),
		M0901(9999, "M0900", "Accounts Payable", "Supplier Payment", "Y", "purchasing/supplierpayment", 9, "MENU", "", false, true, null, false, false,1000),
		M0902(9999, "M0900", "Accounts Payable", "Supplier Adjustment", "Y", "supplieradjustment", 9, "MENU", "", false, true, null, false, false,1000),
		M0903(9999, "M0900", "Accounts Payable", "Supplier Opening", "Y", "supopening", 9, "MENU", "", false, true, null, false, false,1000),
		//Report
		RM0900(9999, "M0900", "Accounts Payable", "Report", "Y", "module/M0900", 1, "SUB-MASTER", "", true, false, null, false, false,1000),
			RM0307(9999, "RM0900", "Accounts Payable", "Supplier Ledger Detail", "Y", "report/RM0307", 9, "MENU", "", true, false, ReportParamMap.RM0307, false, true,1000),
			RM0308(9999, "RM0900", "Accounts Payable", "Supplier Ledger Summary", "Y", "report/RM0308", 9, "MENU", "", true, false, ReportParamMap.RM0308, false, true,1000),
			RM0309(9999, "RM0900", "Accounts Payable", "Supplier Payment Listing", "Y", "report/RM0309", 9, "MENU", "", true, false, ReportParamMap.RM0309, false, true,1000),
			RM0311(9999, "RM0900", "Accounts Payable", "Supplier List", "Y", "report/RM0311", 9, "MENU", "", true, false, ReportParamMap.RM0311, false, true,1000),

	M1000(9999, "M1000", "Accounting", "Accounting", "Y", "module/M1000", 10, "MASTER", "", false, true, null, false, false,1000),
		M1001(9999, "M1000", "Accounting", "Account Default", "Y", "account/accdefault", 10, "MENU", "", false, true, null, false, false,1000),
		M1002(9999, "M1000", "Accounting", "Account Group", "Y", "account/acgroup?level=1", 10, "MENU", "", false, true, null, false, false,1000),
		M1003(9999, "M1000", "Accounting", "Chart Of Account", "Y", "account/coa", 10, "MENU", "", false, true, null, false, false,1000),
		M1004(9999, "M1000", "Accounting", "Sub Account", "Y", "account/acsub", 10, "MENU", "", false, true, null, false, false,1000),
		M1005(9999, "M1000", "Accounting", "Voucher Entry", "Y", "account/voucher", 10, "MENU", "", false, true, null, false, false,1000),
		M1006(9999, "M1000", "Accounting", "Year End Processing", "Y", "system/acyearend", 10, "MENU", "", false, true, null, false, false,1000),
		M1007(9999, "M1000", "Accounting", "Inventory Consumption Process", "Y", "inventory/consumption", 10, "MENU", "", false, true, null, false, false,1000),
		M1008(9999, "M1000", "Accounting", "GL Interfaces", "Y", "module/M1000", 10,"SUB-MASTER", "", true, false, null, false, false,1000), 
			M1009(9999, "M1001", "Accounting","PO to GL Interface", "Y", "system/potoapi", 10, "MENU", "", true, false, null, false, false,1000),
			M1010(9999, "M1001", "Accounting","OP to GL Interface", "Y", "system/optoari", 10, "MENU", "", true, false, null, false, false,1000),
			M1011(9999, "M1001", "Accounting","IM to GL Interface", "Y", "system/imtogli", 10, "MENU", "", true, false, null, false, false,1000),
			M1014(9999, "M1001", "Accounting","IMPRJ to GL Interface", "Y", "system/imprjtogli", 10, "MENU", "", true, false, null, false, false,1000),
			M1012(9999, "M1001", "Accounting","Customer Group (Accounts)", "Y", "account/cusgroup", 10, "MENU", "", true, false, null, false, false,1000),
			M1013(9999, "M1001", "Accounting","Supplier Group (Accounts)", "Y", "account/supgroup", 10, "MENU", "", true, false, null, false, false,1000),
		M1015(9999, "M1000", "Accounting", "Voucher List", "Y", "account/voucher/voucherslist", 10, "MENU", "", false, true, null, false, false,1000),
		//Report
		RM1000(9999, "M1000", "Accounting", "Report", "Y", "module/M1000", 1, "SUB-MASTER", "", true, false, null, false, false,1000),
			RM0801(9999, "RM1000", "Accounting", "Balance Sheet Report", "Y", "report/RM0801", 10, "MENU", "", true, false, ReportParamMap.RM0801, false, true,1000),
			RM0803(9999, "RM1000", "Accounting", "Profit And Lost Report", "Y", "report/RM0803", 10, "MENU", "", true, false, ReportParamMap.RM0803, false, true,1000),
			RM0804(9999, "RM1000", "Accounting", "Cash/Bank Book Report", "Y", "report/RM0804", 10, "MENU", "", true, false, ReportParamMap.RM0804, false, true,1000),
			RM0805(9999, "RM1000", "Accounting", "Date Wise Account Ledger Report", "Y", "report/RM0805", 10, "MENU", "", true, false, ReportParamMap.RM0805, false, true,1000),
			RM0806(9999, "RM1000", "Accounting", "Date Wise Trial Balance Report", "Y", "report/RM0806", 10, "MENU", "", true, false, ReportParamMap.RM0806, false, true,1000),
			RM0807(9999, "RM1000", "Accounting", "Trial Balance By Group Report", "Y", "report/RM0807", 10, "MENU", "", true, false, ReportParamMap.RM0807, false, true,1000),
			RM0808(9999, "RM1000", "Accounting", "General Journal", "Y", "report/RM0808", 10, "MENU", "", true, false, ReportParamMap.RM0808, false, true,1000),
			RM0809(9999, "RM1000", "Accounting", "Chart of Account Summary Report", "Y", "report/RM0809", 10, "MENU", "", true, false, ReportParamMap.RM0809, false, true,1000),
			RM0810(9999, "RM1000", "Accounting", "Chart of Account Details Report", "Y", "report/RM0810", 10, "MENU", "", true, false, ReportParamMap.RM0810, false, true,1000),
		

	M1100(9999, "M1100", "Human Capital Management", "Human Capital Management", "Y","module/M1100", 11, "MASTER", "", false, true, null, false, false,1000),
		M1101(9999, "M1100", "Human Capital Management", "HRD", "Y", "module/M1100", 11, "SUB-MASTER", "", false, true, null, false, false,1000),
			M1102(9999, "M1101", "Human Capital Management", "Employee Information", "Y","hrpersonal", 11, "MENU", "", false, true, null, false, false,1000),
		M1103(9999, "M1100", "Human Capital Management", "Payroll", "Y", "module/M1100", 11, "SUB-MASTER", "", false, true, null, false, false,1000),
			M1104(9999, "M1102", "Human Capital Management", "Employee Information", "Y","paypersonal", 11, "MENU", "", false, true, null, false, false,1000),
			M1105(9999, "M1102", "Human Capital Management", "PD Transaction Types", "Y","system/pdtranstype", 11, "MENU", "", false, true, null, false, false,1000),
			M1106(9999, "M1102", "Human Capital Management", "Loans & Advance Entry", "Y","payroll/pdloantrn", 11, "MENU", "", false, true, null, false, false,1000),
			M1107(9999, "M1102", "Human Capital Management", "Bonus Process", "Y","paypersonal", 11, "MENU", "", false, true, null, false, false,1000),
			M1108(9999, "M1102", "Human Capital Management", "PD Default Setting", "Y","paypersonal", 11, "MENU", "", false, true, null, false, false,1000),
			M1109(9999, "M1102", "Human Capital Management", "Salary Process", "Y","paypersonal", 11, "MENU", "", false, true, null, false, false,1000),
			//Report
			RM1100(9999, "M1100", "Human Capital Management", "Report", "Y", "module/M1100", 1, "SUB-MASTER", "", true, false, null, false, false,1000),

	M1200(9999, "M1200", "Land Management", "Land Management", "Y", "module/M1200", 12, "MASTER", "", false, true, null, false, false,1000),
		M1201(9999, "M1200", "Land Management", "Personal Information", "Y", "landperson", 12, "MENU", "", false, true, null, false, false,1000),
		M1202(9999, "M1200", "Land Management", "Member/Director Information", "Y", "cacus", 12, "MENU", "", false, true, null, false, false,1000),
		M1203(9999, "M1200", "Land Management", "Surveyor Information", "Y", "landsurveyor", 12, "MENU", "", false, true, null, false, false,1000),
		M1204(9999, "M1200", "Land Management", "Land Information", "Y", "landinfo", 12, "MENU", "", false, true, null, false, false,1000),
		M1205(9999, "M1200", "Land Management", "Committee Information", "Y", "landcommitteeinfo", 12, "MENU", "", false, true, null, false, false,1000),
		M1206(9999, "M1200", "Land Management", "Land Events", "Y", "landevents", 12, "MENU", "", false, true, null, false, false,1000),
		M1207(9999, "M1200", "Land Management", "Other Events", "Y", "landotherevents", 12, "MENU", "", false, true, null, false, false,1000),
		//Report
		RM1200(9999, "M1200", "Land Management", "Report", "Y", "module/M1200", 12, "SUB-MASTER", "", true, false, null, false, false,1000),
			RM0701(9999, "RM1200", "Land Management", "Land Details Report", "Y", "report/RM0701", 12, "MENU", "", true, false, ReportParamMap.RM0701, false, true,1000),
			RM0702(9999, "RM1200", "Land Management", "Land Information Report", "Y", "report/RM0702", 12, "MENU", "", true, false, ReportParamMap.RM0702, false, true,1000),
			RM0703(9999, "RM1200", "Land Management", "Sales Listing Detail", "Y", "report/RM0703", 12, "MENU", "", true, false, ReportParamMap.RM0703, false, true,1000),
			RM0704(9999, "RM1200", "Land Management", "Sales Listing Summary", "Y", "report/RM0704", 12, "MENU", "", true, false, ReportParamMap.RM0704, false, true,1000),
			RM0705(9999, "RM1200", "Land Management", "Dag Wise Land Information Details", "Y", "report/RM0705", 12, "MENU", "", true, false, ReportParamMap.RM0705, false, true,1000),
			RM0706(9999, "RM1200", "Land Management", "Member Wise Land Information Details", "Y", "report/RM0706", 12, "MENU", "", true, false, ReportParamMap.RM0706, false, true,1000),
			RM0707(9999, "RM1200", "Land Management", "Money Receipt Listing (Land)", "Y", "report/RM0707", 12, "MENU", "", true, false, ReportParamMap.RM0707, false, true,1000),
			RM0708(9999, "RM1200", "Land Management", "Receivable And Collection Statement Report", "Y", "report/RM0708", 12, "MENU", "", true, false, ReportParamMap.RM0708, false, true,1000),

	M1300(9999, "M1300", "Project Management", "Project Management", "Y", "module/M1300", 13, "MASTER", "", false, true, null, false, false,1000),
		M1301(9999, "M1300", "Project Management", "Project Master", "Y", "project/caproject", 13, "MENU", "", false, true, null, false, false,1000),
		M1302(9999, "M1300", "Project Management", "Site Master", "Y", "project/cawh", 13, "MENU", "", false, true, null, false, false,1000),
		M1303(9999, "M1300", "Project Management", "Budget Entry(Site)", "Y", "project/cawhbudget", 13, "MENU", "", false, true, null, false, false,1000),
		M1304(9999, "M1300", "Project Management", "Item Consumption (Site)", "Y", "project/imissueheader", 13, "MENU", "", false, true, null, false, false,1000),
		M1305(9999, "M1300", "Project Management", "Tasks", "Y", "project/task", 13, "MENU", "", false, true, null, false, false,1000),
		M1306(9999, "M1300", "Project Management", "Issues", "Y", "project/issue", 13, "MENU", "", false, true, null, false, false,1000),
		//Report
		RM1300(9999, "M1300", "Project Management", "Report", "Y", "module/M1300", 1, "SUB-MASTER", "", true, false, null, false, false,1000),
			RM0901(9999, "RM1300", "Project Management", "Site Budget and Actual Cost/Expense Report", "Y", "report/RM0901", 13, "MENU", "", true, false, ReportParamMap.RM0901, false, true,1000),
			RM0902(9999, "RM1300", "Project Management", "Item Consumption Report", "Y", "report/RM0902", 13, "MENU", "", true, false, ReportParamMap.RM0902, false, true,1000),
			RM0903(9999, "RM1300", "Project Management", "Project Details Report", "Y", "report/RM0902", 13, "MENU", "", true, false, ReportParamMap.RM0902, false, true,1000),
			RM0904(9999, "RM1300", "Project Management", "Project Listing Report", "Y", "report/RM0904", 13, "MENU", "", true, false, ReportParamMap.RM0904, false, true,1000),
			RM0905(9999, "RM1300", "Project Management", "Budget Listing Report", "Y", "report/RM0905", 13, "MENU", "", true, false, ReportParamMap.RM0905, false, true,1000),
			RM0906(9999, "RM1300", "Project Management", "Budget Vs Expense Report", "Y", "report/RM0906", 13, "MENU", "", true, false, ReportParamMap.RM0906, false, true,1000),
			RM0907(9999, "RM1300", "Project Management", "Time Sheet Report", "Y", "report/RM0902", 13, "MENU", "", true, false, ReportParamMap.RM0902, false, true,1000),
			RM0908(9999, "RM1300", "Project Management", "Current Stock", "Y", "report/RM0902", 13, "MENU", "", true, false, ReportParamMap.RM0902, false, true,1000),
			RM0909(9999, "RM1300", "Project Management", "Service Requisition Listing", "Y", "report/RM0909", 13, "MENU", "", true, false, ReportParamMap.RM0909, false, true,1000),
			RM0910(9999, "RM1300", "Project Management", "Work Order Listing", "Y", "report/RM0910", 13, "MENU", "", true, false, ReportParamMap.RM0910, false, true,1000),
			RM0911(9999, "RM1300", "Project Management", "Quality Certificate Listing ", "Y", "report/RM0911", 13, "MENU", "", true, false, ReportParamMap.RM0911, false, true,1000),
			RM0912(9999, "RM1300", "Project Management", "QC Listing Summary ", "Y", "report/RM0912", 13, "MENU", "", true, false, ReportParamMap.RM0912, false, true,1000),
	
	M1400(9999, "M1400", "MIS", "MIS", "Y", "module/M1400", 13, "MASTER", "", false, true, null, false, false,1000),
		M1401(9999, "M1400", "MIS", "Notification", "Y", "project/caproject", 13, "MENU", "", false, true, null, false, false,1000),
		//Report
		RM1400(9999, "M1400", "MIS", "Reports", "Y", "module/M1400", 1, "SUB-MASTER", "", true, false, null, false, false,1000);

	private int seqn;
	private String parent;
	private String groupName;
	private String description;
	private String defaultAccess;
	private String menuPath;
	private int groupseqn;
	private String menuType;
	private String roles;
	private boolean centralAccess;
	private boolean branchAccess;
	private Map<String, String> paramMap;
	private boolean fopEnabled;
	private boolean chunkDownload;
	private int chunkLimit;

	private MenuProfile(int seqn, String parent, String groupName, String desc, String defaultAccess, String path, int groupseqn, String menuType, String roles, boolean centralAccess, boolean branchAccess, Map<String, String> paramMap, boolean fopEnabled, boolean chunkDownload, int chunkLimit) {
		this.seqn = seqn;
		this.groupName = groupName;
		this.parent = parent;
		this.description = desc;
		this.defaultAccess = defaultAccess;
		this.menuPath = path;
		this.groupseqn = groupseqn;
		this.menuType = menuType;
		this.roles = roles;
		this.centralAccess = centralAccess;
		this.branchAccess = branchAccess;
		this.paramMap = paramMap;
		this.fopEnabled = fopEnabled;
		this.chunkDownload = chunkDownload;
		this.chunkLimit = chunkLimit;
	}

	public int getSeqn() {
		return seqn;
	}

	public String getGroupName() {
		return groupName;
	}

	public String getParent() {
		return parent;
	}

	public String getDescription() {
		return description;
	}

	public String getDefaultAccess() {
		return defaultAccess;
	}

	public String getMenuPath() {
		return menuPath;
	}

	public int getGroupseqn() {
		return groupseqn;
	}

	public String getMenuType() {
		return menuType;
	}

	public String getRoles() {
		return roles;
	}

	public boolean isCentralAccess() {
		return centralAccess;
	}

	public boolean isBranchAccess() {
		return branchAccess;
	}

	public Map<String, String> getParamMap() {
		return this.paramMap;
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
}
