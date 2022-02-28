package com.asl.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Jun 20, 2021
 */
@Data
public class OpreqheaderPK implements Serializable {

	private static final long serialVersionUID = -5404937390882380667L;

	private String zid;
	private String xdoreqnum;
}
