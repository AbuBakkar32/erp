package com.asl.service;

import java.util.List;

import com.asl.entity.LandCommitteeInfo;
import com.asl.entity.LandCommitteeMembers;

public interface LandCommitteeInfoService {

	public long save(LandCommitteeInfo landcommiteeinfo);

	public long update(LandCommitteeInfo landcommiteeinfo);

	public long delete(LandCommitteeInfo landcommiteeinfo);
	
	public List<LandCommitteeInfo> getAllLandCommitteeInfo();

	public LandCommitteeInfo findByLandCommitteeInfo(String xcommittee);
	
	public List<LandCommitteeInfo> searchCommitteeId(String xcommittee);
	
	//for Committee Members
	
	public long save(LandCommitteeMembers landcommiteemembers);

	public long update(LandCommitteeMembers landcommiteemembers);
	
	public long deleteLandCommitteeMembers(LandCommitteeMembers landcommiteemembers);

	public List<LandCommitteeMembers> getAllLandCommitteeMembers();
	
	public List<LandCommitteeMembers> findByLandCommitteeMembers(String xcommittee);
	
	public LandCommitteeMembers findComMembersByXcommitteeAndXrow(String xcommittee,int xrow);

	public LandCommitteeMembers findByXcommitteeAndXperson(String xcommittee, String xperson);
}
