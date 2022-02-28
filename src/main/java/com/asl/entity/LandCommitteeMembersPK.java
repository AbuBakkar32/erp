package com.asl.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class LandCommitteeMembersPK implements Serializable{

	
	private static final long serialVersionUID = 1338023978221333467L;
	
	private String zid;
	private String xcommittee;
	private int xrow;
	

}
