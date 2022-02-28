package com.asl.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class ArhedPK implements Serializable {

	private static final long serialVersionUID = -7292652155193209162L;
	
	private String zid;
	private String xvoucher;
}
