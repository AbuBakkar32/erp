package com.asl.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Acsub;
import com.asl.mapper.AcsubMapper;
import com.asl.service.AcsubService;

/**
 * @author Zubayer Ahamed
 * @since Jul 26, 2021
 */
@Service
public class AcsubServiceImpl extends AbstractGenericService implements AcsubService {

	@Autowired private AcsubMapper acsubMapper;

	@Transactional
	@Override
	public long save(Acsub acsub) {
		if(acsub == null) return 0;
		acsub.setZid(sessionManager.getBusinessId());
		acsub.setZauserid(getAuditUser());
		return acsubMapper.save(acsub);
	}

	@Transactional
	@Override
	public long update(Acsub acsub) {
		if(acsub == null) return 0;
		acsub.setZid(sessionManager.getBusinessId());
		acsub.setZuuserid(getAuditUser());
		return acsubMapper.update(acsub);
	}

	@Transactional
	@Override
	public long delete(String xacc, String xsub) {
		if(StringUtils.isBlank(xacc) || StringUtils.isBlank(xsub)) return 0;
		return acsubMapper.delete(xacc, xsub, sessionManager.getBusinessId());
	}

	@Override
	public List<Acsub> getAll() {
		return acsubMapper.getAll(sessionManager.getBusinessId());
	}

	@Override
	public Acsub findByXaccAndXsub(String xacc, String xsub) {
		if(StringUtils.isBlank(xacc) || StringUtils.isBlank(xsub)) return null;
		return acsubMapper.findByXaccAndXsub(xacc, xsub, sessionManager.getBusinessId());
	}

}
