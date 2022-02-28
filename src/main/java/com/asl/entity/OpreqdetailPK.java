package com.asl.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Jun 20, 2021
 */
@Data
public class OpreqdetailPK implements Serializable {

	private static final long serialVersionUID = -5633240762700874357L;

	private String zid;
	private String xdoreqnum;
	private int xrow;
}
