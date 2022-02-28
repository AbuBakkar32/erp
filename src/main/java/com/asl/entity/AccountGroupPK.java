package com.asl.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2021
 */
@Data
public class AccountGroupPK implements Serializable {

	private static final long serialVersionUID = 2917914189885869772L;

	private String zid;
	private String xagcode;
}
