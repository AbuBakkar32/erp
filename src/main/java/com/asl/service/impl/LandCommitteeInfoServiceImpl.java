package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.LandCommitteeInfo;
import com.asl.entity.LandCommitteeMembers;
import com.asl.entity.LandPerson;
import com.asl.mapper.LandCommitteeInfoMapper;
import com.asl.service.LandCommitteeInfoService;
@Service
public class LandCommitteeInfoServiceImpl extends AbstractGenericService implements LandCommitteeInfoService{

	@Autowired private LandCommitteeInfoMapper  landcommitteeinfoMapper;
	@Override
	public long save(LandCommitteeInfo landCommiteeinfo) {
		if (landCommiteeinfo == null)
			return 0;
		landCommiteeinfo.setZid(sessionManager.getBusinessId());
		landCommiteeinfo.setZauserid(getAuditUser());
		return landcommitteeinfoMapper.saveLandCommitteeInfo(landCommiteeinfo);
	}

	@Override
	public long update(LandCommitteeInfo landCommiteeinfo) {
		if (landCommiteeinfo == null)
			return 0;
		landCommiteeinfo.setZid(sessionManager.getBusinessId());
		landCommiteeinfo.setZauserid(getAuditUser());
		return landcommitteeinfoMapper.updateLandCommitteeInfo(landCommiteeinfo);
	}

	@Override
	public long delete(LandCommitteeInfo landcommiteeinfo) {
		if(landcommiteeinfo == null) return 0;
		long count = landcommitteeinfoMapper.deleteLandCommitteeInfo(landcommiteeinfo);
		return count;
	}
	
	@Override
	public List<LandCommitteeInfo> getAllLandCommitteeInfo() {
		return landcommitteeinfoMapper.getAllLandCommitteeInfo(sessionManager.getBusinessId());
	}

	@Override
	public LandCommitteeInfo findByLandCommitteeInfo(String xcommittee) {
		if (StringUtils.isBlank(xcommittee))
			return null;
		return landcommitteeinfoMapper.findByLandCommitteeInfo(xcommittee, sessionManager.getBusinessId());
	}

	@Override
	public List<LandCommitteeInfo> searchCommitteeId(String xcommittee) {
		if(StringUtils.isBlank(xcommittee)) return Collections.emptyList();
		return landcommitteeinfoMapper.searchCommitteeId(xcommittee.toUpperCase(), sessionManager.getBusinessId());
	}
	//for LandCommitteeMembers
	
	@Transactional
	@Override
	public long save(LandCommitteeMembers landcommiteemembers) {
		if (landcommiteemembers == null)
			return 0;
		landcommiteemembers.setZid(sessionManager.getBusinessId());
		landcommiteemembers.setZauserid(getAuditUser());
		return landcommitteeinfoMapper.saveLandCommitteeMembers(landcommiteemembers);
	}

	@Transactional
	@Override
	public long update(LandCommitteeMembers landcommiteemembers) {
		if (landcommiteemembers == null)
			return 0;
		landcommiteemembers.setZid(sessionManager.getBusinessId());
		landcommiteemembers.setZauserid(getAuditUser());
		return landcommitteeinfoMapper.updateLandCommitteeMembers(landcommiteemembers);
	}

	@Override
	public long deleteLandCommitteeMembers(LandCommitteeMembers landcommiteemembers) {
		if(landcommiteemembers == null) return 0;
		long count = landcommitteeinfoMapper.deleteLandCommitteeMembers(landcommiteemembers);
		return count;
	}

	@Override
	public List<LandCommitteeMembers> getAllLandCommitteeMembers() {
		return landcommitteeinfoMapper.getAllLandCommitteeMembers(sessionManager.getBusinessId());
	}

	@Override
	public List<LandCommitteeMembers> findByLandCommitteeMembers(String xcommittee) {
		if (StringUtils.isBlank(xcommittee))
			return null;
		return landcommitteeinfoMapper.findByLandCommitteeMembers(xcommittee, sessionManager.getBusinessId());
	}

	@Override
	public LandCommitteeMembers findComMembersByXcommitteeAndXrow(String xcommittee, int xrow) {
		if(StringUtils.isBlank(xcommittee) || xrow == 0) return null;
		return landcommitteeinfoMapper.findComMembersByXcommitteeAndXrow(xcommittee,xrow,sessionManager.getBusinessId());
	}

	@Override
	public LandCommitteeMembers findByXcommitteeAndXperson(String xcommittee, String xperson) {
		if (StringUtils.isBlank(xcommittee) || StringUtils.isBlank(xperson))
			return null;
		return landcommitteeinfoMapper.findByXcommitteeAndXperson(xcommittee, xperson, sessionManager.getBusinessId());
	}


	//end of LandCommitteeMembers

}
