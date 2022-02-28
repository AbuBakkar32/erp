package com.asl.entity;

import java.io.Serializable;

import lombok.Data;
@Data
public class prplanfinPK implements Serializable{
	
	private static final long serialVersionUID = -686090801157809516L;
	
	private String xprplan;
	private String zid;
	private int xrow;

}
