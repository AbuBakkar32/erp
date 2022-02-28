package com.asl.model.pagination;

import com.asl.enums.ColumnSortType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zubayer Ahamed
 * @since Feb 22, 2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeaderColumn {

	private int index;
	private String prompt;
	private String dbColumn;
	private ColumnSortType sortType;
	
	String title;
	String type;
	Boolean hidden;
	String name;
}
