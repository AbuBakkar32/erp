package com.asl.entity;

import java.util.Date;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Apr 26, 2021
 */
@Data
public class ASLProcError {

	private String zid;
	private String zauserid;
	private Date zutime;
	private String osqlcode;
	private String v_error_message;
	private String v_source;
	private String action;
}
