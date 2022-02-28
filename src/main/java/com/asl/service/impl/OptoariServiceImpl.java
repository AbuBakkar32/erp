package com.asl.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Optoari;
import com.asl.mapper.OptoariMapper;
import com.asl.service.OptoariService;
@Service
public class OptoariServiceImpl  extends AbstractGenericService implements OptoariService{

	@Autowired
	private OptoariMapper optoariMapper;
	
	@Transactional
	@Override
	public long save(Optoari optoari) {
		if (optoari == null)
			return 0;
		optoari.setZid(sessionManager.getBusinessId());
		optoari.setZauserid(getAuditUser());
		return optoariMapper.saveOptoari(optoari);
	}

	@Transactional
	@Override
	public long update(Optoari optoari) {
		if (optoari == null)
			return 0;
		optoari.setZid(sessionManager.getBusinessId());
		optoari.setZauserid(getAuditUser());
		return optoariMapper.updateOptoari(optoari);
	}

	@Transactional
	@Override
	public long delete(Optoari optoari) {
		if(optoari == null) return 0;
		long count = optoariMapper.deleteOptoari(optoari);
		return count;
	}

	@Override
	public List<Optoari> getAllOptoari() {
		return optoariMapper.getAllOptoari(sessionManager.getBusinessId());
	}

	@Override
	public Optoari findByXtypeXgcusAndXgitem(String xtype, String xgcus, String xgitem) {
		if (StringUtils.isBlank(xtype) || StringUtils.isBlank(xgcus) || StringUtils.isBlank(xgitem))
			return null;
		return optoariMapper.findByXtypeXgcusAndXgitem(xtype, xgcus,xgitem, sessionManager.getBusinessId());
	}

}
