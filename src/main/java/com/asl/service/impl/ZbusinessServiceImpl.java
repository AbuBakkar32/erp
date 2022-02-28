package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.Cacus;
import com.asl.entity.Zbusiness;
import com.asl.mapper.ZbusinessMapper;
import com.asl.service.ZbusinessService;

/**
 * @author Zubayer Ahamed
 * @since Dec 30, 2020
 */
@Service
public class ZbusinessServiceImpl extends AbstractGenericService implements ZbusinessService{

	@Autowired private ZbusinessMapper businessMapper;

	@Override
	public long save(Zbusiness zbusiness) {
		if(zbusiness == null || StringUtils.isBlank(zbusiness.getZid())) return 0;
		zbusiness.setZauserid(getAuditUser());
		return businessMapper.save(zbusiness);
	}

	@Override
	public long update(Zbusiness zbusiness) {
		if(zbusiness == null || StringUtils.isBlank(zbusiness.getZid())) return 0;
		zbusiness.setZuuserid(getAuditUser());
		return businessMapper.update(zbusiness);
	}

	@Override
	public Zbusiness findBById(String zid) {
		if(StringUtils.isBlank(zid)) return null;
		return businessMapper.findByZid(zid);
	}
	
	@Override
	public List<Cacus> getBusinessName(String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();;
		return businessMapper.getBusinessName(hint.toUpperCase(), sessionManager.getBusinessId());
	}
	
	@Override
	public Zbusiness findfromZid() {
		return businessMapper.findByZid(sessionManager.getBusinessId());
	}
	
	@Override
	public List<Zbusiness> getAllBranchBusiness() {
		return businessMapper.getAllBranchBusiness(sessionManager.getBusinessId());
	}
	
	@Override
	public List<Zbusiness> getBranchBusiness() {
		return businessMapper.getBranchBusiness(sessionManager.getBusinessId());
	}

	@Override
	public Zbusiness getCentralBusiness() {
		return businessMapper.getCentralBusiness(sessionManager.getZbusiness().getCentralzid());
	}

}
