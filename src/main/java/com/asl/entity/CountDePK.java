package com.asl.entity;

import java.io.Serializable;

import lombok.Data;
@Data
public class CountDePK implements Serializable{
	
	private static final long serialVersionUID = 6122488646033218481L;

	private String xprplan;
	private String zid;
	private int xrow;
	private int xcount;
	
}
