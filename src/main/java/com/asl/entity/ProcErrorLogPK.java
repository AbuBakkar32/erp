package com.asl.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Mar 20, 2021
 */
@Data
public class ProcErrorLogPK implements Serializable {

	private static final long serialVersionUID = 6510381915906667317L;

	private String zid;
	private String osqlCode;
	private String action;
	private String source;
}
