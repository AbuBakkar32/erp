package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Acmst;
import com.asl.mapper.AcmstMapper;
import com.asl.service.AcmstService;

/**
 * @author Zubayer Ahamed
 * @since Jul 6, 2021
 */
@Service
public class AcmstServiceImpl extends AbstractGenericService implements AcmstService {

	@Autowired private AcmstMapper acmstMapper;

	@Transactional
	@Override
	public long save(Acmst acmst) {
		if(acmst == null) return 0;
		acmst.setZauserid(getAuditUser());
		acmst.setZid(sessionManager.getBusinessId());
		return acmstMapper.save(acmst);
	}

	@Transactional
	@Override
	public long update(Acmst acmst) {
		if(acmst == null) return 0;
		acmst.setZuuserid(getAuditUser());
		acmst.setZid(sessionManager.getBusinessId());
		return acmstMapper.update(acmst);
	}

	@Override
	public List<Acmst> getAllAcmst() {
		return acmstMapper.getAllAcmst(sessionManager.getBusinessId());
	}

	@Override
	public Acmst findByXacc(String xacc) {
		if(StringUtils.isBlank(xacc)) return null;
		return acmstMapper.findByXacc(xacc, sessionManager.getBusinessId());
	}

	@Transactional
	@Override
	public long delete(String xacc) {
		if(StringUtils.isBlank(xacc)) return 0;
		return acmstMapper.delete(xacc, sessionManager.getBusinessId());
	}

	@Override
	public List<Acmst> searchByXaccORXdesc(String hint) {
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		return acmstMapper.searchByXaccORXdesc(hint.toUpperCase(), sessionManager.getBusinessId());
	}
	
	// for Report Search
	@Override
	public List<Acmst> getAllAcc(String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		return acmstMapper.getAllAcc(hint.toUpperCase(),sessionManager.getBusinessId());
	}
	
	@Override
	public List<Acmst> getAllSub(String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		return acmstMapper.getAllSub(hint.toUpperCase(),sessionManager.getBusinessId());
	}
	
}
