package com.asl.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class LandOwnerPK implements Serializable {

	private static final long serialVersionUID = 8464884857440293115L;
	private String zid;
	private String xland;
	private int xrow;
	
}
