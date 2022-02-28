package com.asl.service;

import java.util.List;

import com.asl.entity.POSSettings;

public interface POSSettingsService {
	
	public long save(POSSettings pOSSettings);
	
	public long update(POSSettings pOSSettings);

	public List<POSSettings> getAllPOSSettings();

	public POSSettings findByPOSSettings(String xacc);

}
