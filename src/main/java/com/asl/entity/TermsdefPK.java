package com.asl.entity;

import java.io.Serializable;

import lombok.Data;
@Data
public class TermsdefPK implements Serializable{

	private static final long serialVersionUID = 3272388764636485266L;

	private String zid;
	private String xtype;
	private String xterm;
	private int xtermid;

}
