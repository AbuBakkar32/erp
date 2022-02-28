package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.LandDocument;
import com.asl.entity.LandSurveyor;

@Component
public interface LandDocumentService {

	public long save(LandDocument landDocument);
	
	public long update(LandDocument landDocument);

	public List<LandDocument> getAllLandDocument();

	public LandDocument findByLandDocument(String xdoc);
	
	public List<LandDocument> findByLandPersonDocument(String xperson);
	
	public List<LandDocument> findByLandSurveyorDocument(String xsurveyor);
	
	public List<LandDocument> findByAllLandDocument(String xland);
	
	public List<LandDocument> findByAllProjectDocument(String xproject);
	
	public List<LandDocument> findByAllProjectsSiteDocument(String xproject);
	
	public List<LandDocument> findByAllSiteDocument(String xwh);
	
	public LandDocument findLandPersonDocumentByXpersonAndXrow(String xperson, int xrow);
	
	public LandDocument findLandSurveyorDocumentBySurveyorAndXrow(String xsurveyor, int xrow);
	
	public LandDocument findLandLandDocumentByLandAndXrow(String xland, int xrow);
	
	public LandDocument findDocumentByXprojectAndXrow(String xproject, int xrow);
	
	public LandDocument findSiteDocumentByXwhAndXrow(String xwh, int xrow);
	
	public long deleteDetail(LandDocument LandDocument);
	
	//Search
	public List<LandSurveyor> searchServeyorId(String xsurveyor);
	
	//HR Document
	public List<LandDocument> findByHRDocument(String xstaff);
	public LandDocument findHRDocumentByXstaffAndXrow(String xstaff, int xrow);
}
