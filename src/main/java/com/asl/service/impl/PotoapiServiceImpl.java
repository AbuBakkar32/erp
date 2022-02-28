package com.asl.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Potoapi;
import com.asl.mapper.PotoapiMapper;
import com.asl.service.PotoapiService;
@Service
public class PotoapiServiceImpl extends AbstractGenericService implements PotoapiService{

	@Autowired
	private PotoapiMapper potoapiMapper;
	
	@Transactional
	@Override
	public long savePotoapi(Potoapi potoapi) {
		if (potoapi == null)
			return 0;
		potoapi.setZid(sessionManager.getBusinessId());
		potoapi.setZauserid(getAuditUser());
		return potoapiMapper.savePotoapi(potoapi);
	}

	@Transactional
	@Override
	public long updatePotoapi(Potoapi potoapi) {
		if (potoapi == null)
			return 0;
		potoapi.setZid(sessionManager.getBusinessId());
		potoapi.setZauserid(getAuditUser());
		return potoapiMapper.updatePotoapi(potoapi);
	}

	@Transactional
	@Override
	public long deletePotoapi(Potoapi potoapi) {
		if(potoapi == null) return 0;
		long count = potoapiMapper.deletePotoapi(potoapi);
		return count;
	}

	@Override
	public List<Potoapi> getAllPotoapi() {
		return potoapiMapper.getAllPotoapi(sessionManager.getBusinessId());
	}

	@Override
	public Potoapi findByXtypeXgsupAndXgitem(String xtype, String xgsup, String xgitem) {
		if (StringUtils.isBlank(xtype) || StringUtils.isBlank(xgsup) || StringUtils.isBlank(xgitem))
			return null;
		return potoapiMapper.findByXtypeXgsupAndXgitem(xtype, xgsup,xgitem, sessionManager.getBusinessId());
	}

}
