package com.asl.model;

import java.util.Date;

import lombok.Data;

@Data
public class Consumption {
	
	private String zid;
	private String xuser;
	private Date xstartdate;
	private Date xdatexenddate;
	private Date xdate;
	private String xwh;
	private String xtrn;
	

}
