package com.asl.model.pagination;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Feb 14, 2022
 */
@Data
public class QueryColumn {
	private String column;
	private String prefix;
	private int index;
	private String sortType;
	private boolean link;
	private String linkUrl;
}
