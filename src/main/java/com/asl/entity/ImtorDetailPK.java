package com.asl.entity;
import java.io.Serializable;

import lombok.Data;
@Data
public class ImtorDetailPK implements Serializable {

	private static final long serialVersionUID = -7433240907373614569L;
	
	private String zid;
	private String xtornum;
	private int xrow;
}
