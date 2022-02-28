package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.LandCommitteeInfo;
import com.asl.entity.LandCommitteeMembers;
import com.asl.entity.LandOwner;
import com.asl.entity.LandPerson;

@Mapper
public interface LandCommitteeInfoMapper {
	
	public long saveLandCommitteeInfo(LandCommitteeInfo landcommiteeinfo);

	public long updateLandCommitteeInfo(LandCommitteeInfo landcommiteeinfo);

	public long deleteLandCommitteeInfo(LandCommitteeInfo landcommiteeinfo);
	
	public List<LandCommitteeInfo> getAllLandCommitteeInfo(String zid);

	public LandCommitteeInfo findByLandCommitteeInfo(String xcommittee, String zid);
	
	public List<LandCommitteeInfo> searchCommitteeId(String xcommittee, String zid);
	
	//for Committee Members
	
	public long saveLandCommitteeMembers(LandCommitteeMembers landcommiteemembers);

	public long updateLandCommitteeMembers(LandCommitteeMembers landcommiteemembers);
	
	public long deleteLandCommitteeMembers(LandCommitteeMembers landcommiteemembers);

	public List<LandCommitteeMembers> getAllLandCommitteeMembers(String zid);
	
	public List<LandCommitteeMembers> findByLandCommitteeMembers(String xcommittee, String zid);
	
	public LandCommitteeMembers findComMembersByXcommitteeAndXrow(String xcommittee,int xrow, String zid);

	public LandCommitteeMembers findByXcommitteeAndXperson(String xcommittee, String xperson, String zid);
}
