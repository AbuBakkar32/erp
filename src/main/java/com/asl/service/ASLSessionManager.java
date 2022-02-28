package com.asl.service;

import com.asl.entity.Pdmst;
import com.asl.entity.Zbusiness;
import com.asl.model.LoggedInUserDetails;

/**
 * @author Zubayer Ahamed
 * @since Dec 28, 2020
 */
public interface ASLSessionManager {

	public void addToMap(String key, Object value);

	public Object getFromMap(String key);

	public void removeFromMap(String key);

	public LoggedInUserDetails getLoggedInUserDetails();

	public String getBusinessId();

	public Zbusiness getZbusiness();

	public boolean isCentral();

	public boolean isBranch();
	
	
	
}
