package com.asl.entity;
import java.io.Serializable;

import lombok.Data;
@Data
public class ImtdetPK implements Serializable {
	
	private static final long serialVersionUID = 8363576717117256571L;
	
	private String zid;
	private String xtagnum;
	private int xrow;
}
