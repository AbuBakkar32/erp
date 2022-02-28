package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.LandDagDetails;
import com.asl.entity.LandEducation;
import com.asl.entity.LandEvents;
import com.asl.entity.LandInfo;
import com.asl.entity.LandOwner;
import com.asl.entity.LandPerson;
@Mapper
public interface LandInfoMapper {
	
	public long saveLandInfo(LandInfo landinfo);
	
	public long updateLandInfo(LandInfo landinfo);

	public long deleteLandInfo(LandInfo landinfo);
	
	public List<LandInfo> getAllLandInfo(String zid);

	public LandInfo findByLandInfo(String xland, String zid);
	
	public List<LandInfo> searchLandId(String xland, String zid);
	
	public List<LandInfo> searchMemLand(String xland, String zid);

	public List<LandInfo> getMemLand(String xcus, String zid);
	
	//for owner
	public long saveLandOwner(LandOwner landOwner);
	
	public long updateLandOwner(LandOwner landOwner);
	
	public long deleteLandOwner(LandOwner landOwner);
	
	public List<LandOwner> getAllLandOwner(String zid);
	
	public List<LandOwner> findByLandOwner(String xland, String zid);
	
	public LandOwner findLandOwnerByXlandAndXrow(String xland, int xrow, String zid);
	
	public LandOwner findByXlandAndXperson(String xland, String xperson, String zid);
	
	//for dag details
	
	public long saveLandDagDetails(LandDagDetails landDagDetails);
	
	public long updateLandDagDetails(LandDagDetails landDagDetails);
	
	public long deleteLandDagDetails(LandDagDetails landDagDetails);
	
	public List<LandDagDetails> getAllLandDagDetails(String zid);
	
	public List<LandDagDetails> findByLandDagDetails(String xland, String zid);
	
	public LandDagDetails findlandDagDetailsByXlandAndXrow(String xland, int xrow, String zid);

	//for Land Events
	
	public long saveLandEvents(LandEvents landEvents);
	
	public long updateLandEvents(LandEvents landEvents);
	
	public long deleteLandEvents(LandEvents landEvents);
	
	public List<LandEvents> getAllLandEvents(String zid);
	
	public List<LandEvents> findByLandEvents(String xland, String zid);
	
	public LandEvents findLandEventsByXlandAndXrow(String xland, int xrow, String zid);



}
