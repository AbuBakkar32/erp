package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.LandSurvey;

@Mapper
public interface LandSurveyMapper {
	

	public long saveLandSurvey(LandSurvey landSurvey);
	
	public long updateLandSurvey(LandSurvey landSurvey);
	
	public long deleteDetail(LandSurvey landSurvey);
	
	public List<LandSurvey> getAllLandSurvey(String zid);
	
	public List<LandSurvey> findByXlandSurvey(String xland, String zid);
	
	public LandSurvey findLandSurveydetailByXlandAndXrow(String xland, int xrow, String zid);
	
	public LandSurvey findLandSurveyByXlandAndXsurveyor(String xland, String xsurveyor,  String zid);
	
	public long updatesurveyinfo(String xland,String zid,int xrow);
	public long updatesurveystatusopen(String xland,String zid,int xrow);
	public long updatesurveystatusconfirmed(String xland,String zid,int xrow);
}
