package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.LandComEvent;
import com.asl.entity.LandDagDetails;
import com.asl.entity.LandEventsMember;
import com.asl.entity.LandOwner;



@Mapper
public interface LandComEventMapper {
	
	public long saveLandComEvent(LandComEvent landComEvent);
	
	public long updateLandComEvent(LandComEvent landComEvent);
	
	public long deleteLandComEvent(LandComEvent landComEvent);
	
	public List<LandComEvent> getAllLandComEvent(String zid);
	
	public List<LandComEvent> getAllLandOtherEvent(String zid);
	
	public LandComEvent findAllLandComEvent(String xevent, String zid);
	
	//for event members
	
	public long saveLandEventsMember(LandEventsMember landEventsMember);
	
	public long updateLandEventsMember(LandEventsMember landEventsMember);
	
	public long deleteLandEventsMember(LandEventsMember landEventsMember);
	
	public List<LandEventsMember> getAllLandEventsMember(String zid);
	
	public List<LandEventsMember> findByLandEventsMember(String xevent, String zid);
	
	public LandEventsMember findlandLandEventsMemberByXeventAndXrow(String xevent, int xrow, String zid);
	
	public LandEventsMember findByXeventAndXperson(String xevent, String xperson, String zid);
 	
	

}
