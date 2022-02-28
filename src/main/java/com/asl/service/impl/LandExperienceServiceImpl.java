package com.asl.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.LandExperience;
import com.asl.mapper.LandExperienceMapper;
import com.asl.service.LandExperienceService;


@Service
public class LandExperienceServiceImpl extends AbstractGenericService implements LandExperienceService {

	
	@Autowired
	private LandExperienceMapper landExperienceMapper;
	
	@Transactional
	@Override
	public long save(LandExperience landExperience) {
		if (landExperience == null)
			return 0;
		landExperience.setZid(sessionManager.getBusinessId());
		landExperience.setZauserid(getAuditUser());
		return landExperienceMapper.saveExPerson(landExperience);
	}

	@Transactional
	@Override
	public long update(LandExperience landExperience) {
		if (landExperience == null)
			return 0;
		landExperience.setZid(sessionManager.getBusinessId());
		landExperience.setZuuserid(getAuditUser());
		return landExperienceMapper.updateExPerson(landExperience);
	}

	@Override
	public List<LandExperience> getAllLandExperience() {
		return landExperienceMapper.getAllLandExperience(sessionManager.getBusinessId());
	}

	@Override
	public LandExperience findByLandExperiencePerson(String xperson) {
		if (StringUtils.isBlank(xperson))
			return null;
		return landExperienceMapper.findByLandExperiencePerson(xperson, sessionManager.getBusinessId());
	}
	
	@Override
	public LandExperience findByXpersonAndXrow(String xperson, int xrow) {
		if(StringUtils.isBlank(xperson) || xrow == 0) return null;
		return landExperienceMapper.findByXpersonAndXrow(xperson, xrow, sessionManager.getBusinessId());
	}
	
	@Override
	public List<LandExperience> findByPersonExperience(String xperson){
		if (StringUtils.isBlank(xperson)) return null;
		return landExperienceMapper.findByPersonExperience(xperson, sessionManager.getBusinessId());
	}
	
	@Override
	public LandExperience findByXpersonAndXname(String xperson, String xname) {
		if(StringUtils.isBlank(xperson) || StringUtils.isBlank(xname)) return null;
		return landExperienceMapper.findByXpersonAndXname(xperson, xname, sessionManager.getBusinessId());
	}
	
	@Override
	public long deleteDetail(LandExperience landExperience){
		if(landExperience==null) return 0;
		long count = landExperienceMapper.deleteDetail(landExperience);
		return count;
	}
	
}
