package com.asl.model.pagination;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Feb 22, 2021
 */
@Data
public class PaginationHelper {

	private List<Integer> limits = new ArrayList<>(Arrays.asList(10,20,50,100));
	private int defaultLimit = 10;

	private String hint;

	private List<HeaderColumn> headers = new ArrayList<>();

	private List<List<BodyRecord>> records = new ArrayList<>();

	private String selectedPage;
}
