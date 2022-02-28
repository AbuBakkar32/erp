package com.asl.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Mar 30, 2021
 */
@Data
public class ProfileLinePK implements Serializable {

	private static final long serialVersionUID = -6952207808761772245L;

	private String zid;
	private String profilelineid;
	private String profilelinecode;
	private String profilecode;
}
