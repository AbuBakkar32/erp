package com.asl.entity;
import java.io.Serializable;

import lombok.Data;
@Data
public class XtrnpPK implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5285520774719195613L;

	private String zid;
	private String xtypetrn;
	private String xtrn;
	private String xtyperel;
}
