package com.asl.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class POSPermissionsPK implements Serializable{

	private static final long serialVersionUID = 4694199570846191164L;
	private String zid;
	private String xacc;
	private String xrole;
}
