package com.asl.entity;
import java.io.Serializable;

import lombok.Data;
@Data
public class ModetailPK implements Serializable {
	
	private static final long serialVersionUID = 5063995465596782239L;
	
	private String zid;
	private int xrow;
	private String xbatch;
}
