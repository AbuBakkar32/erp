package com.asl.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Pdloantrn;
import com.asl.entity.Pdloanwriteoff;
import com.asl.mapper.PdloantrnMapper;
import com.asl.service.PdloantrnService;
@Service
public class PdloantrnServiceImpl extends AbstractGenericService implements PdloantrnService{

	@Autowired
	private PdloantrnMapper pdloantrnMapper;
	
	@Transactional
	@Override
	public long savePdloantrn(Pdloantrn pdloantrn) {
		if (pdloantrn == null)
			return 0;
		pdloantrn.setZid(sessionManager.getBusinessId());
		pdloantrn.setZauserid(getAuditUser());
		return pdloantrnMapper.savePdloantrn(pdloantrn);
	}

	@Transactional
	@Override
	public long updatePdloantrn(Pdloantrn pdloantrn) {
		if (pdloantrn == null)
			return 0;
		pdloantrn.setZid(sessionManager.getBusinessId());
		pdloantrn.setZauserid(getAuditUser());
		return pdloantrnMapper.updatePdloantrn(pdloantrn);
	}

	@Transactional
	@Override
	public long deletePdloantrn(Pdloantrn pdloantrn) {
		if(pdloantrn == null) return 0;
		long count = pdloantrnMapper.deletePdloantrn(pdloantrn);
		return count;
	}

	@Override
	public List<Pdloantrn> getAllPdloantrn() {
		return pdloantrnMapper.getAllPdloantrn(sessionManager.getBusinessId());
	}

	@Override
	public Pdloantrn findByPdloantrn(String xvoucher) {
		if (StringUtils.isBlank(xvoucher))
			return null;
		return pdloantrnMapper.findByPdloantrn(xvoucher, sessionManager.getBusinessId());
	}

	@Transactional
	@Override
	public long savePdloanwriteoff(Pdloanwriteoff loanwo) {
		if (loanwo == null)
			return 0;
		loanwo.setZid(sessionManager.getBusinessId());
		loanwo.setZauserid(getAuditUser());
		return pdloantrnMapper.savePdloanwriteoff(loanwo);
	}

	@Transactional
	@Override
	public long updatePdloanwriteoff(Pdloanwriteoff loanwo) {
		if (loanwo == null)
			return 0;
		loanwo.setZid(sessionManager.getBusinessId());
		loanwo.setZauserid(getAuditUser());
		return pdloantrnMapper.updatePdloanwriteoff(loanwo);
	}

	@Transactional
	@Override
	public long deletePdloanwriteoff(Pdloanwriteoff loanwo) {
		if(loanwo == null) return 0;
		long count = pdloantrnMapper.deletePdloanwriteoff(loanwo);
		return count;
	}

	@Override
	public List<Pdloanwriteoff> getAllPdloanwriteoff() {
		return pdloantrnMapper.getAllPdloanwriteoff(sessionManager.getBusinessId());
	}

	@Override
	public List<Pdloanwriteoff> findByPdloanwriteoff(String xvoucher) {
		if (StringUtils.isBlank(xvoucher))
			return null;
		return pdloantrnMapper.findByPdloanwriteoff(xvoucher, sessionManager.getBusinessId());
	}

	@Override
	public Pdloanwriteoff findPdloanwriteoffByXvoucherAndXrow(String xvoucher, int xrow) {
		if(StringUtils.isBlank(xvoucher) || xrow == 0) return null;
		return pdloantrnMapper.findPdloanwriteoffByXvoucherAndXrow(xvoucher,xrow,sessionManager.getBusinessId());
	}

}
