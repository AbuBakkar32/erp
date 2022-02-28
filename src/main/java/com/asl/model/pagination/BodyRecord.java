package com.asl.model.pagination;

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
public class BodyRecord {

	private int index;
	private String value;
}
