package com.asl.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class PoordHeaderPK implements Serializable {

	private static final long serialVersionUID = -8853164314183524454L;

	private String zid;
	private String xpornum;
}
