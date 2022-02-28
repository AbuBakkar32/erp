package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.ProfileAllocation;
import com.asl.mapper.ProfileAllocationMapper;
import com.asl.service.ProfileAllocationService;

/**
 * @author Zubayer Ahamed
 * @since Jan 5, 2021
 */
@Service
public class ProfileAllocationServiceImpl extends AbstractGenericService implements ProfileAllocationService {

	@Autowired private ProfileAllocationMapper profileAllocationMapper;

	@Override
	public long save(ProfileAllocation profileAllocation) {
		if(profileAllocation == null || StringUtils.isBlank(profileAllocation.getZemail())) return 0;

		profileAllocation.setZid(sessionManager.getBusinessId());
		profileAllocation.setZauserid(getAuditUser());
		return profileAllocationMapper.save(profileAllocation);
	}

	@Override
	public long update(ProfileAllocation profileAllocation) {
		if(profileAllocation == null 
				|| StringUtils.isBlank(profileAllocation.getZemail()) 
				|| StringUtils.isBlank(profileAllocation.getPaid())) return 0;

		profileAllocation.setZid(sessionManager.getBusinessId());
		profileAllocation.setZuuserid(getAuditUser());
		return profileAllocationMapper.update(profileAllocation);
	}

	@Override
	public ProfileAllocation findByPaid(String paid) {
		if(StringUtils.isBlank(paid)) return null;
		return profileAllocationMapper.findByPaid(paid, sessionManager.getBusinessId());
	}

	@Override
	public List<ProfileAllocation> getAllProfileAllocation() {
		List<ProfileAllocation> list = profileAllocationMapper.getAllProfileAllocation(sessionManager.getBusinessId());
		if(list == null) return Collections.emptyList();
		return list;
	}

	@Override
	public ProfileAllocation findByZemail(String username) {
		if(StringUtils.isBlank(username)) return null;
		return profileAllocationMapper.findByZemail(username, sessionManager.getBusinessId()); 
	}

	
}
