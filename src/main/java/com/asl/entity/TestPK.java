package com.asl.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class TestPK implements Serializable{
	
	private static final long serialVersionUID = -4255961843803924966L;
	
	private String zid;
	private String xpornum;

}
