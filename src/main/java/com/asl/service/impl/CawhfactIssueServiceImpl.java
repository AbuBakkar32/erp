package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Cawhfact;
import com.asl.mapper.CawhfactIssueMapper;
import com.asl.service.CawhfactIssueService;

@Service
public class CawhfactIssueServiceImpl extends AbstractGenericService implements CawhfactIssueService {

	
	@Autowired private CawhfactIssueMapper cawhfactMapper;
	
	@Transactional
	@Override
	public long save(Cawhfact cawhfact) {
		if(cawhfact == null) return 0;
		
		cawhfact.setZid(sessionManager.getBusinessId());
		cawhfact.setZauserid(getAuditUser());
		cawhfact.setXtype("Issue");
		
		return cawhfactMapper.saveIssue(cawhfact);
	}

	@Transactional
	@Override
	public long update(Cawhfact cawhfact) {
		if(cawhfact == null) return 0;
		
		cawhfact.setZid(sessionManager.getBusinessId());
		cawhfact.setZauserid(getAuditUser());
		cawhfact.setXtype("Issue");
		
		return cawhfactMapper.updateIssue(cawhfact);
	}

	@Override
	public List<Cawhfact> getAllIssue() {
		
		return cawhfactMapper.getAllIssue(sessionManager.getBusinessId());
	}

	@Override
	public Cawhfact findByIssueId(String xtrnnum) {
		if (StringUtils.isBlank(xtrnnum)) return null;
		
		return cawhfactMapper.findByIssueId(xtrnnum, sessionManager.getBusinessId());
	}

	@Transactional
	@Override
	public long delete(Cawhfact cawhfact) {
		if(cawhfact==null) return 0;
		
		long count = cawhfactMapper.deleteIssue(cawhfact);
		return count;
	}

	@Override
	public List<Cawhfact> searchDependsOn(String hint) {
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		
		return cawhfactMapper.searchDependsOn(hint.toUpperCase(), sessionManager.getBusinessId());
	}

}
