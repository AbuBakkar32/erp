package com.asl.model;

import java.util.ArrayList;
import java.util.List;

import com.asl.entity.ProfileLine;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Apr 6, 2021
 */
@Data
public class ProfileLineWrapper {
	private List<ProfileLine> profileLines = new ArrayList<>();
	private boolean allchecked = true;
	private boolean allenabled = true;
}
