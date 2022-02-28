package com.asl.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Feb 24, 2021
 */
@Data
public class XcodesPK implements Serializable {

	private static final long serialVersionUID = 3915133288641449550L;

	private String zid;
	private String xcode;
	private String xtype;
}
