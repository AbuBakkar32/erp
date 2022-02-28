package com.asl.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Jul 25, 2021
 */
@Data
public class AcsubPK implements Serializable {

	private static final long serialVersionUID = 8121563738386039130L;

	private String zid;
	private String xacc;
	private String xsub;
}
