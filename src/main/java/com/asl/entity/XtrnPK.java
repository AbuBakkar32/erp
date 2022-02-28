package com.asl.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Feb 24, 2021
 */
@Data
public class XtrnPK implements Serializable {

	private static final long serialVersionUID = 7078439678926327781L;
	private String zid;
	private String xtrn;
	private String xtypetrn;
}
