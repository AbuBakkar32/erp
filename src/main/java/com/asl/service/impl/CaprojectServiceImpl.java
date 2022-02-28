package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Caproject;
import com.asl.entity.Caprojectplan;
import com.asl.entity.LandInfo;
import com.asl.mapper.CaprojectMapper;
import com.asl.mapper.LandInfoMapper;
import com.asl.service.CaprojectService;
import com.asl.service.LandInfoService;
@Service
public class CaprojectServiceImpl extends AbstractGenericService implements CaprojectService {

	@Autowired private CaprojectMapper caprojectMapper;
	
	@Transactional
	@Override
	public long saveCaproject(Caproject caproject) {
		if (caproject == null)
			return 0;
		caproject.setZid(sessionManager.getBusinessId());
		caproject.setZauserid(getAuditUser());
		return caprojectMapper.saveCaproject(caproject);
	}

	@Transactional
	@Override
	public long updateCaproject(Caproject caproject) {
		if (caproject == null)
			return 0;
		caproject.setZid(sessionManager.getBusinessId());
		caproject.setZauserid(getAuditUser());
		return caprojectMapper.updateCaproject(caproject);
	}

	@Transactional
	@Override
	public long deleteCaproject(Caproject caproject) {
		if(caproject == null) return 0;
		long count = caprojectMapper.deleteCaproject(caproject);
		return count;
	}

	@Override
	public List<Caproject> getAllCaproject() {
		return caprojectMapper.getAllCaproject(sessionManager.getBusinessId());
	}

	@Override
	public Caproject findByCaproject(String xproject) {
		if (StringUtils.isBlank(xproject))
			return null;
		return caprojectMapper.findByCaproject(xproject, sessionManager.getBusinessId());
	}

	@Override
	public List<Caproject> searchProjectFromCaproject(String xproject) {
		if(StringUtils.isBlank(xproject)) return Collections.emptyList();
		return caprojectMapper.searchProjectFromCaproject(xproject.toUpperCase(), sessionManager.getBusinessId());
	}

	@Override
	public long saveCaprojectplan(Caprojectplan caprojectplan) {
		if (caprojectplan == null)
			return 0;
		caprojectplan.setZid(sessionManager.getBusinessId());
		caprojectplan.setZauserid(getAuditUser());
		return caprojectMapper.saveCaprojectplan(caprojectplan);
	}

	@Override
	public long updateCaprojectplan(Caprojectplan caprojectplan) {
		if (caprojectplan == null)
			return 0;
		caprojectplan.setZid(sessionManager.getBusinessId());
		caprojectplan.setZauserid(getAuditUser());
		return caprojectMapper.updateCaprojectplan(caprojectplan);
	}

	@Override
	public long deleteCaprojectplan(Caprojectplan caprojectplan) {
		if(caprojectplan == null) return 0;
		long count = caprojectMapper.deleteCaprojectplan(caprojectplan);
		return count;
	}

	@Override
	public List<Caprojectplan> getAllCaprojectplan() {
		return caprojectMapper.getAllCaprojectplan(sessionManager.getBusinessId());
	}

	@Override
	public List<Caprojectplan> findByCaprojectplan(String xproject) {
		if (StringUtils.isBlank(xproject))
			return null;
		return caprojectMapper.findByCaprojectplan(xproject, sessionManager.getBusinessId());
	}

	@Override
	public Caprojectplan findCaprojectplanByXprojectAndXrow(String xproject, int xrow) {
		if(StringUtils.isBlank(xproject) || xrow == 0) return null;
		return caprojectMapper.findCaprojectplanByXprojectAndXrow(xproject,xrow,sessionManager.getBusinessId());
	}

}
