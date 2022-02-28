package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.LandSurvey;

@Component
public interface LandSurveyService {

	public long save(LandSurvey landSurvey);
	
	public long update(LandSurvey landSurvey);
	
	public long deleteDetail(LandSurvey landSurvey);
	
	public List<LandSurvey> getAllLandSurvey();
	
	public List<LandSurvey> findByXlandSurvey(String xland);
	
	public LandSurvey findLandSurveydetailByXlandAndXrow(String xland, int xrow);
	
	public LandSurvey findLandSurveyByXlandAndXsurveyor(String xland, String xsurveyor);
	
	public long updatesurveyinfo(String xland,int xrow);
	public long updatesurveystatusopen(String xland,int xrow);
	public long updatesurveystatusconfirmed(String xland,int xrow);
}
