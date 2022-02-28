package com.asl.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class PogrnHeaderPK implements Serializable{

	private static final long serialVersionUID = 6977822924762929217L;
	
	private String zid;
	private String xgrnnum;

}
