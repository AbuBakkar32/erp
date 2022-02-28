package com.asl.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Dec 30, 2020
 */
@Data
@AllArgsConstructor
public class Business {

	private String username;
	private String password;
	private String zid;
	private String businessName;
	private boolean central;
	private boolean branch;
}
