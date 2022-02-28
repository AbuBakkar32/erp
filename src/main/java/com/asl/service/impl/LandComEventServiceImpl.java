package com.asl.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.LandComEvent;
import com.asl.entity.LandEventsMember;
import com.asl.entity.LandOwner;
import com.asl.mapper.LandComEventMapper;
import com.asl.service.LandComEventService;

@Service
public class LandComEventServiceImpl extends AbstractGenericService implements LandComEventService {

	
	@Autowired
	private LandComEventMapper landComEventMapper;
	
	@Transactional
	@Override
	public long saveLandComEvent(LandComEvent landComEvent) {
		if (landComEvent == null)
			return 0;
		landComEvent.setZid(sessionManager.getBusinessId());
		landComEvent.setZauserid(getAuditUser());
		return landComEventMapper.saveLandComEvent(landComEvent);
	}

	@Transactional
	@Override
	public long updateLandComEvent(LandComEvent landComEvent) {
		if (landComEvent == null)
			return 0;
		landComEvent.setZid(sessionManager.getBusinessId());
		landComEvent.setZauserid(getAuditUser());
		return landComEventMapper.updateLandComEvent(landComEvent);
	}

	@Override
	public long delete(LandComEvent landComEvent) {
		if(landComEvent == null) return 0;
		long count = landComEventMapper.deleteLandComEvent(landComEvent);
		return count;
	}
	
	@Override
	public List<LandComEvent> getAllLandComEvent() {
		return landComEventMapper.getAllLandComEvent(sessionManager.getBusinessId());
	}

	@Override
	public LandComEvent findAllLandComEvent(String xevent) {
		if (StringUtils.isBlank(xevent))
			return null;
		return landComEventMapper.findAllLandComEvent(xevent, sessionManager.getBusinessId());
	}

	@Override
	public List<LandComEvent> getAllLandOtherEvent() {
		return landComEventMapper.getAllLandOtherEvent(sessionManager.getBusinessId());
	}

	//start of Event Member
	@Transactional
	@Override
	public long save(LandEventsMember landEventsMember) {
		if (landEventsMember == null)
			return 0;
		landEventsMember.setZid(sessionManager.getBusinessId());
		landEventsMember.setZauserid(getAuditUser());
		return landComEventMapper.saveLandEventsMember(landEventsMember);
	}

	@Transactional
	@Override
	public long update(LandEventsMember landEventsMember) {
		if (landEventsMember == null)
			return 0;
		landEventsMember.setZid(sessionManager.getBusinessId());
		landEventsMember.setZauserid(getAuditUser());
		return landComEventMapper.updateLandEventsMember(landEventsMember);
	}

	@Override
	public long deleteLandEventsMember(LandEventsMember landEventsMember) {
		if(landEventsMember == null) return 0;
		long count = landComEventMapper.deleteLandEventsMember(landEventsMember);
		return count;
	}

	@Override
	public List<LandEventsMember> getAllLandEventsMember() {
		return landComEventMapper.getAllLandEventsMember(sessionManager.getBusinessId());
	}

	@Override
	public List<LandEventsMember> findByLandEventsMember(String xevent) {
		if (StringUtils.isBlank(xevent))
			return null;
		return landComEventMapper.findByLandEventsMember(xevent, sessionManager.getBusinessId());
	}

	@Override
	public LandEventsMember findlandLandEventsMemberByXeventAndXrow(String xevent, int xrow) {
		if(StringUtils.isBlank(xevent) || xrow == 0) return null;
		return landComEventMapper.findlandLandEventsMemberByXeventAndXrow(xevent,xrow,sessionManager.getBusinessId());
	}

	@Override
	public LandEventsMember findByXeventAndXperson(String xevent, String xperson) {
		
			if (StringUtils.isBlank(xevent) || StringUtils.isBlank(xperson))
				return null;
			return landComEventMapper.findByXeventAndXperson(xevent, xperson, sessionManager.getBusinessId());
		
		
	}


	

}
