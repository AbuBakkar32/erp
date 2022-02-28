package com.asl.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Dec 27, 2020
 */
@Data
@AllArgsConstructor
public class DropdownOption {

	private String value;
	private String prompt;
}
