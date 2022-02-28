package com.asl.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class AcmstPK implements Serializable{
	
	private static final long serialVersionUID = 8445840017619927957L;
	
	private String zid;
	private String xacc;
	
}
