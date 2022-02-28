package com.asl.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.Imprjtogli;
import com.asl.mapper.ImprjtogliMapper;
import com.asl.service.ImprjtogliService;

@Service
public class ImprjtogliServiceImpl extends AbstractGenericService implements ImprjtogliService{
	
	@Autowired private ImprjtogliMapper imprjtogliMapper;
	
	@Transactional
	@Override
	public long saveImprjtogli(Imprjtogli imprjtogli) {
		if(imprjtogli == null)
		return 0;
		imprjtogli.setZid(sessionManager.getBusinessId());
		imprjtogli.setZuuserid(getAuditUser());
		return imprjtogliMapper.saveImprjtogli(imprjtogli);
	}

	@Transactional
	@Override
	public long updateImprjtogli(Imprjtogli imprjtogli) {
		if(imprjtogli == null)
		return 0;
		imprjtogli.setZid(sessionManager.getBusinessId());
		imprjtogli.setZuuserid(getAuditUser());
		return imprjtogliMapper.updateImprjtogli(imprjtogli);
	}

	@Transactional
	@Override
	public long deleteImprjtogli(Imprjtogli imprjtogli) {
		if(imprjtogli == null)
		return 0;
		long count=imprjtogliMapper.deleteImprjtogli(imprjtogli);
		return count;
	}

	@Override
	public List<Imprjtogli> getAllImprjtogli() {
		return imprjtogliMapper.getAllImprjtogli(sessionManager.getBusinessId());
	}

	@Override
	public Imprjtogli findByXgitem(String xgitem) {
		return imprjtogliMapper.findByXgitem(sessionManager.getBusinessId(), xgitem);
	}

}
