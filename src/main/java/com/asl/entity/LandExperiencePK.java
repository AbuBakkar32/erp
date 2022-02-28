package com.asl.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class LandExperiencePK implements Serializable{
	
	private static final long serialVersionUID = -1963498395311657738L;
	private String zid;
	private String xperson;
	private int xrow;

}
