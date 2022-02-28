package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Cadag;
import com.asl.entity.LandDagDetails;
import com.asl.mapper.LandDagMasterMapper;
import com.asl.service.LandDagMasterService;

@Service
public class LandDagMasterServiceImpl extends AbstractGenericService implements LandDagMasterService{

	@Autowired
	private LandDagMasterMapper landDagMasterMapper;
	
	@Transactional
	@Override
	public long saveLandDagMaster(Cadag landDagMaster) {
		if (landDagMaster == null)
			return 0;
		landDagMaster.setZid(sessionManager.getBusinessId());
		landDagMaster.setZauserid(getAuditUser());
		return landDagMasterMapper.saveLandDagMaster(landDagMaster);
	}

	@Transactional
	@Override
	public long updateLandDagMaster(Cadag landDagMaster) {
		if (landDagMaster == null)
			return 0;
		landDagMaster.setZid(sessionManager.getBusinessId());
		landDagMaster.setZauserid(getAuditUser());
		return landDagMasterMapper.updateLandDagMaster(landDagMaster);
	}

	@Transactional
	@Override
	public long deleteLandDagMaster(Cadag landDagMaster) {
		if(landDagMaster == null) return 0;
		long count = landDagMasterMapper.deleteLandDagMaster(landDagMaster);
		return count;
	}

	@Override
	public List<Cadag> getAllLandDagMaster() {
		return landDagMasterMapper.getAllLandDagMaster(sessionManager.getBusinessId());
	}

	@Override
	public Cadag findByXdagnumAndXdagtype(String xdagtype, int xdagnum) {
		if (StringUtils.isBlank(xdagtype) || xdagnum == 0)
			return null;
		return landDagMasterMapper.findByXdagnumAndXdagtype(xdagtype, xdagnum, sessionManager.getBusinessId());
	}

	@Override
	public Cadag findbyXdagtypeAndxdagid(String xdagtype, int xdagid) {
		if (StringUtils.isBlank(xdagtype) || xdagid == 0)
			return null;
		return landDagMasterMapper.findByXdagnumAndXdagtype(xdagtype, xdagid, sessionManager.getBusinessId());
	}

	@Override
	public Cadag findbyXdagid(int xdagid) {
		if(xdagid == 0)
			return null;
		return landDagMasterMapper.findbyXdagid(xdagid, sessionManager.getBusinessId());
	}

	@Override
	public List<Cadag> getDagNo(String xdagtype) {
		if(StringUtils.isBlank(xdagtype)) return Collections.emptyList();
		return landDagMasterMapper.getDagNo(xdagtype,sessionManager.getBusinessId());
	}


}
