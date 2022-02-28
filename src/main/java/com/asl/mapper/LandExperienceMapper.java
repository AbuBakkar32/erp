package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.LandExperience;


@Mapper
public interface LandExperienceMapper {
	
	public long saveExPerson(LandExperience landExperience);
	
	public long updateExPerson(LandExperience landExperience);

	public List<LandExperience> getAllLandExperience(String zid);

	public LandExperience findByLandExperiencePerson(String xperson, String zid);
	
	public List<LandExperience> findByPersonExperience(String xperson, String zid);
	
	public LandExperience findByXpersonAndXrow(String xperson, int xrow, String zid);
	
	public LandExperience findByXpersonAndXname(String xperson, String xname, String zid);
	
	public long deleteDetail(LandExperience landExperience);

}
