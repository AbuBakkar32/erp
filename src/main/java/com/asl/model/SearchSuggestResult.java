package com.asl.model;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zubayer Ahamed
 * @since Dec 27, 2020
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchSuggestResult {

	private String value;
	private String prompt;
	private boolean tableMood;
	private String[] columns;
	private List<String[]> data;
	private int valueindex;
	private int[] promptindex;

	public SearchSuggestResult(String value, String prompt) {
		this.value = value;
		this.prompt = prompt;
	}
}
