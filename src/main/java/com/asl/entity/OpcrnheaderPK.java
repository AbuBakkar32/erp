package com.asl.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class OpcrnheaderPK  implements Serializable {

	private static final long serialVersionUID = -8026900927669838706L;

	private String zid;
	private String xcrnnum;
}
