package com.asl.service.report.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Lcamend;
import com.asl.entity.Lcinfo;
import com.asl.mapper.LcinfoMapper;
import com.asl.service.LcinfoService;
import com.asl.service.impl.AbstractGenericService;
@Service
public class LcinfoServiceImpl extends AbstractGenericService implements LcinfoService{

	@Autowired private LcinfoMapper  lcinfoMapper;
	
	@Transactional
	@Override
	public long saveLcinfo(Lcinfo lcinfo) {
		if (lcinfo == null)
			return 0;
		lcinfo.setZid(sessionManager.getBusinessId());
		lcinfo.setZauserid(getAuditUser());
		return lcinfoMapper.saveLcinfo(lcinfo);
	}

	@Transactional
	@Override
	public long updateLcinfo(Lcinfo lcinfo) {
		if (lcinfo == null)
			return 0;
		lcinfo.setZid(sessionManager.getBusinessId());
		lcinfo.setZauserid(getAuditUser());
		return lcinfoMapper.updateLcinfo(lcinfo);
	}

	@Transactional
	@Override
	public long deleteLcinfo(Lcinfo lcinfo) {
		if(lcinfo == null)
			return 0;
		long count = lcinfoMapper.deleteLcinfo(lcinfo);
		return count;
	}

	@Override
	public List<Lcinfo> getAllLcinfo() {
		return lcinfoMapper.getAllLcinfo(sessionManager.getBusinessId());
	}

	@Override
	public Lcinfo findByLcinfo(String xlcno) {
		if (StringUtils.isBlank(xlcno))
			return null;
		return lcinfoMapper.findByLcinfo(xlcno, sessionManager.getBusinessId());
	}

	@Transactional
	@Override
	public long saveLcamend(Lcamend lcamend) {
		if (lcamend == null)
			return 0;
		lcamend.setZid(sessionManager.getBusinessId());
		lcamend.setZauserid(getAuditUser());
		return lcinfoMapper.saveLcamend(lcamend);
	}

	@Transactional
	@Override
	public long updateLcamend(Lcamend lcamend) {
		if (lcamend == null)
			return 0;
		lcamend.setZid(sessionManager.getBusinessId());
		lcamend.setZauserid(getAuditUser());
		return lcinfoMapper.updateLcamend(lcamend);
	}

	@Transactional
	@Override
	public long deleteLcamend(Lcamend lcamend) {
		if(lcamend == null) return 0;
		long count = lcinfoMapper.deleteLcamend(lcamend);
		return count;
	}

	@Override
	public List<Lcamend> getAllLcamend() {
		return lcinfoMapper.getAllLcamend(sessionManager.getBusinessId());
	}

	@Override
	public List<Lcamend> findByLcamend(String xlcno) {
		if(StringUtils.isBlank(xlcno)) return null;
		return lcinfoMapper.findByLcamend(xlcno,sessionManager.getBusinessId());
	}

	@Override
	public Lcamend findLcamendByXlcnoAndXrow(String xlcno, int xrow) {
		if(StringUtils.isBlank(xlcno) || xrow == 0) return null;
		return lcinfoMapper.findLcamendByXlcnoAndXrow(xlcno,xrow,sessionManager.getBusinessId());
	}

	@Override
	public List<Lcinfo> searchLcNo(String hint) {
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		return lcinfoMapper.searchLcNo(hint.toUpperCase(), sessionManager.getBusinessId());
	}

}
