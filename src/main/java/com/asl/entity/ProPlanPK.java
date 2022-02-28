package com.asl.entity;

import java.io.Serializable;

import lombok.Data;
@Data
public class ProPlanPK implements Serializable{
	
	private static final long serialVersionUID = -2840466818555355098L;

	private String xprplan;
	private String zid;
	private int xrow;
}
