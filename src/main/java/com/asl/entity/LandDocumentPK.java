package com.asl.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class LandDocumentPK implements Serializable{
	
	private static final long serialVersionUID = -9050387008400636769L;

	private String zid;
	private String xdoc;
	private int xrow;
}
