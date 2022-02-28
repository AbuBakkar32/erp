package com.asl.model;

import java.util.HashMap;
import java.util.Map;

import com.asl.enums.ReportParamDataType;

/**
 * @author Zubayer Ahamed
 * @since Dec 27, 2020
 */
public class ReportParamMap {

	// Procurements  -- M0300
	public static final Map<String, String> RM0101 = new HashMap<>();
	public static final Map<String, String> RM0102 = new HashMap<>();
	public static final Map<String, String> RM0103 = new HashMap<>();
	public static final Map<String, String> RM0104 = new HashMap<>();
	public static final Map<String, String> RM0105 = new HashMap<>();
	public static final Map<String, String> RM0106 = new HashMap<>();
	public static final Map<String, String> RM0301 = new HashMap<>();
	public static final Map<String, String> RM0302 = new HashMap<>();
	public static final Map<String, String> RM0303 = new HashMap<>();
	public static final Map<String, String> RM0304 = new HashMap<>();
	public static final Map<String, String> RM0305 = new HashMap<>();
	public static final Map<String, String> RM0306 = new HashMap<>();
	public static final Map<String, String> RM0307 = new HashMap<>();
	public static final Map<String, String> RM0308 = new HashMap<>();
	public static final Map<String, String> RM0309 = new HashMap<>();
	public static final Map<String, String> RM0310 = new HashMap<>();
	public static final Map<String, String> RM0311 = new HashMap<>();
	public static final Map<String, String> RM0312 = new HashMap<>();
	public static final Map<String, String> RM0313 = new HashMap<>();
	public static final Map<String, String> RM0314 = new HashMap<>();

	// Sales & Invoicing  -- M0400
	public static final Map<String, String> RM0401 = new HashMap<>();
	public static final Map<String, String> RM0402 = new HashMap<>();
	public static final Map<String, String> RM0403 = new HashMap<>();
	public static final Map<String, String> RM0404 = new HashMap<>();
	public static final Map<String, String> RM0405 = new HashMap<>();
	public static final Map<String, String> RM0406 = new HashMap<>();
	public static final Map<String, String> RM0407 = new HashMap<>();
	public static final Map<String, String> RM0408 = new HashMap<>();
	public static final Map<String, String> RM0409 = new HashMap<>();
	public static final Map<String, String> RM0410 = new HashMap<>();
	public static final Map<String, String> RM0411 = new HashMap<>();
	public static final Map<String, String> RM0412 = new HashMap<>();
	public static final Map<String, String> RM0420 = new HashMap<>();
	public static final Map<String, String> RM0421 = new HashMap<>();
	public static final Map<String, String> RM0422 = new HashMap<>();
	public static final Map<String, String> RM0423 = new HashMap<>();
	public static final Map<String, String> RM0424 = new HashMap<>();
	public static final Map<String, String> RM0425 = new HashMap<>();
	public static final Map<String, String> RM0426 = new HashMap<>();

	// Production  -- M0500
	public static final Map<String, String> RM0501 = new HashMap<>();
	public static final Map<String, String> RM0502 = new HashMap<>();
	public static final Map<String, String> RM0503 = new HashMap<>();
	public static final Map<String, String> RM0504 = new HashMap<>();

	// Inventory  -- M0600
	public static final Map<String, String> RM0601 = new HashMap<>();
	public static final Map<String, String> RM0602 = new HashMap<>();
	public static final Map<String, String> RM0603 = new HashMap<>();
	public static final Map<String, String> RM0604 = new HashMap<>();
	public static final Map<String, String> RM0605 = new HashMap<>();
	public static final Map<String, String> RM0606 = new HashMap<>();
	public static final Map<String, String> RM0607 = new HashMap<>();
	public static final Map<String, String> RM0608 = new HashMap<>();
	public static final Map<String, String> RM0609 = new HashMap<>();
	public static final Map<String, String> RM0610 = new HashMap<>();
	public static final Map<String, String> RM0611 = new HashMap<>();
	
	//Land Management
	public static final Map<String, String> RM0700 = new HashMap<>();
	public static final Map<String, String> RM0701 = new HashMap<>();
	public static final Map<String, String> RM0702 = new HashMap<>();
	public static final Map<String, String> RM0703 = new HashMap<>();
	public static final Map<String, String> RM0704 = new HashMap<>();
	public static final Map<String, String> RM0705 = new HashMap<>();
	public static final Map<String, String> RM0706 = new HashMap<>();
	public static final Map<String, String> RM0707 = new HashMap<>();
	public static final Map<String, String> RM0708 = new HashMap<>();
	
	//Account Report
	public static final Map<String, String> RM0801 = new HashMap<>();
	public static final Map<String, String> RM0802 = new HashMap<>();
	public static final Map<String, String> RM0803 = new HashMap<>();
	public static final Map<String, String> RM0804 = new HashMap<>();
	public static final Map<String, String> RM0805 = new HashMap<>();
	public static final Map<String, String> RM0806 = new HashMap<>();
	public static final Map<String, String> RM0807 = new HashMap<>();
	public static final Map<String, String> RM0808 = new HashMap<>();
	public static final Map<String, String> RM0809 = new HashMap<>();
	public static final Map<String, String> RM0810 = new HashMap<>();

	//Project Management
	public static final Map<String, String> RM0901 = new HashMap<>();
	public static final Map<String, String> RM0902 = new HashMap<>();
	public static final Map<String, String> RM0904 = new HashMap<>();
	public static final Map<String, String> RM0905 = new HashMap<>();
	public static final Map<String, String> RM0906 = new HashMap<>();
	public static final Map<String, String> RM0909 = new HashMap<>();
	public static final Map<String, String> RM0910 = new HashMap<>();
	public static final Map<String, String> RM0911 = new HashMap<>();
	public static final Map<String, String> RM0912 = new HashMap<>();


	static {
		
		// For Branch Customer- Start
		RM0421.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0421.put("param2", "FDATE|" + ReportParamDataType.DATE.name());
		RM0421.put("param3", "TDATE|" + ReportParamDataType.DATE.name());
		RM0421.put("param4", "XCUS|" + ReportParamDataType.STRING.name());
		RM0421.put("param5", "XITEM|" + ReportParamDataType.STRING.name());
		RM0421.put("param6", "XSTATUSPOR|" + ReportParamDataType.STRING.name());
		
		RM0422.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0422.put("param2", "FDATE|" + ReportParamDataType.DATE.name());
		RM0422.put("param3", "TDATE|" + ReportParamDataType.DATE.name());
		RM0422.put("param4", "XHWH|" + ReportParamDataType.STRING.name());
		RM0422.put("param5", "XWH|" + ReportParamDataType.STRING.name());
		RM0422.put("param6", "XCUS|" + ReportParamDataType.STRING.name());
		RM0422.put("param7", "XPORNUM|" + ReportParamDataType.STRING.name());
		RM0422.put("param8", "XITEM|" + ReportParamDataType.STRING.name());
		RM0422.put("param9", "XSTATUSPOR|" + ReportParamDataType.STRING.name());
		RM0422.put("param10", "XGITEM|" + ReportParamDataType.STRING.name());
		RM0422.put("param11", "XCATITEM|" + ReportParamDataType.STRING.name());
		
		RM0423.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0423.put("param2", "FromDate|" + ReportParamDataType.DATE.name());
		RM0423.put("param3", "ToDate|" + ReportParamDataType.DATE.name());
		RM0423.put("param4", "Customer|" + ReportParamDataType.STRING.name());
		
		RM0424.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0424.put("param2", "FDATE|" + ReportParamDataType.DATE.name());
		RM0424.put("param3", "TDATE|" + ReportParamDataType.DATE.name());
		RM0424.put("param4", "XCUS|" + ReportParamDataType.STRING.name());
		RM0424.put("param5", "WAREHOUSE|" + ReportParamDataType.STRING.name());
		RM0424.put("param6", "STATUS|" + ReportParamDataType.STRING.name());
		
		RM0425.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0425.put("param2", "From Date|" + ReportParamDataType.DATE.name());
		RM0425.put("param3", "To Date|" + ReportParamDataType.DATE.name());
		RM0425.put("param4", "Xcus|" + ReportParamDataType.STRING.name());
		RM0425.put("param5", "Item Group|" + ReportParamDataType.STRING.name());
		RM0425.put("param6", "Item Code|" + ReportParamDataType.STRING.name());
		RM0425.put("param7", "Status|" + ReportParamDataType.STRING.name());

		
		RM0426.put("param1", "zid|" + ReportParamDataType.STRING.name());
		RM0426.put("param2", "Date|" + ReportParamDataType.DATE.name());
		RM0426.put("param3", "Branch|" + ReportParamDataType.STRING.name());
		// For Branch Customer- End

		RM0301.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0301.put("param2", "FDATE|" + ReportParamDataType.DATE.name());
		RM0301.put("param3", "TDATE|" + ReportParamDataType.DATE.name());
		RM0301.put("param4", "XHWH|" + ReportParamDataType.STRING.name());
		RM0301.put("param5", "XWH|" + ReportParamDataType.STRING.name());
		RM0301.put("param6", "XCUS|" + ReportParamDataType.STRING.name());
		RM0301.put("param7", "XPORNUM|" + ReportParamDataType.STRING.name());
		RM0301.put("param8", "XITEM|" + ReportParamDataType.STRING.name());
		RM0301.put("param9", "XSTATUSPOR|" + ReportParamDataType.STRING.name());
		RM0301.put("param10", "XGITEM|" + ReportParamDataType.STRING.name());
		RM0301.put("param11", "XCATITEM|" + ReportParamDataType.STRING.name());
		
		RM0302.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0302.put("param2", "CHALAN|" + ReportParamDataType.STRING.name());
		RM0302.put("param3", "NAME|" + ReportParamDataType.STRING.name());
		
		RM0303.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0303.put("param2", "XPORNUM|" + ReportParamDataType.STRING.name());
		RM0303.put("param3", "FromDate|" + ReportParamDataType.DATE.name());
		RM0303.put("param4", "ToDate|" + ReportParamDataType.DATE.name());
		
		RM0304.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0304.put("param2", "FDATE|" + ReportParamDataType.DATE.name());
		RM0304.put("param3", "TDATE|" + ReportParamDataType.DATE.name());
		RM0304.put("param4", "XPORNUM|" + ReportParamDataType.STRING.name());

		RM0305.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0305.put("param2", "FDATE|" + ReportParamDataType.DATE.name());
		RM0305.put("param3", "TDATE|" + ReportParamDataType.DATE.name());
		RM0305.put("param4", "XHWH|" + ReportParamDataType.STRING.name());
		RM0305.put("param5", "XWH|" + ReportParamDataType.STRING.name());
		RM0305.put("param6", "XCUS|" + ReportParamDataType.STRING.name());
		RM0305.put("param7", "XPORNUM|" + ReportParamDataType.STRING.name());
		RM0305.put("param8", "XITEM|" + ReportParamDataType.STRING.name());
		RM0305.put("param9", "XSTATUSPOR|" + ReportParamDataType.STRING.name());
		RM0305.put("param10", "XGITEM|" + ReportParamDataType.STRING.name());
		RM0305.put("param11", "XCATITEM|" + ReportParamDataType.STRING.name());
		
		RM0306.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0306.put("param2", "FDATE|" + ReportParamDataType.DATE.name());
		RM0306.put("param3", "TDATE|" + ReportParamDataType.DATE.name());
		RM0306.put("param4", "XHWH|" + ReportParamDataType.STRING.name());
		RM0306.put("param5", "XWH|" + ReportParamDataType.STRING.name());
		RM0306.put("param6", "XCUS|" + ReportParamDataType.STRING.name());
		RM0306.put("param7", "XPORNUM|" + ReportParamDataType.STRING.name());
		RM0306.put("param8", "XSTATUSPOR|" + ReportParamDataType.STRING.name());
		
		
		RM0307.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0307.put("param2", "FDATE|" + ReportParamDataType.DATE.name());
		RM0307.put("param3", "TDATE|" + ReportParamDataType.DATE.name());
		RM0307.put("param4", "Supplier|" + ReportParamDataType.STRING.name());
		
		RM0308.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0308.put("param2", "FDATE|" + ReportParamDataType.DATE.name());
		RM0308.put("param3", "TDATE|" + ReportParamDataType.DATE.name());
		RM0308.put("param4", "XGCUS|" + ReportParamDataType.STRING.name());
		RM0308.put("param5", "XCUS|" + ReportParamDataType.STRING.name());
		
		RM0309.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0309.put("param2", "FDATE|" + ReportParamDataType.DATE.name());
		RM0309.put("param3", "TDATE|" + ReportParamDataType.DATE.name());
		RM0309.put("param4", "XGCUS|" + ReportParamDataType.STRING.name());
		RM0309.put("param5", "XCUS|" + ReportParamDataType.STRING.name());
		RM0309.put("param6", "XWH|" + ReportParamDataType.STRING.name());
		RM0309.put("param7", "XPAYMENTTYPE|" + ReportParamDataType.STRING.name());
		RM0309.put("param8", "XBANK|" + ReportParamDataType.STRING.name());
		RM0309.put("param9", "XSTAFF|" + ReportParamDataType.STRING.name());
		RM0309.put("param10", "XSTATUS|" + ReportParamDataType.STRING.name());
		
		RM0310.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0310.put("param2", "XCUS|" + ReportParamDataType.STRING.name());
		
		RM0311.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0311.put("param2", "XCUS|" + ReportParamDataType.STRING.name());
		RM0311.put("param3", "ACTIVE|" + ReportParamDataType.STRING.name());
		
		RM0312.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0312.put("param2", "FDATE|" + ReportParamDataType.DATE.name());
		RM0312.put("param3", "TDATE|" + ReportParamDataType.DATE.name());
		RM0312.put("param4", "XHWH|" + ReportParamDataType.STRING.name());
		RM0312.put("param5", "XWH|" + ReportParamDataType.STRING.name());
		RM0312.put("param6", "XCUS|" + ReportParamDataType.STRING.name());
		RM0312.put("param7", "XPORNUM|" + ReportParamDataType.STRING.name());
		RM0312.put("param8", "XITEM|" + ReportParamDataType.STRING.name());
		RM0312.put("param9", "XSTATUSPOR|" + ReportParamDataType.STRING.name());
		RM0312.put("param10", "XGITEM|" + ReportParamDataType.STRING.name());
		RM0312.put("param11", "XCATITEM|" + ReportParamDataType.STRING.name());
		
		RM0313.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0313.put("param2", "FDATE|" + ReportParamDataType.DATE.name());
		RM0313.put("param3", "TDATE|" + ReportParamDataType.DATE.name());
		RM0313.put("param4", "XHWH|" + ReportParamDataType.STRING.name());
		RM0313.put("param5", "XWH|" + ReportParamDataType.STRING.name());
		RM0313.put("param6", "XCUS|" + ReportParamDataType.STRING.name());
		RM0313.put("param7", "XPORNUM|" + ReportParamDataType.STRING.name());
		RM0313.put("param8", "XITEM|" + ReportParamDataType.STRING.name());
		RM0313.put("param9", "XSTATUSPOR|" + ReportParamDataType.STRING.name());
		RM0313.put("param10", "XGITEM|" + ReportParamDataType.STRING.name());
		RM0313.put("param11", "XCATITEM|" + ReportParamDataType.STRING.name());

		
		RM0314.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0314.put("param2", "XCUS|" + ReportParamDataType.STRING.name());
		RM0314.put("param3", "ACTIVE|" + ReportParamDataType.STRING.name());
		
		RM0401.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0401.put("param2", "FDATE|" + ReportParamDataType.DATE.name());
		RM0401.put("param3", "TDATE|" + ReportParamDataType.DATE.name());
		RM0401.put("param4", "XHWH|" + ReportParamDataType.STRING.name());
		RM0401.put("param5", "XWH|" + ReportParamDataType.STRING.name());
		RM0401.put("param6", "XCUS|" + ReportParamDataType.STRING.name());
		RM0401.put("param7", "XPORNUM|" + ReportParamDataType.STRING.name());
		RM0401.put("param8", "XITEM|" + ReportParamDataType.STRING.name());
		RM0401.put("param9", "XSTATUSPOR|" + ReportParamDataType.STRING.name());
		RM0401.put("param10", "XGITEM|" + ReportParamDataType.STRING.name());
		RM0401.put("param11", "XCATITEM|" + ReportParamDataType.STRING.name());
		
		RM0402.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0402.put("param2", "From Date|" + ReportParamDataType.DATE.name());
		RM0402.put("param3", "To Date|" + ReportParamDataType.DATE.name());
		RM0402.put("param4", "Customer|" + ReportParamDataType.STRING.name());
		RM0402.put("param5", "Do Status|" + ReportParamDataType.STRING.name());
		RM0402.put("param6", "Do Number|" + ReportParamDataType.STRING.name());
		
		RM0403.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0403.put("param2", "FDATE|" + ReportParamDataType.DATE.name());
		RM0403.put("param3", "TDATE|" + ReportParamDataType.DATE.name());
		RM0403.put("param4", "XHWH|" + ReportParamDataType.STRING.name());
		RM0403.put("param5", "XWH|" + ReportParamDataType.STRING.name());
		RM0403.put("param6", "XCUS|" + ReportParamDataType.STRING.name());
		RM0403.put("param7", "XPORNUM|" + ReportParamDataType.STRING.name());
		RM0403.put("param8", "XSTATUSPOR|" + ReportParamDataType.STRING.name());
		
		RM0404.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0404.put("param2", "FDATE|" + ReportParamDataType.DATE.name());
		RM0404.put("param3", "TDATE|" + ReportParamDataType.DATE.name());
		RM0404.put("param4", "XGCUS|" + ReportParamDataType.STRING.name());
		RM0404.put("param5", "XCUS|" + ReportParamDataType.STRING.name());
		RM0404.put("param6", "XWH|" + ReportParamDataType.STRING.name());
		RM0404.put("param7", "XPAYMENTTYPE|" + ReportParamDataType.STRING.name());
		RM0404.put("param8", "XBANK|" + ReportParamDataType.STRING.name());
		RM0404.put("param9", "XSTAFF|" + ReportParamDataType.STRING.name());
		RM0404.put("param10", "XSTATUS|" + ReportParamDataType.STRING.name());
		
		RM0405.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0405.put("param2", "FromDate|" + ReportParamDataType.DATE.name());
		RM0405.put("param3", "ToDate|" + ReportParamDataType.DATE.name());
		RM0405.put("param4", "Customer|" + ReportParamDataType.STRING.name());
		
		RM0406.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0406.put("param2", "FDATE|" + ReportParamDataType.DATE.name());
		RM0406.put("param3", "TDATE|" + ReportParamDataType.DATE.name());
		RM0406.put("param4", "CusGroup|" + ReportParamDataType.STRING.name());
		RM0406.put("param5", "XCUS|" + ReportParamDataType.STRING.name());

		RM0407.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0407.put("param2", "FDATE|" + ReportParamDataType.DATE.name());
		RM0407.put("param3", "TDATE|" + ReportParamDataType.DATE.name());
		RM0407.put("param4", "XHWH|" + ReportParamDataType.STRING.name());
		RM0407.put("param5", "XWH|" + ReportParamDataType.STRING.name());
		RM0407.put("param6", "XCUS|" + ReportParamDataType.STRING.name());
		RM0407.put("param7", "XPORNUM|" + ReportParamDataType.STRING.name());
		RM0407.put("param8", "XITEM|" + ReportParamDataType.STRING.name());
		RM0407.put("param9", "XSTATUSPOR|" + ReportParamDataType.STRING.name());
		RM0407.put("param10", "XGITEM|" + ReportParamDataType.STRING.name());
		RM0407.put("param11", "XCATITEM|" + ReportParamDataType.STRING.name());
		
//		RM0408.put("param1", "ZID|" + ReportParamDataType.STRING.name());
//		RM0408.put("param2", "XGCUS|" + ReportParamDataType.STRING.name());
		
//		RM0409.put("param1", "ZID|" + ReportParamDataType.STRING.name());
//		RM0409.put("param2", "FDATE|" + ReportParamDataType.DATE.name());
//		RM0409.put("param3", "TDATE|" + ReportParamDataType.DATE.name());
//		RM0409.put("param4", "XCUS|" + ReportParamDataType.STRING.name());
		
		RM0410.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0410.put("param2", "FDATE|" + ReportParamDataType.DATE.name());
		RM0410.put("param3", "TDATE|" + ReportParamDataType.DATE.name());
		RM0410.put("param4", "XHWH|" + ReportParamDataType.STRING.name());
		RM0410.put("param5", "XWH|" + ReportParamDataType.STRING.name());
		RM0410.put("param6", "XCUS|" + ReportParamDataType.STRING.name());
		RM0410.put("param7", "XPORNUM|" + ReportParamDataType.STRING.name());
		RM0410.put("param8", "XITEM|" + ReportParamDataType.STRING.name());
		RM0410.put("param9", "XSTATUSPOR|" + ReportParamDataType.STRING.name());
		RM0410.put("param10", "XGITEM|" + ReportParamDataType.STRING.name());
		RM0410.put("param11", "XCATITEM|" + ReportParamDataType.STRING.name());
		
		RM0411.put("param1", "zid|" + ReportParamDataType.STRING.name());
		RM0411.put("param2", "Date|" + ReportParamDataType.DATE.name());
		RM0411.put("param3", "Branch|" + ReportParamDataType.STRING.name());
		
		RM0412.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0412.put("param2", "DATE|" + ReportParamDataType.DATE.name());
		
		RM0501.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0501.put("param2", "XITEM|" + ReportParamDataType.STRING.name());
		RM0501.put("param3", "BOM|" + ReportParamDataType.STRING.name());

		
		RM0502.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0502.put("param2", "FDATE|" + ReportParamDataType.DATE.name());
		RM0502.put("param3", "TDATE|" + ReportParamDataType.DATE.name());
		RM0502.put("param4", "BATCHNO|" + ReportParamDataType.STRING.name());
		RM0502.put("param5", "XGITEM|" + ReportParamDataType.STRING.name());
		RM0502.put("param6", "XCATITEM|" + ReportParamDataType.STRING.name());
		RM0502.put("param7", "XITEM|" + ReportParamDataType.STRING.name());
		
		RM0503.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0503.put("param2", "FDATE|" + ReportParamDataType.DATE.name());
		RM0503.put("param3", "TDATE|" + ReportParamDataType.DATE.name());
		RM0503.put("param4", "BATCHNO|" + ReportParamDataType.STRING.name());
		RM0503.put("param5", "XGITEM|" + ReportParamDataType.STRING.name());
		RM0503.put("param6", "XCATITEM|" + ReportParamDataType.STRING.name());
		RM0503.put("param7", "XITEM|" + ReportParamDataType.STRING.name());
		
		RM0504.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0504.put("param2", "RAWMET|" + ReportParamDataType.STRING.name());
		
		RM0601.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0601.put("param2", "Item Group|" + ReportParamDataType.STRING.name());
		RM0601.put("param3", "Category|" + ReportParamDataType.STRING.name());
		RM0601.put("param4", "XITEM|" + ReportParamDataType.STRING.name());
		
		RM0602.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0602.put("param2", "FDATE|" + ReportParamDataType.DATE.name());
		RM0602.put("param3", "TDATE|" + ReportParamDataType.DATE.name());
		RM0602.put("param4", "XITEM|" + ReportParamDataType.STRING.name());
		RM0602.put("param5", "XGITEM|" + ReportParamDataType.STRING.name());
		RM0602.put("param6", "XCATITEM|" + ReportParamDataType.STRING.name());
		RM0602.put("param7", "XWH|" + ReportParamDataType.STRING.name());
		
		RM0603.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0603.put("param2", "Item Group|" + ReportParamDataType.STRING.name());
		RM0603.put("param3", "Category|" + ReportParamDataType.STRING.name());
		
//		RM0604.put("param1", "ZID|" + ReportParamDataType.STRING.name());
//		RM0604.put("param2", "FDATE|" + ReportParamDataType.DATE.name());
//		RM0604.put("param3", "TDATE|" + ReportParamDataType.DATE.name());
//		RM0604.put("param4", "ITEM|" + ReportParamDataType.STRING.name());
//		RM0604.put("param5", "ITMGRP|" + ReportParamDataType.STRING.name());
//		RM0604.put("param6", "ITMCAT|" + ReportParamDataType.STRING.name());
		
		RM0605.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0605.put("param2", "FDATE|" + ReportParamDataType.DATE.name());
		RM0605.put("param3", "TDATE|" + ReportParamDataType.DATE.name());
		RM0605.put("param4", "ITM|" + ReportParamDataType.STRING.name());
		RM0605.put("param5", "ITMGRP|" + ReportParamDataType.STRING.name());
		RM0605.put("param6", "ITMCAT|" + ReportParamDataType.STRING.name());
		RM0605.put("param7", "XWH|" + ReportParamDataType.STRING.name());
		

		RM0606.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0606.put("param2", "FDATE|" + ReportParamDataType.DATE.name());
		RM0606.put("param3", "TDATE|" + ReportParamDataType.DATE.name());
		RM0606.put("param4", "ITM|" + ReportParamDataType.STRING.name());
		RM0606.put("param5", "ITMGRP|" + ReportParamDataType.STRING.name());
		RM0606.put("param6", "ITMCAT|" + ReportParamDataType.STRING.name());
		RM0606.put("param7", "WAREHOUSE|" + ReportParamDataType.STRING.name());
		
		RM0607.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0607.put("param2", "XGITEM|" + ReportParamDataType.STRING.name());
		RM0607.put("param3", "XCATAGORY|" + ReportParamDataType.STRING.name());
		RM0607.put("param4", "XITEM|" + ReportParamDataType.STRING.name());

		RM0608.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0608.put("param2", "FDATE|" + ReportParamDataType.DATE.name());
		
		RM0609.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0609.put("param2", "DPT|" + ReportParamDataType.STRING.name());
		RM0609.put("param3", "STATUS|" + ReportParamDataType.STRING.name());
	
		RM0610.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		
		RM0611.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0611.put("param2", "FDATE|" + ReportParamDataType.DATE.name());
		RM0611.put("param3", "TDATE|" + ReportParamDataType.DATE.name());
		RM0611.put("param4", "FWH|" + ReportParamDataType.STRING.name());
		RM0611.put("param5", "TWH|" + ReportParamDataType.STRING.name());
		RM0611.put("param6", "STATUS|" + ReportParamDataType.STRING.name());
	
		RM0700.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0700.put("param2", "LAND|" + ReportParamDataType.STRING.name());
		
		RM0701.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0701.put("param2", "LAND|" + ReportParamDataType.STRING.name());
		RM0701.put("param3", "CSDAG|" + ReportParamDataType.STRING.name());
		RM0701.put("param4", "RSDAG|" + ReportParamDataType.STRING.name());
		RM0701.put("param5", "CITYDAG|" + ReportParamDataType.STRING.name());
//		RM0701.put("param3", "MEMBER|" + ReportParamDataType.STRING.name());
//		RM0701.put("param4", "DAG|" + ReportParamDataType.STRING.name());
		
		
		RM0702.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0702.put("param2", "LAND|" + ReportParamDataType.STRING.name());
		RM0702.put("param3", "Status|" + ReportParamDataType.STRING.name());
		
		RM0703.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0703.put("param2", "From Date|" + ReportParamDataType.DATE.name());
		RM0703.put("param3", "To Date|" + ReportParamDataType.DATE.name());
		RM0703.put("param4", "Customer|" + ReportParamDataType.STRING.name());
		RM0703.put("param5", "Do Status|" + ReportParamDataType.STRING.name());
		RM0703.put("param6", "Do Number|" + ReportParamDataType.STRING.name());
		
		RM0704.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0704.put("param2", "From Date|" + ReportParamDataType.DATE.name());
		RM0704.put("param3", "To Date|" + ReportParamDataType.DATE.name());
		RM0704.put("param4", "Customer|" + ReportParamDataType.STRING.name());
		RM0704.put("param5", "Do Status|" + ReportParamDataType.STRING.name());
		RM0704.put("param6", "Do Number|" + ReportParamDataType.STRING.name());
		
		RM0705.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0705.put("param2", "TYPE|" + ReportParamDataType.STRING.name());
		RM0705.put("param3", "NUM|" + ReportParamDataType.STRING.name());
		
		RM0706.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0706.put("param2", "MEMBER|" + ReportParamDataType.STRING.name());
		RM0706.put("param3", "LAND|" + ReportParamDataType.STRING.name());
		RM0706.put("param4", "STATUS|" + ReportParamDataType.STRING.name());
		
		RM0707.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0707.put("param2", "FDATE|" + ReportParamDataType.DATE.name());
		RM0707.put("param3", "TDATE|" + ReportParamDataType.DATE.name());
		RM0707.put("param4", "XGCUS|" + ReportParamDataType.STRING.name());
		RM0707.put("param5", "PAYTYPE|" + ReportParamDataType.STRING.name());
		RM0707.put("param6", "RECBY|" + ReportParamDataType.STRING.name());
		
		RM0708.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0708.put("param2", "FDATE|" + ReportParamDataType.DATE.name());
		RM0708.put("param3", "TDATE|" + ReportParamDataType.DATE.name());
		RM0708.put("param4", "CUS|" + ReportParamDataType.STRING.name());
		RM0708.put("param5", "LAND|" + ReportParamDataType.STRING.name());
		RM0708.put("param6", "PUR|" + ReportParamDataType.STRING.name());
		
		//Account Report Parameters
		RM0801.put("param1", "p_from_date|" + ReportParamDataType.DATE.name());
		RM0801.put("param2", "p_to_date|" + ReportParamDataType.DATE.name());
		RM0801.put("param3", "p_xwh|" + ReportParamDataType.STRING.name());
		RM0801.put("param4", "p_xyear|" + ReportParamDataType.STRING.name());
		RM0801.put("param5", "p_zid|" + ReportParamDataType.STRING.name());
		
		RM0802.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0802.put("param2", "P_voucher|" + ReportParamDataType.STRING.name());
		
		RM0803.put("param1", "p_from_date|" + ReportParamDataType.DATE.name());
		RM0803.put("param2", "p_to_date|" + ReportParamDataType.DATE.name());
		RM0803.put("param3", "p_xwh|" + ReportParamDataType.STRING.name());
		RM0803.put("param4", "p_xyear|" + ReportParamDataType.STRING.name());
		RM0803.put("param5", "p_zid|" + ReportParamDataType.STRING.name());
		
		RM0804.put("param1", "p_from_date|" + ReportParamDataType.DATE.name());
		RM0804.put("param2", "p_to_date|" + ReportParamDataType.DATE.name());
		RM0804.put("param3", "p_xac|" + ReportParamDataType.STRING.name());
		RM0804.put("param4", "p_xwh|" + ReportParamDataType.STRING.name());
		RM0804.put("param5", "p_xyear|" + ReportParamDataType.STRING.name());
		RM0804.put("param6", "p_zid|" + ReportParamDataType.STRING.name());
		RM0804.put("param7", "p_xsub|" + ReportParamDataType.STRING.name());
		
		
		RM0805.put("param1", "p_from_date|" + ReportParamDataType.DATE.name());
		RM0805.put("param2", "p_to_date|" + ReportParamDataType.DATE.name());
		RM0805.put("param3", "p_xac|" + ReportParamDataType.STRING.name());
		RM0805.put("param4", "p_xwh|" + ReportParamDataType.STRING.name());
		RM0805.put("param5", "p_xyear|" + ReportParamDataType.STRING.name());
		RM0805.put("param6", "p_zid|" + ReportParamDataType.STRING.name());
		RM0805.put("param7", "p_xsub|" + ReportParamDataType.STRING.name());
		
		RM0806.put("param1", "p_from_date|" + ReportParamDataType.DATE.name());
		RM0806.put("param2", "p_to_date|" + ReportParamDataType.DATE.name());
		RM0806.put("param3", "p_xwh|" + ReportParamDataType.STRING.name());
		RM0806.put("param4", "p_xyear|" + ReportParamDataType.STRING.name());
		RM0806.put("param5", "p_zid|" + ReportParamDataType.STRING.name());
		
		RM0807.put("param1", "p_from_date|" + ReportParamDataType.DATE.name());
		RM0807.put("param2", "p_to_date|" + ReportParamDataType.DATE.name());
		RM0807.put("param3", "p_xwh|" + ReportParamDataType.STRING.name());
		RM0807.put("param4", "p_xyear|" + ReportParamDataType.STRING.name());
		RM0807.put("param5", "p_zid|" + ReportParamDataType.STRING.name());
		
		RM0808.put("param1", "p_zid|" + ReportParamDataType.STRING.name());
		RM0808.put("param2", "p_from_date|" + ReportParamDataType.DATE.name());
		RM0808.put("param3", "p_to_date|" + ReportParamDataType.DATE.name());
		RM0808.put("param4", "p_statusJv|" + ReportParamDataType.STRING.name());
		RM0808.put("param5", "p_status|" + ReportParamDataType.STRING.name());

		RM0809.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		
		RM0810.put("param1", "ZID|" + ReportParamDataType.STRING.name());

		RM0901.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0901.put("param2", "CUS|" + ReportParamDataType.STRING.name());
		RM0901.put("param3", "PROJECT|" + ReportParamDataType.STRING.name());
		RM0901.put("param4", "SITE|" + ReportParamDataType.STRING.name());

		RM0902.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0902.put("param2", "FDATE|" + ReportParamDataType.DATE.name());
		RM0902.put("param3", "TDATE|" + ReportParamDataType.DATE.name());
		RM0902.put("param4", "XHWH|" + ReportParamDataType.STRING.name());
		RM0902.put("param5", "XWH|" + ReportParamDataType.STRING.name());
		RM0902.put("param6", "XCUS|" + ReportParamDataType.STRING.name());		
		
		RM0904.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0904.put("param2", "XCUS|" + ReportParamDataType.STRING.name());
		
		RM0905.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0905.put("param2", "FDATE|" + ReportParamDataType.DATE.name());
		RM0905.put("param3", "TDATE|" + ReportParamDataType.DATE.name());
		RM0905.put("param4", "XHWH|" + ReportParamDataType.STRING.name());
		RM0905.put("param5", "XWH|" + ReportParamDataType.STRING.name());
		RM0905.put("param6", "XCUS|" + ReportParamDataType.STRING.name());
		
		RM0906.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0906.put("param2", "XHWH|" + ReportParamDataType.STRING.name());
		RM0906.put("param3", "XWH|" + ReportParamDataType.STRING.name());
		RM0906.put("param4", "XCUS|" + ReportParamDataType.STRING.name());
		
		RM0909.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0909.put("param2", "FDATE|" + ReportParamDataType.DATE.name());
		RM0909.put("param3", "TDATE|" + ReportParamDataType.DATE.name());
		RM0909.put("param4", "XHWH|" + ReportParamDataType.STRING.name());
		RM0909.put("param5", "XWH|" + ReportParamDataType.STRING.name());
		RM0909.put("param6", "XCUS|" + ReportParamDataType.STRING.name());
		RM0909.put("param7", "XPORNUM|" + ReportParamDataType.STRING.name());
		RM0909.put("param8", "XITEM|" + ReportParamDataType.STRING.name());
		RM0909.put("param9", "XSTATUSPOR|" + ReportParamDataType.STRING.name());
		RM0909.put("param10", "XGITEM|" + ReportParamDataType.STRING.name());
		RM0909.put("param11", "XCATITEM|" + ReportParamDataType.STRING.name()); 
		
		
		RM0910.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0910.put("param2", "FDATE|" + ReportParamDataType.DATE.name());
		RM0910.put("param3", "TDATE|" + ReportParamDataType.DATE.name());
		RM0910.put("param4", "XHWH|" + ReportParamDataType.STRING.name());
		RM0910.put("param5", "XWH|" + ReportParamDataType.STRING.name());
		RM0910.put("param6", "XCUS|" + ReportParamDataType.STRING.name());
		RM0910.put("param7", "XPORNUM|" + ReportParamDataType.STRING.name());
		RM0910.put("param8", "XITEM|" + ReportParamDataType.STRING.name());
		RM0910.put("param9", "XSTATUSPOR|" + ReportParamDataType.STRING.name());
		RM0910.put("param10", "XGITEM|" + ReportParamDataType.STRING.name());
		RM0910.put("param11", "XCATITEM|" + ReportParamDataType.STRING.name()); 
		
		
		RM0911.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0911.put("param2", "FDATE|" + ReportParamDataType.DATE.name());
		RM0911.put("param3", "TDATE|" + ReportParamDataType.DATE.name());
		RM0911.put("param4", "XHWH|" + ReportParamDataType.STRING.name());
		RM0911.put("param5", "XWH|" + ReportParamDataType.STRING.name());
		RM0911.put("param6", "XCUS|" + ReportParamDataType.STRING.name());
		RM0911.put("param7", "XPORNUM|" + ReportParamDataType.STRING.name());
		RM0911.put("param8", "XITEM|" + ReportParamDataType.STRING.name());
		RM0911.put("param9", "XSTATUSPOR|" + ReportParamDataType.STRING.name());
		RM0911.put("param10", "XGITEM|" + ReportParamDataType.STRING.name());
		RM0911.put("param11", "XCATITEM|" + ReportParamDataType.STRING.name()); 
		
		
		RM0912.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0912.put("param2", "FDATE|" + ReportParamDataType.DATE.name());
		RM0912.put("param3", "TDATE|" + ReportParamDataType.DATE.name());
		RM0912.put("param4", "XHWH|" + ReportParamDataType.STRING.name());
		RM0912.put("param5", "XWH|" + ReportParamDataType.STRING.name());
		RM0912.put("param6", "XCUS|" + ReportParamDataType.STRING.name());
		RM0912.put("param7", "XPORNUM|" + ReportParamDataType.STRING.name());
		RM0912.put("param8", "XSTATUSPOR|" + ReportParamDataType.STRING.name());


//		SRS.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
//		SRS.put("param2", "fdate|" + ReportParamDataType.DATE.name());
//		SRS.put("param3", "tdate|" + ReportParamDataType.DATE.name());
//		SRS.put("param4", "div|" + ReportParamDataType.STRING.name());
//		SRS.put("param5", "terminal|" + ReportParamDataType.STRING.name());
//		SRS.put("param6", "rpttype|" + ReportParamDataType.STRING.name());
//		SRS.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());
//
//		SRD.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
//		SRD.put("param2", "fdate|" + ReportParamDataType.DATE.name());
//		SRD.put("param3", "tdate|" + ReportParamDataType.DATE.name());
//		SRD.put("param4", "div|" + ReportParamDataType.STRING.name());
//		SRD.put("param5", "terminal|" + ReportParamDataType.STRING.name());
//		SRD.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());
//
//		CBR.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
//		CBR.put("param2", "fdate|" + ReportParamDataType.DATE.name());
//		CBR.put("param3", "tdate|" + ReportParamDataType.DATE.name());
//		CBR.put("param4", "terminal|" + ReportParamDataType.STRING.name());
//		CBR.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());
//
//		CSR.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
//		CSR.put("param2", "fdate|" + ReportParamDataType.DATE.name());
//		CSR.put("param3", "tdate|" + ReportParamDataType.DATE.name());
//		CSR.put("param4", "div|" + ReportParamDataType.STRING.name());
//		CSR.put("param5", "citem|" + ReportParamDataType.STRING.name());
//		CSR.put("param6", "item|" + ReportParamDataType.STRING.name());
//		CSR.put("param7", "reptype|" + ReportParamDataType.STRING.name());
//		CSR.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());
//
//		IWSS.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
//		IWSS.put("param2", "fdate|" + ReportParamDataType.DATE.name());
//		IWSS.put("param3", "tdate|" + ReportParamDataType.DATE.name());
//		IWSS.put("param4", "div|" + ReportParamDataType.STRING.name());
//		IWSS.put("param5", "item|" + ReportParamDataType.STRING.name());
//		IWSS.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());
//
//		SMWSS.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
//		SMWSS.put("param2", "fdate|" + ReportParamDataType.DATE.name());
//		SMWSS.put("param3", "tdate|" + ReportParamDataType.DATE.name());
//		SMWSS.put("param4", "div|" + ReportParamDataType.STRING.name());
//		SMWSS.put("param5", "item|" + ReportParamDataType.STRING.name());
//		SMWSS.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());
//
//		IWSST.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
//		IWSST.put("param2", "fdate|" + ReportParamDataType.DATE.name());
//		IWSST.put("param3", "tdate|" + ReportParamDataType.DATE.name());
//		IWSST.put("param4", "div|" + ReportParamDataType.STRING.name());
//		IWSST.put("param5", "item|" + ReportParamDataType.STRING.name());
//		IWSST.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());
//
//		IL.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
//		IL.put("param2", "item|" + ReportParamDataType.STRING.name());
//		IL.put("param3", "category|" + ReportParamDataType.STRING.name());
//		IL.put("param4", "status|" + ReportParamDataType.STRING.name());
//		IL.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());
//
//		IPH.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
//		IPH.put("param2", "fdate|" + ReportParamDataType.DATE.name());
//		IPH.put("param3", "tdate|" + ReportParamDataType.DATE.name());
//		IPH.put("param4", "item|" + ReportParamDataType.STRING.name());
//		IPH.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());
//
//		CRS.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
//		CRS.put("param2", "fdate|" + ReportParamDataType.DATE.name());
//		CRS.put("param3", "tdate|" + ReportParamDataType.DATE.name());
//		CRS.put("param4", "terminal|" + ReportParamDataType.STRING.name());
//		CRS.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());
//
//		CRD.put("param1", "@zid|" + ReportParamDataType.INTEGER.name());
//		CRD.put("param2", "@puser|" + ReportParamDataType.STRING.name());
//		CRD.put("param3", "@pfterminal|" + ReportParamDataType.STRING.name());
//		CRD.put("param4", "@ptterminal|" + ReportParamDataType.STRING.name());
//		CRD.put("param5", "@fdate|" + ReportParamDataType.DATESTRING.name());
//		CRD.put("param6", "@tdate|" + ReportParamDataType.DATESTRING.name());
//		CRD.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());
//
//		CCCD.put("param1", "@zid|" + ReportParamDataType.INTEGER.name());
//		CCCD.put("param2", "@user|" + ReportParamDataType.STRING.name());
//		CCCD.put("param3", "@terminal|" + ReportParamDataType.STRING.name());
//		CCCD.put("param4", "@date|" + ReportParamDataType.DATESTRING.name());
//		CCCD.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());
//
//		SET.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
//		SET.put("param2", "fdate|" + ReportParamDataType.DATE.name());
//		SET.put("param3", "tdate|" + ReportParamDataType.DATE.name());
//		SET.put("param4", "terminal|" + ReportParamDataType.STRING.name());
//		SET.put("param5", "pack|" + ReportParamDataType.STRING.name());
//		SET.put("param6", "item|" + ReportParamDataType.STRING.name());
//		SET.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());
//
//		VR.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
//		VR.put("param2", "fdate|" + ReportParamDataType.DATE.name());
//		VR.put("param3", "tdate|" + ReportParamDataType.DATE.name());
//		VR.put("param4", "div|" + ReportParamDataType.STRING.name());
//		VR.put("param5", "shop|" + ReportParamDataType.STRING.name());
//		VR.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());
//
//		SS.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
//		SS.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());
//
//		DR.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
//		DR.put("param2", "fdate|" + ReportParamDataType.DATE.name());
//		DR.put("param3", "tdate|" + ReportParamDataType.DATE.name());
//		DR.put("param4", "div|" + ReportParamDataType.STRING.name());
//		DR.put("param5", "terminal|" + ReportParamDataType.STRING.name());
//		DR.put("param6", "rpttype|" + ReportParamDataType.STRING.name());
//		DR.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());
//
//		DSR.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
//		DSR.put("param2", "fdate|" + ReportParamDataType.DATE.name());
//		DSR.put("param3", "tdate|" + ReportParamDataType.DATE.name());
//		DSR.put("param4", "div|" + ReportParamDataType.STRING.name());
//		DSR.put("param5", "terminal|" + ReportParamDataType.STRING.name());
//		DSR.put("param6", "rpttype|" + ReportParamDataType.STRING.name());
//		DSR.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());
//
//		SRSV.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
//		SRSV.put("param2", "fdate|" + ReportParamDataType.DATE.name());
//		SRSV.put("param3", "tdate|" + ReportParamDataType.DATE.name());
//		SRSV.put("param4", "terminal|" + ReportParamDataType.STRING.name());
//		SRSV.put("param5", "rpttype|" + ReportParamDataType.STRING.name());
//		SRSV.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());
//
//		SRDV.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
//		SRDV.put("param2", "fdate|" + ReportParamDataType.DATE.name());
//		SRDV.put("param3", "tdate|" + ReportParamDataType.DATE.name());
//		SRDV.put("param4", "terminal|" + ReportParamDataType.STRING.name());
//		SRDV.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());
//
//		IWSSV.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
//		IWSSV.put("param2", "fdate|" + ReportParamDataType.DATE.name());
//		IWSSV.put("param3", "tdate|" + ReportParamDataType.DATE.name());
//		IWSSV.put("param4", "item|" + ReportParamDataType.STRING.name());
//		IWSSV.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());
//
//		IPHV.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
//		IPHV.put("param2", "fdate|" + ReportParamDataType.DATE.name());
//		IPHV.put("param3", "tdate|" + ReportParamDataType.DATE.name());
//		IPHV.put("param4", "item|" + ReportParamDataType.STRING.name());
//		IPHV.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());
//
//		CRSV.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
//		CRSV.put("param2", "fdate|" + ReportParamDataType.DATE.name());
//		CRSV.put("param3", "tdate|" + ReportParamDataType.DATE.name());
//		CRSV.put("param4", "terminal|" + ReportParamDataType.STRING.name());
//		CRSV.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());
//
//		CRDV.put("param1", "@zid|" + ReportParamDataType.INTEGER.name());
//		CRDV.put("param2", "@puser|" + ReportParamDataType.STRING.name());
//		CRDV.put("param3", "@pfterminal|" + ReportParamDataType.STRING.name());
//		CRDV.put("param4", "@ptterminal|" + ReportParamDataType.STRING.name());
//		CRDV.put("param5", "@fdate|" + ReportParamDataType.DATESTRING.name());
//		CRDV.put("param6", "@tdate|" + ReportParamDataType.DATESTRING.name());
//		CRDV.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());
//
//		CCCDV.put("param1", "@zid|" + ReportParamDataType.INTEGER.name());
//		CCCDV.put("param2", "@user|" + ReportParamDataType.STRING.name());
//		CCCDV.put("param3", "@terminal|" + ReportParamDataType.STRING.name());
//		CCCDV.put("param4", "@date|" + ReportParamDataType.DATESTRING.name());
//		CCCDV.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());
//
//		SETV.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
//		SETV.put("param2", "fdate|" + ReportParamDataType.DATE.name());
//		SETV.put("param3", "tdate|" + ReportParamDataType.DATE.name());
//		SETV.put("param4", "terminal|" + ReportParamDataType.STRING.name());
//		SETV.put("param5", "pack|" + ReportParamDataType.STRING.name());
//		SETV.put("param6", "item|" + ReportParamDataType.STRING.name());
//		SETV.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());
//
//		OOSS.put("param1", "@zid|" + ReportParamDataType.INTEGER.name());
//		OOSS.put("param2", "@shop|" + ReportParamDataType.STRING.name());
//		OOSS.put("param3", "@fdate|" + ReportParamDataType.DATESTRING.name());
//		OOSS.put("param4", "@tdate|" + ReportParamDataType.DATESTRING.name());
//		OOSS.put("param5", "@reporttype|" + ReportParamDataType.STRING.name());
//		OOSS.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());
//
//		OHCAS.put("param1", "@zid|" + ReportParamDataType.INTEGER.name());
//		OHCAS.put("param2", "@shop|" + ReportParamDataType.STRING.name());
//		OHCAS.put("param3", "@fdate|" + ReportParamDataType.DATESTRING.name());
//		OHCAS.put("param4", "@tdate|" + ReportParamDataType.DATESTRING.name());
//		OHCAS.put("param5", "@ftime|" + ReportParamDataType.STRING.name());
//		OHCAS.put("param6", "@ttime|" + ReportParamDataType.STRING.name());
//		OHCAS.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());
//
//		OISS.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
//		OISS.put("param2", "shop|" + ReportParamDataType.STRING.name());
//		OISS.put("param3", "fdate|" + ReportParamDataType.DATE.name());
//		OISS.put("param4", "tdate|" + ReportParamDataType.DATE.name());
//		OISS.put("param5", "ftime|" + ReportParamDataType.STRING.name());
//		OISS.put("param6", "ttime|" + ReportParamDataType.STRING.name());
//		OISS.put("param7", "reporttype|" + ReportParamDataType.STRING.name());
//		OISS.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());
//
//		OOSB.put("param1", "@zid|" + ReportParamDataType.INTEGER.name());
//		OOSB.put("param2", "@shop|" + ReportParamDataType.STRING.name());
//		OOSB.put("param3", "@fdate|" + ReportParamDataType.DATESTRING.name());
//		OOSB.put("param4", "@tdate|" + ReportParamDataType.DATESTRING.name());
//		OOSB.put("param5", "@reporttype|" + ReportParamDataType.STRING.name());
//		OOSB.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());
//
//		OBDWAS.put("param1", "@zid|" + ReportParamDataType.INTEGER.name());
//		OBDWAS.put("param2", "@shop|" + ReportParamDataType.STRING.name());
//		OBDWAS.put("param3", "@fdate|" + ReportParamDataType.DATESTRING.name());
//		OBDWAS.put("param4", "@tdate|" + ReportParamDataType.DATESTRING.name());
//		OBDWAS.put("param5", "@ftime|" + ReportParamDataType.STRING.name());
//		OBDWAS.put("param6", "@ttime|" + ReportParamDataType.STRING.name());
//		OBDWAS.put("param7", "@reporttype|" + ReportParamDataType.STRING.name());
//		OBDWAS.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());
//
//		ODWSS.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
//		ODWSS.put("param2", "fdate|" + ReportParamDataType.DATE.name());
//		ODWSS.put("param3", "tdate|" + ReportParamDataType.DATE.name());
//		ODWSS.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());
//
//		OSS.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
//		OSS.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

	}
}
