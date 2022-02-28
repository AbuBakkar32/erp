package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Imtrn;
import com.asl.mapper.ImtrnMapper;
import com.asl.service.ImtrnService;

@Service
public class ImtrnServiceImpl extends AbstractGenericService implements ImtrnService {

	@Autowired private ImtrnMapper imtrnMapper;

	@Override
	@Transactional
	public long save(Imtrn imtrn) {
		if (imtrn == null || StringUtils.isBlank(imtrn.getXtype()) || StringUtils.isBlank(imtrn.getXtrn())) return 0;
		imtrn.setZid(sessionManager.getBusinessId());
		imtrn.setZauserid(getAuditUser());
		return imtrnMapper.saveImtrn(imtrn);
	}

	@Override
	@Transactional
	public long save(List<Imtrn> imtrns) {
		if(imtrns == null || imtrns.isEmpty()) return 0;
		long f_count = 0;
		for(Imtrn imt : imtrns) {
			f_count += save(imt);
		}
		return f_count;
	}

	@Override
	@Transactional
	public long update(Imtrn imtrn) {
		if (imtrn == null || StringUtils.isBlank(imtrn.getXtrn())) return 0;
		imtrn.setZid(sessionManager.getBusinessId());
		imtrn.setZuuserid(getAuditUser());
		return imtrnMapper.updateImtrn(imtrn);
	}

	@Override
	public Imtrn findImtrnByXimtrnnum(String ximtrnnum) {
		if (StringUtils.isBlank(ximtrnnum)) return null;
		return imtrnMapper.findImtrnByXimtrnnum(ximtrnnum, sessionManager.getBusinessId());
	}

	@Override
	public List<Imtrn> getAllImtrn() {
		return imtrnMapper.getAllImtrn(sessionManager.getBusinessId());
	}
	
	@Override
	public List<Imtrn> getAllImtrnlist(String xtype) {
		if(StringUtils.isBlank(xtype)) return Collections.emptyList();
		return imtrnMapper.getAllImtrnlist(xtype,sessionManager.getBusinessId());
	}

	@Override
	public long deleteByXimtrnnum(String ximtrnnum) {
		if (StringUtils.isBlank(ximtrnnum)) return 0;
		return imtrnMapper.deleteByXimtrnnum(ximtrnnum, sessionManager.getBusinessId());
	}

}
