package com.asl.model;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Zubayer Ahamed
 * @since Aug 23, 2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ModuleOption {

	public enum OptionType {
		CHCKBOX,
		SEARCH;
	}

	private String promt;
	private String name;
	private String id;
	private boolean hiddenAndChecked;
	private String dependentPromt;
	private String dependentName;
	private Map<String, String> dependentOptions;
	
	/*Selected data from client side only*/
	private List<String> selectedData;
}
