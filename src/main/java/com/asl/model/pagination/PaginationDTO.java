package com.asl.model.pagination;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Jan 29, 2022
 */
@Data
public class PaginationDTO {

	private long page;
	private long limit;
	private String hint;
	private String orderBy;
	private String sortType;
}
