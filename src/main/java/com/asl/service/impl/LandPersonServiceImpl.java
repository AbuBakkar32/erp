package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.LandPerson;
import com.asl.entity.PoordDetail;
import com.asl.entity.LandEducation;
import com.asl.mapper.LandPersonMapper;
import com.asl.service.LandPersonService;


@Service
public class LandPersonServiceImpl extends AbstractGenericService implements LandPersonService {

	
	@Autowired
	private LandPersonMapper landpersonMapper;
	
	@Transactional
	@Override
	public long save(LandPerson landPerson) {
		if (landPerson == null)
			return 0;
		landPerson.setZid(sessionManager.getBusinessId());
		landPerson.setZauserid(getAuditUser());
		return landpersonMapper.saveLandPerson(landPerson);
	}

	@Transactional
	@Override
	public long update(LandPerson landPerson) {
		if (landPerson == null)
			return 0;
		landPerson.setZid(sessionManager.getBusinessId());
		landPerson.setZuuserid(getAuditUser());
		return landpersonMapper.updateLandPerson(landPerson);
	}

	@Override
	public long delete(LandPerson landperson) {
		if(landperson == null) return 0;
		long count = landpersonMapper.deleteLandPerson(landperson);
		return count;
	}
	
	@Override
	public List<LandPerson> getAllLandPerson() {
		return landpersonMapper.getAllLandPerson(sessionManager.getBusinessId());
	}

	@Override
	public LandPerson findByLandPerson(String xperson) {
		if (StringUtils.isBlank(xperson))
			return null;
		return landpersonMapper.findByLandPerson(xperson, sessionManager.getBusinessId());
	}
	
	@Override
	public List<LandPerson> searchPersonId(String hint) {
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		return landpersonMapper.searchPersonId(hint.toUpperCase(), sessionManager.getBusinessId());
	}
	
	//For Education
	
	@Transactional
	@Override
	public long save(LandEducation landEducation) {
		if (landEducation == null)
			return 0;
		landEducation.setZid(sessionManager.getBusinessId());
		landEducation.setZauserid(getAuditUser());
		return landpersonMapper.savePersonEducation(landEducation);
	}
	
	@Transactional
	@Override
	public long update(LandEducation landEducation) {
		if (landEducation == null)
			return 0;
		landEducation.setZid(sessionManager.getBusinessId());
		landEducation.setZuuserid(getAuditUser());
		return landpersonMapper.updatePersonEducation(landEducation);
	}
	
	@Override
	public List<LandEducation> getAllPersonEducation() {
		return landpersonMapper.getAllPersonEducation(sessionManager.getBusinessId());
	}

	@Override
	public List<LandEducation> findByPersonEducation(String xperson) {
		if (StringUtils.isBlank(xperson))
			return null;
		return landpersonMapper.findByPersonEducation(xperson, sessionManager.getBusinessId());
	}
	
	@Override
	public LandEducation findLandEducationdetailByXpersonAndXrow(String xperson, int xrow) {
		if(StringUtils.isBlank(xperson) || xrow == 0) return null;
		return landpersonMapper.findLandEducationdetailByXpersonAndXrow(xperson,xrow,sessionManager.getBusinessId());
	}
	
	@Override
	public LandEducation findLandEducationByXpersonAndXexam(String xperson, String xexam) {
		if(StringUtils.isBlank(xperson) || StringUtils.isBlank(xexam)) return null;
		return landpersonMapper.findLandEducationByXpersonAndXexam(xperson, xexam, sessionManager.getBusinessId());
	}
	
	@Override
	public long deleteDetail(LandEducation landEducation) {
		if(landEducation == null) return 0;
		long count = landpersonMapper.deleteDetail(landEducation);
		return count;
	}


}
