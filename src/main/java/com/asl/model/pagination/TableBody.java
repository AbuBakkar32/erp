package com.asl.model.pagination;

import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Feb 14, 2022
 */
@Data
public class TableBody {
	private List<Map<String, Object>> lst;
	private long totalData;
}
