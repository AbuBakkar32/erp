package com.asl.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Mar 8, 2021
 */
@Data
public class OpordheaderPK implements Serializable {

	private static final long serialVersionUID = 142228474840303755L;

	private String zid;
	private String xordernum;
}
