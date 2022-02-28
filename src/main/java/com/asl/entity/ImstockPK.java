package com.asl.entity;

import java.io.Serializable;

import lombok.Data;
@Data
public class ImstockPK implements Serializable {

	private static final long serialVersionUID = 8387031971664588675L;
	
	private String zid;
	private String xitem;
	private String xwh;
	
}
