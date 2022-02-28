package com.asl.model.report;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Apr 7, 2021
 */
@Data
public class AbstractReportModel implements Serializable {

	private static final long serialVersionUID = 8575759329347924405L;

	private String businessName;
	private String businessAddress;
	private String reportName;
	private String fromDate;
	private String toDate;
	private String printDate;
	private String copyrightText = "Powered by ASL © Copyright " + new SimpleDateFormat("yyyy").format(new Date());
	private String reportLogo;
	private String phone;
	private String fax;
	private String email;
}
