package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.LandSurveyor;

@Component
public interface LandSurveyorService {
	
	public long save(LandSurveyor landsurveyor);
	
	public long update(LandSurveyor landsurveyor);
	
	public long delete(LandSurveyor landsurveyor);

	public List<LandSurveyor> getAllLandSurveyor();

	public LandSurveyor findByLandSurveyor(String xsurveyor);
	
	public List<LandSurveyor> searchSurveyor(String xsurveyor);

}
