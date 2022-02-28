package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Cawhfact;
import com.asl.mapper.CawhfactMapper;
import com.asl.service.CawhfactService;

@Service
public class CawhfactServiceImpl extends AbstractGenericService implements CawhfactService{
	
	@Autowired private CawhfactMapper cawhfactMapper;

	@Transactional
	@Override
	public long save(Cawhfact cawhfact) {
		if(cawhfact == null) return 0;
		cawhfact.setZid(sessionManager.getBusinessId());
		cawhfact.setZauserid(getAuditUser());
		cawhfact.setXtype("Task");
		
		return cawhfactMapper.saveTask(cawhfact);
	}

	@Transactional
	@Override
	public long update(Cawhfact cawhfact) {
		if(cawhfact == null) return 0;
		cawhfact.setZid(sessionManager.getBusinessId());
		cawhfact.setZauserid(getAuditUser());
		cawhfact.setXtype("Task");
		
		return cawhfactMapper.updateTask(cawhfact);
	}
	
	@Override
	@Transactional
	public long delete(Cawhfact cawhfact) {
		if(cawhfact==null) return 0;
		long count = cawhfactMapper.deleteTask(cawhfact);
		return count;
	}

	@Override
	public List<Cawhfact> getAllTask() {
		return cawhfactMapper.getAllTask(sessionManager.getBusinessId());
	}

	@Override
	public List<Cawhfact> getAllTasksByProject(String xproject) {
		if (StringUtils.isBlank(xproject)) return null;
		return cawhfactMapper.getAllTasksByProject(xproject, sessionManager.getBusinessId());
	}

	@Override
	public Cawhfact findByTaskId(String xtrnnum) {
		if (StringUtils.isBlank(xtrnnum)) return null;
		return cawhfactMapper.findByTaskId(xtrnnum, sessionManager.getBusinessId());
	}

	@Override
	public List<Cawhfact> searchDependsOn(String hint) {
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		return cawhfactMapper.searchDependsOn(hint.toUpperCase(), sessionManager.getBusinessId());
	}

}
