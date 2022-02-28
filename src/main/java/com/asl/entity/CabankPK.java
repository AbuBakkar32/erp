package com.asl.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class CabankPK implements Serializable {

	private static final long serialVersionUID = -3334586101823767275L;

	private String zid;
	private String xbank;
}
