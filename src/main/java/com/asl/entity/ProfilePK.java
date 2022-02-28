package com.asl.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Mar 30, 2021
 */
@Data
public class ProfilePK implements Serializable {

	private static final long serialVersionUID = -4242102785998812790L;

	private String zid;
	private String profilecode;
}
