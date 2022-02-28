package com.asl.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Imtogli;
import com.asl.entity.Xtrn;
import com.asl.mapper.ImtogliMapper;
import com.asl.service.ImtogliService;
@Service
public class ImtogliServiceImpl extends AbstractGenericService implements ImtogliService{

	@Autowired
	private ImtogliMapper imtogliMapper;
	
	@Transactional
	@Override
	public long saveImtogli(Imtogli imtogli) {
		if (imtogli == null)
			return 0;
		imtogli.setZid(sessionManager.getBusinessId());
		imtogli.setZauserid(getAuditUser());
		return imtogliMapper.saveImtogli(imtogli);
	}

	@Transactional
	@Override
	public long updateImtogli(Imtogli imtogli) {
		if (imtogli == null)
			return 0;
		imtogli.setZid(sessionManager.getBusinessId());
		imtogli.setZauserid(getAuditUser());
		return imtogliMapper.updateImtogli(imtogli);
	}

	@Transactional
	@Override
	public long deleteImtogli(Imtogli imtogli) {
		if(imtogli == null) return 0;
		long count = imtogliMapper.deleteImtogli(imtogli);
		return count;
	}

	@Override
	public List<Imtogli> getAllImtogli(String xtrn) {
		return imtogliMapper.getAllImtogli(xtrn,sessionManager.getBusinessId());
	}

	@Override
	public Imtogli findByXtrnAndXgitem(String xtrn, String xgitem) {
		if (StringUtils.isBlank(xtrn) || StringUtils.isBlank(xgitem))
			return null;
		return imtogliMapper.findByXtrnAndXgitem(xtrn, xgitem, sessionManager.getBusinessId());
	}

	@Override
	public List<Xtrn> getxtrn() {
		return imtogliMapper.getxtrn(sessionManager.getBusinessId());
	}

	

	
}
