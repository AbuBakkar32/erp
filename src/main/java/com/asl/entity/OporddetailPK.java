package com.asl.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Mar 8, 2021
 */
@Data
public class OporddetailPK implements Serializable {

	private static final long serialVersionUID = -8282875406776969688L;

	private String zid;
	private String xordernum;
	private String xrow;
}
