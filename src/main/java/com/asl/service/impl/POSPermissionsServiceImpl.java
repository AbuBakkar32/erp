package com.asl.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.POSPermissions;
import com.asl.mapper.POSPermissionsMapper;
import com.asl.service.POSPermissionsService;

@Service
public class POSPermissionsServiceImpl extends AbstractGenericService implements POSPermissionsService {

	@Autowired
	private POSPermissionsMapper posPermissionsMapper;
	
	@Transactional
	@Override
	public long save(POSPermissions posPermissions) {
		if (posPermissions == null)
			return 0;
		posPermissions.setZid(sessionManager.getBusinessId());
		posPermissions.setZauserid(getAuditUser());
		return posPermissionsMapper.savePOSPermissions(posPermissions);
	}

	@Transactional
	@Override
	public long update(POSPermissions posPermissions) {
		if (posPermissions == null)
			return 0;
		posPermissions.setZid(sessionManager.getBusinessId());
		posPermissions.setZuuserid(getAuditUser());
		return posPermissionsMapper.updatePOSPermissions(posPermissions);
	}

	@Override
	public List<POSPermissions> getAllPOSPermissions() {
		return posPermissionsMapper.getAllPOSPermissions(sessionManager.getBusinessId());
	}

	@Override
	public POSPermissions findByPOSPermissionsByXaccAndXrole(String xacc, String xrole) {
		if (StringUtils.isBlank(xacc) || StringUtils.isBlank(xrole)) return null;
		return posPermissionsMapper.findPOSPermissionsByXaccAndXrole(xacc, xrole, sessionManager.getBusinessId());
	}

	
}
