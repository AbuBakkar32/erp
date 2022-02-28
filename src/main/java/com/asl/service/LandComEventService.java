package com.asl.service;

import java.util.List;

import com.asl.entity.LandComEvent;
import com.asl.entity.LandEventsMember;
import com.asl.entity.LandOwner;

public interface LandComEventService {
	
	public long saveLandComEvent(LandComEvent landComEvent);
	
	public long updateLandComEvent(LandComEvent landComEvent);
	
	public long delete(LandComEvent landComEvent);
	
	public List<LandComEvent> getAllLandComEvent();
	
	public List<LandComEvent> getAllLandOtherEvent();
	
	public LandComEvent findAllLandComEvent(String xevent);
	
	//for event members
	
	public long save(LandEventsMember landEventsMember);
	
	public long update(LandEventsMember landEventsMember);
	
	public long deleteLandEventsMember(LandEventsMember landEventsMember);
	
	public List<LandEventsMember> getAllLandEventsMember();
	
	public List<LandEventsMember> findByLandEventsMember(String xevent);
	
	public LandEventsMember findlandLandEventsMemberByXeventAndXrow(String xevent, int xrow);
	
	public LandEventsMember findByXeventAndXperson(String xevent, String xperson);
	
	

}
