package com.asl.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Termsdef;
import com.asl.mapper.TermsdefMapper;
import com.asl.service.TermsdefService;
@Service
public class TermsdefServiceImpl extends AbstractGenericService implements TermsdefService{

	@Autowired private TermsdefMapper termsdefMapper;
	
	@Transactional
	@Override
	public long saveTermsdef(Termsdef termsdef) {
		if(termsdef == null) return 0;
		termsdef.setZid(sessionManager.getBusinessId());
		termsdef.setZauserid(getAuditUser());
		long count = termsdefMapper.saveTermsdef(termsdef);
		return count;
	}

	@Transactional
	@Override
	public long updateTermsdef(Termsdef termsdef) {
		if(termsdef == null) return 0;
		termsdef.setZid(sessionManager.getBusinessId());
		termsdef.setZauserid(getAuditUser());
		long count = termsdefMapper.updateTermsdef(termsdef);
		return count;
	}

	@Transactional
	@Override
	public long deleteTermsdef(Termsdef termsdef) {
		if(termsdef == null) return 0;
		long count = termsdefMapper.deleteTermsdef(termsdef);
		return count;
	}

	@Override
	public List<Termsdef> getAllTermsdef() {
		return termsdefMapper.getAllTermsdef(sessionManager.getBusinessId());
	}

	@Override
	public Termsdef findTermsdefByXtypeAndXterm(String xtype, String xterm) {
		if(StringUtils.isBlank(xtype) || StringUtils.isBlank(xterm)) return null;
		return termsdefMapper.findTermsdefByXtypeAndXterm(xtype, xterm, sessionManager.getBusinessId());
	}

	@Override
	public Termsdef findbyXtermid(int xtermid) {
		if(xtermid == 0) return null;
		return termsdefMapper.findbyXtermid(xtermid, sessionManager.getBusinessId());
	}


}
