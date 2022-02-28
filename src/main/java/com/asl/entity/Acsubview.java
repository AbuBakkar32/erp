package com.asl.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Jul 25, 2021
 */
@Data
public class Acsubview implements Serializable {

	private static final long serialVersionUID = 4133168208397957184L;

	private String zid;
	private String xsub;
	private String xorg;
	private String xmadd;
	private String xaccusage;
	private String xacc;
	private String xbank;
	private String xacccr;
	private String xaccdr;
	private int count;
	private String xgcus;
	private String xacctype;
}
