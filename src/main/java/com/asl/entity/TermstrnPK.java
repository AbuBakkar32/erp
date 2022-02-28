package com.asl.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class TermstrnPK implements Serializable{
	
	private static final long serialVersionUID = -701824612920817563L;
	
	private String zid;
	private String xdocnum;
	private String xterm;
}
