package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.LandInfo;
import com.asl.entity.LandOwner;
import com.asl.entity.LandPerson;
import com.asl.mapper.LandOwnerMapper;
import com.asl.service.LandOwnerService;

@Service
public class LandOwnerServiceImpl extends AbstractGenericService implements LandOwnerService {

	@Autowired
	private LandOwnerMapper landownerMapper;

	@Transactional
	@Override
	public long save(LandOwner landOwner) {
		if (landOwner == null)
			return 0;
		landOwner.setZid(sessionManager.getBusinessId());
		landOwner.setZauserid(getAuditUser());
		return landownerMapper.saveLandOwner(landOwner);
	}

	@Transactional
	@Override
	public long update(LandOwner landOwner) {
		if (landOwner == null)
			return 0;
		landOwner.setZid(sessionManager.getBusinessId());
		landOwner.setZuuserid(getAuditUser());
		return landownerMapper.updateLandOwner(landOwner);
	}

	@Override
	public List<LandOwner> getAllLandOwner() {
		return landownerMapper.getAllLandOwner(sessionManager.getBusinessId());
	}

	@Override
	public LandOwner findByXlandAndXperson(String xland, String xperson) {
		if (StringUtils.isBlank(xland) || StringUtils.isBlank(xperson))
			return null;
		return landownerMapper.findByXlandAndXperson(xland, xperson, sessionManager.getBusinessId());
	}

	@Override
	public List<LandInfo> searchLandId(String xland) {
		if(StringUtils.isBlank(xland)) return Collections.emptyList();
		return landownerMapper.searchLandId(xland.toUpperCase(), sessionManager.getBusinessId());
	}

	@Override
	public List<LandPerson> searchPersonId(String xperson) {
		if(StringUtils.isBlank(xperson)) return Collections.emptyList();
		return landownerMapper.searchPersonId(xperson.toUpperCase(), sessionManager.getBusinessId());
	}
	
	@Override
	public List<LandPerson> searchPersonName(String xname){
		if(StringUtils.isBlank(xname)) return Collections.emptyList();
		return landownerMapper.searchPersonName(xname.toUpperCase(), sessionManager.getBusinessId());
	}

}
