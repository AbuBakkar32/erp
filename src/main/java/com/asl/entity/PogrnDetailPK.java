package com.asl.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class PogrnDetailPK implements Serializable {

	private static final long serialVersionUID = -1527020582421587375L;

	private String zid;
	private String xgrnnum;
	private String xrow;

}
