package com.asl.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class POSSettingsPK implements Serializable{

	private static final long serialVersionUID = -1012940027717134430L;
	private String zid;
	private String xacc;
}
