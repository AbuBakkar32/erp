package com.asl.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class PoordDetailPK implements Serializable {

	private static final long serialVersionUID = 2134289175594921540L;

	private String zid;
	private String xpornum;
	private int xrow;

}
