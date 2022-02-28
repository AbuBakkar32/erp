package com.asl.model.pagination;

import java.util.List;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Feb 14, 2022
 */
@Data
public class PaginationQueryHelper {

	private List<QueryColumn> queryColumns;
	private String baseTable;
	private String baseTableAlias;
	private String joinCondition;
	private String whereCondition;
	private String[] orderByColumns;
	
	private int dataLimit;
	private String dataHint;
	private int dataPage;
}
