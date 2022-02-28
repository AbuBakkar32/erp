package com.asl.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Mar 22, 2021
 */
@Data
public class ProductionSuggestionPK implements Serializable {
	private static final long serialVersionUID = 3502245604326855519L;
	private String zid;
	private String chalan;
	private String xitem;
}
