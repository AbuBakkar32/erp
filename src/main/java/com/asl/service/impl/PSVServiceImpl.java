package com.asl.service.impl;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.PSV;
import com.asl.mapper.PSVMapper;
import com.asl.service.PSVService;

/**
 * @author Zubayer Ahamed
 * @since May 20, 2021
 */
@Service
public class PSVServiceImpl extends AbstractGenericService implements PSVService {

	@Autowired private PSVMapper psvMapper;

	@Transactional
	@Override
	public long savePSV(PSV psv) {
		if(psv == null) return 0;
		psv.setZid(sessionManager.getBusinessId());
		psv.setZauserid(getAuditUser());
		return psvMapper.savePSV(psv);
	}

	@Transactional
	@Override
	public long updatePSV(PSV psv) {
		if(psv == null) return 0;
		psv.setZid(sessionManager.getBusinessId());
		psv.setZuuserid(getAuditUser());
		return psvMapper.updatePSV(psv);
	}

	@Transactional
	@Override
	public long deletePSV(PSV psv) {
		if(psv == null) return 0;
		psv.setZid(sessionManager.getBusinessId());
		psv.setZuuserid(getAuditUser());
		return psvMapper.deletePSV(psv);
	}

	@Override
	public PSV findByXchalanAndXbatchAndXrawitem(String xchalan, String xbatch, String xrawitem) {
		if(StringUtils.isBlank(xchalan) || StringUtils.isBlank(xbatch) || StringUtils.isBlank(xrawitem)) return null;
		return psvMapper.findByXchalanAndXbatchAndXrawitem(xchalan, xbatch, xrawitem, sessionManager.getBusinessId());
	}

	@Override
	public BigDecimal getTotalRawUsedExceptCurrentBatch(String xchalan, String xrawitem, String xbatch) {
		if(StringUtils.isBlank(xchalan) || StringUtils.isBlank(xbatch) || StringUtils.isBlank(xrawitem)) return null;
		return psvMapper.getTotalRawUsedExceptCurrentBatch(xchalan, xrawitem, xbatch, sessionManager.getBusinessId());
	}

}
