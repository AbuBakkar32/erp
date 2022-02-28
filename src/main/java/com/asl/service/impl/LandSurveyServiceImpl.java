package com.asl.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.mapper.LandSurveyMapper;
import com.asl.service.LandSurveyService;
import com.asl.entity.LandSurvey;

@Service
public class LandSurveyServiceImpl extends AbstractGenericService implements LandSurveyService{
	
	@Autowired
	private LandSurveyMapper landSurveyMapper;
	
	@Transactional
	@Override
	public long save(LandSurvey landSurvey) {
		if (landSurvey == null)
			return 0;
		landSurvey.setZid(sessionManager.getBusinessId());
		landSurvey.setZauserid(getAuditUser());
		return landSurveyMapper.saveLandSurvey(landSurvey);
	}
	
	@Transactional
	@Override
	public long update(LandSurvey landSurvey) {
		if (landSurvey == null)
			return 0;
		landSurvey.setZid(sessionManager.getBusinessId());
		landSurvey.setZuuserid(getAuditUser());
		return landSurveyMapper.updateLandSurvey(landSurvey);
	}
	
	@Override
	public List<LandSurvey> getAllLandSurvey() {
		return landSurveyMapper.getAllLandSurvey(sessionManager.getBusinessId());
	}
	
	@Override
	public List<LandSurvey> findByXlandSurvey(String xland) {
		if (StringUtils.isBlank(xland))
			return null;
		return landSurveyMapper.findByXlandSurvey(xland, sessionManager.getBusinessId());
	}
	
	@Override
	public LandSurvey findLandSurveydetailByXlandAndXrow(String xland, int xrow) {
		if(StringUtils.isBlank(xland)) return null;
		return landSurveyMapper.findLandSurveydetailByXlandAndXrow(xland,xrow,sessionManager.getBusinessId());
	}
	
	@Override
	public LandSurvey findLandSurveyByXlandAndXsurveyor(String xland, String xsurveyor) {
		if(StringUtils.isBlank(xland) || StringUtils.isBlank(xsurveyor)) return null;
		return landSurveyMapper.findLandSurveyByXlandAndXsurveyor(xland, xsurveyor, sessionManager.getBusinessId());
	}

	@Transactional
	@Override
	public long deleteDetail(LandSurvey landSurvey) {
		if(landSurvey == null) return 0;
		long count = landSurveyMapper.deleteDetail(landSurvey);
		return count;
	}

	@Transactional
	@Override
	public long updatesurveyinfo(String xland, int xrow) {
		if(StringUtils.isBlank(xland)|| xrow == 0) return 0;
		return landSurveyMapper.updatesurveyinfo(xland,sessionManager.getBusinessId(),xrow);
	}

	@Override
	public long updatesurveystatusopen(String xland, int xrow) {
		if(StringUtils.isBlank(xland)|| xrow == 0) return 0;
		return landSurveyMapper.updatesurveystatusopen(xland,sessionManager.getBusinessId(),xrow);
	}

	@Override
	public long updatesurveystatusconfirmed(String xland, int xrow) {
		if(StringUtils.isBlank(xland)|| xrow == 0) return 0;
		return landSurveyMapper.updatesurveystatusconfirmed(xland,sessionManager.getBusinessId(),xrow);
	}
}
