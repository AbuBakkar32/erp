package com.asl.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class OpcrndetailPK implements Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8320745309193612264L;
	private String zid;
	private String xcrnnum;
	private int xrow;

}
