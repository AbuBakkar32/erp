package com.asl.service;

import java.util.List;

import com.asl.entity.LandInfo;
import com.asl.entity.LandOwner;
import com.asl.entity.LandPerson;

public interface LandOwnerService {

	public long save(LandOwner landowner);

	public long update(LandOwner landowner);

	public List<LandOwner> getAllLandOwner();

	public LandOwner findByXlandAndXperson(String xland, String xperson);

	// Search
	public List<LandInfo> searchLandId(String xland);

	public List<LandPerson> searchPersonId(String xperson);
	
	public List<LandPerson> searchPersonName(String xname);
}
