package com.asl.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Feb 24, 2021
 */
@Data
public class XusersPK implements Serializable {

	private static final long serialVersionUID = 6458664254386681580L;

	private String zid;
	private String zemail;
}
