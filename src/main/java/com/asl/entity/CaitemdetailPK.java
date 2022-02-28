package com.asl.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Jun 13, 2021
 */
@Data
public class CaitemdetailPK implements Serializable {

	private static final long serialVersionUID = 4903754583117663127L;

	private String zid;
	private String xitem;
	private String xsubitem;
}
