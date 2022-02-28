package com.asl.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * @author ASL
 * @since Jan 30, 2021
 */
@Data
public class XroleprivilegePK implements Serializable {

	private static final long serialVersionUID = -2415435591799865512L;

	private String zid;
	private String xrole;
	private String zscreen;
}
