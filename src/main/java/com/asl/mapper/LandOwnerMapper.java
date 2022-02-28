package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.LandInfo;
import com.asl.entity.LandOwner;
import com.asl.entity.LandPerson;

@Mapper
public interface LandOwnerMapper {

	public long saveLandOwner(LandOwner landowner);

	public long updateLandOwner(LandOwner landowner);

	public List<LandOwner> getAllLandOwner(String zid);

	public LandOwner findByXlandAndXperson(String xland, String xperson, String zid);

	// search
	public List<LandInfo> searchLandId(String xland, String zid);

	public List<LandPerson> searchPersonId(String xperson, String zid);
	
	public List<LandPerson> searchPersonName(String xname, String zid);

}
