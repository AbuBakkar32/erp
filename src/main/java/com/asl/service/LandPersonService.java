package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.LandPerson;
import com.asl.entity.LandEducation;

@Component
public interface LandPersonService {
	
	public long save(LandPerson landperson);
	
	public long update(LandPerson landperson);
	
	public long delete(LandPerson landperson);

	public List<LandPerson> getAllLandPerson();

	public LandPerson findByLandPerson(String xperson);
	
	public List<LandPerson> searchPersonId(String xperson);
	
	
	//For Education
	public long save(LandEducation landEducation);
	
	public long update(LandEducation landEducation);
	
	public long deleteDetail(LandEducation landEducation);

	public List<LandEducation> getAllPersonEducation();

	public List<LandEducation> findByPersonEducation(String xperson);
	
	public LandEducation findLandEducationdetailByXpersonAndXrow(String xperson, int xrow);
	
	public LandEducation findLandEducationByXpersonAndXexam(String xperson, String xexam);

}
