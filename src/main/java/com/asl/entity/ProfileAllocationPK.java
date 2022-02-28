package com.asl.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Mar 30, 2021
 */
@Data
public class ProfileAllocationPK implements Serializable {

	private static final long serialVersionUID = 8360552698895531240L;

	private String zid;
	private Long paid;
	private String zemail;
	
}
