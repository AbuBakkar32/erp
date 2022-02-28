package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.LandPerson;
import com.asl.entity.PoordDetail;
import com.asl.entity.LandEducation;

@Mapper
public interface LandPersonMapper {
	
	public long saveLandPerson(LandPerson landperson);
	
	public long updateLandPerson(LandPerson landperson);

	public long deleteLandPerson(LandPerson landperson);
	
	public List<LandPerson> getAllLandPerson(String zid);

	public LandPerson findByLandPerson(String xperson, String zid);
	
	public List<LandPerson> searchPersonId(String xperson, String zid);
	
	//For education
	public long savePersonEducation(LandEducation landEducation);
	
	public long updatePersonEducation(LandEducation landEducation);
	
	public long deleteDetail(LandEducation landEducation);
	
	public List<LandEducation> getAllPersonEducation(String zid);
	
	public List<LandEducation> findByPersonEducation(String xperson, String zid);
	
	public LandEducation findLandEducationdetailByXpersonAndXrow(String xperson, int xrow, String zid);
	
	public LandEducation findLandEducationByXpersonAndXexam(String xperson, String xexam,  String zid);

	
}
