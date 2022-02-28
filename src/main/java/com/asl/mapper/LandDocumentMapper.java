 package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.LandDocument;
import com.asl.entity.LandSurveyor;

@Mapper
public interface LandDocumentMapper {
	
	public long saveLandDocument(LandDocument LandDocument);
	
	public long updateLandDocument(LandDocument LandDocument);

	public List<LandDocument> getAllLandDocument(String zid);

	public LandDocument findByLandDocument(String xdoc, String zid);
	
	public List<LandDocument> findByLandPersonDocument(String xperson, String zid);
	
	public List<LandDocument> findByLandSurveyorDocument(String xsurveyor, String zid);
	
	public List<LandDocument> findByAllLandDocument(String xland, String zid);
	
	public List<LandDocument> findByAllProjectDocument(String xproject, String zid);
	
	public List<LandDocument> findByAllProjectsSiteDocument(String xproject, String zid);
	
	public List<LandDocument> findByAllSiteDocument(String xwh, String zid);
	
	public LandDocument findLandPersonDocumentByXpersonAndXrow(String xperson, int xrow, String zid);
	
	public LandDocument findLandSurveyorDocumentBySurveyorAndXrow(String xsurveyor, int xrow, String zid);
	
	public LandDocument findLandLandDocumentByLandAndXrow(String xland, int xrow, String zid);
	
	public LandDocument findDocumentByXprojectAndXrow(String xproject, int xrow, String zid);
	
	public LandDocument findSiteDocumentByXwhAndXrow(String xwh, int xrow, String zid);
	
	public long deleteDetail(LandDocument LandDocument);
	
	//Search
	public List<LandSurveyor> searchServeyorId(String xsurveyor, String zid);
	
	//HR Document
	public List<LandDocument> findByHRDocument(String xstaff, String zid);
	public LandDocument findHRDocumentByXstaffAndXrow(String xstaff, int xrow, String zid);
	
}
