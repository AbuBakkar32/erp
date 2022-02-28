package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Cawh;
import com.asl.entity.Cawhcostest;
import com.asl.mapper.CawhMapper;
import com.asl.service.CawhService;

@Service
public class CawhServiceImpl extends AbstractGenericService implements CawhService{

	@Autowired private CawhMapper cawhMapper;
	
	@Transactional
	@Override
	public long saveCawh(Cawh cawh) {
		if (cawh == null)
			return 0;
		cawh.setZid(sessionManager.getBusinessId());
		cawh.setZauserid(getAuditUser());
		return cawhMapper.saveCawh(cawh);
	}

	@Transactional
	@Override
	public long updateCawh(Cawh cawh) {
		if (cawh == null)
			return 0;
		cawh.setZid(sessionManager.getBusinessId());
		cawh.setZauserid(getAuditUser());
		return cawhMapper.updateCawh(cawh);
	}

	@Transactional
	@Override
	public long deleteCawh(Cawh cawh) {
		if(cawh == null) return 0;
		long count = cawhMapper.deleteCawh(cawh);
		return count;
	}

	@Override
	public List<Cawh> getAllCawh() {
		return cawhMapper.getAllCawh(sessionManager.getBusinessId());
	}

	@Override
	public Cawh findByCawh(String xwh) {
		if (StringUtils.isBlank(xwh))
			return null;
		return cawhMapper.findByCawh(xwh, sessionManager.getBusinessId());
	}

	@Override
	public List<Cawh> searchSite(String xwh) {
		if(StringUtils.isBlank(xwh)) return Collections.emptyList();
		return cawhMapper.searchSite(xwh.toUpperCase(), sessionManager.getBusinessId());
	}

	@Override
	public List<Cawh> getCawhBYProject(String xproject) {
		if(StringUtils.isBlank(xproject)) return null;
		return cawhMapper.getCawhBYProject(xproject.toUpperCase(), sessionManager.getBusinessId());
	}

	//For Estimated Cost
	@Transactional
	@Override
	public long saveCawhcostest(Cawhcostest cawhcostest) {
		if (cawhcostest == null) return 0;
		cawhcostest.setZid(sessionManager.getBusinessId());
		cawhcostest.setZauserid(getAuditUser());
		long count = cawhMapper.saveCawhcostest(cawhcostest);
		if(count != 0) 
		{ 
			count = updateCawhTotalAmt(cawhcostest.getXwh()); 
		}
		return count;
	}

	@Transactional
	@Override
	public long updateCawhcostest(Cawhcostest cawhcostest) {
		if (cawhcostest == null) return 0;
		cawhcostest.setZid(sessionManager.getBusinessId());
		cawhcostest.setZauserid(getAuditUser());
		long count = cawhMapper.updateCawhcostest(cawhcostest);
		if(count != 0) 
		{ 
			count = updateCawhTotalAmt(cawhcostest.getXwh()); 
		}
		return count;
	}

	@Transactional
	@Override
	public long deleteCawhcostest(Cawhcostest cawhcostest) {
		if(cawhcostest == null) return 0;
		long count = cawhMapper.deleteCawhcostest(cawhcostest);
		if(count != 0) 
		{ 
			count = updateCawhTotalAmt(cawhcostest.getXwh()); 
		}
		return count;
	}

	@Override
	public List<Cawhcostest> getAllCawhcostest() {
		return cawhMapper.getAllCawhcostest(sessionManager.getBusinessId());
	}

	@Override
	public List<Cawhcostest> findByCawhcostest(String xwh) {
		if(StringUtils.isBlank(xwh)) return null;
		return cawhMapper.findByCawhcostest(xwh,sessionManager.getBusinessId());
	}

	@Override
	public Cawhcostest findCawhcostestByXwhAndXrow(String xwh, int xrow) {
		if(StringUtils.isBlank(xwh) || xrow == 0) return null;
		return cawhMapper.findCawhcostestByXwhAndXrow(xwh,xrow,sessionManager.getBusinessId());
	}

	@Override
	public Cawhcostest findCawhcostestByXwhAndXitem(String xwh, String xitem) {
		if(StringUtils.isBlank(xwh) || StringUtils.isBlank(xitem)) return null;
		return cawhMapper.findCawhcostestByXwhAndXitem(xwh,xitem,sessionManager.getBusinessId());
	}

	@Override
	public long updateCawhTotalAmt(String xwh) {
		if(StringUtils.isBlank(xwh)) return 0;
		return cawhMapper.updateCawhTotalAmt(xwh, sessionManager.getBusinessId());
	}

}
