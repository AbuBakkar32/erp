package com.asl.entity;

import java.util.Date;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since May 27, 2021
 */
@Data
public class ConventionBookedDetails {

	private String xordernum;
	private Date xstartdate;
	private String xstarttime;
	private Date xenddate;
	private String xendtime;
	private String xstatus;
	private String xitem;
	private String xdesc;
	private boolean booked;
}
