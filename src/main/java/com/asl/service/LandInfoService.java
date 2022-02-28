package com.asl.service;

import java.util.List;

import com.asl.entity.LandDagDetails;
import com.asl.entity.LandEvents;
import com.asl.entity.LandInfo;
import com.asl.entity.LandOwner;

public interface LandInfoService {

	public long save(LandInfo landinfo);

	public long update(LandInfo landinfo);

	public long delete(LandInfo landinfo);

	public List<LandInfo> getAllLandInfo();

	public LandInfo findByLandInfo(String xland);

	public List<LandInfo> searchLandId(String xland);

	public List<LandInfo> searchMemLand(String xland);

	public List<LandInfo> getMemLand(String xcus);
	
	// for owner
	public long save(LandOwner landOwner);

	public long update(LandOwner landOwner);

	public long deleteLandOwner(LandOwner landOwner);

	public List<LandOwner> getAllLandOwner(String zid);

	public List<LandOwner> findByLandOwner(String xland);

	public LandOwner findLandOwnerByXlandAndXrow(String xland, int xrow);

	public LandOwner findByXlandAndXperson(String xland, String xperson);

	// for dag details

	public long save(LandDagDetails landDagDetails);

	public long update(LandDagDetails landDagDetails);

	public long deleteLandDagDetails(LandDagDetails landDagDetails);

	public List<LandDagDetails> getAllLandDagDetails(String zid);

	public List<LandDagDetails> findByLandDagDetails(String xland);

	public LandDagDetails findlandDagDetailsByXlandAndXrow(String xland, int xrow);

	// for Land Events

	public long save(LandEvents landEvents);

	public long update(LandEvents landEvents);

	public long deleteLandEvents(LandEvents landEvents);

	public List<LandEvents> getAllLandEvents();

	public List<LandEvents> findByLandEvents(String xland);

	public LandEvents findLandEventsByXlandAndXrow(String xland, int xrow);

}
