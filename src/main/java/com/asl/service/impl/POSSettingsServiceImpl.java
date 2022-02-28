package com.asl.service.impl;


import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.POSSettings;
import com.asl.mapper.POSSettingsMapper;
import com.asl.service.POSSettingsService;

@Service
public class POSSettingsServiceImpl extends AbstractGenericService implements POSSettingsService{

	@Autowired
	private POSSettingsMapper pOSSettingsMapper;
	
	@Transactional
	@Override
	public long save(POSSettings pOSSettings) {
		if (pOSSettings == null)
			return 0;
		pOSSettings.setZid(sessionManager.getBusinessId());
		pOSSettings.setZauserid(getAuditUser());
		return pOSSettingsMapper.savePOSSettings(pOSSettings);
	}

	@Transactional
	@Override
	public long update(POSSettings pOSSettings) {
		if (pOSSettings == null)
			return 0;
		pOSSettings.setZid(sessionManager.getBusinessId());
		pOSSettings.setZuuserid(getAuditUser());
		return pOSSettingsMapper.updatePOSSettings(pOSSettings);
	}

	@Override
	public List<POSSettings> getAllPOSSettings() {
		return pOSSettingsMapper.getAllPOSSettings(sessionManager.getBusinessId());
	}

	@Override
	public POSSettings findByPOSSettings(String xacc) {
		if (StringUtils.isBlank(xacc))
			return null;
		return pOSSettingsMapper.findByPOSSettings(xacc, sessionManager.getBusinessId());
	}
	
	
	

}
