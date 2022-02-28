package com.asl.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Cawhbudget;
import com.asl.entity.Cawhbudgetdt;
import com.asl.mapper.CaprojectMapper;
import com.asl.mapper.CawhbudgetMapper;
import com.asl.service.CawhbudgetService;

@Service
public class CawhbudgetServiceImpl extends AbstractGenericService implements CawhbudgetService{

	@Autowired private CawhbudgetMapper cawhbudgetMapper;
	
	@Transactional
	@Override
	public long saveCawhbudget(Cawhbudget cawhbudget) {
		if (cawhbudget == null)
			return 0;
		cawhbudget.setZid(sessionManager.getBusinessId());
		cawhbudget.setZauserid(getAuditUser());
		return cawhbudgetMapper.saveCawhbudget(cawhbudget);
	}

	@Transactional
	@Override
	public long updateCawhbudget(Cawhbudget cawhbudget) {
		if (cawhbudget == null)
			return 0;
		cawhbudget.setZid(sessionManager.getBusinessId());
		cawhbudget.setZauserid(getAuditUser());
		return cawhbudgetMapper.updateCawhbudget(cawhbudget);
	}

	@Transactional
	@Override
	public long deleteCawhbudget(Cawhbudget cawhbudget) {
		if(cawhbudget == null) return 0;
		long count = cawhbudgetMapper.deleteCawhbudget(cawhbudget);
		return count;
	}

	@Override
	public List<Cawhbudget> getAllCawhbudget() {
		return cawhbudgetMapper.getAllCawhbudget(sessionManager.getBusinessId());
	}

	@Override
	public Cawhbudget findByCawhbudget(String xbudget) {
		if (StringUtils.isBlank(xbudget))
			return null;
		return cawhbudgetMapper.findByCawhbudget(xbudget, sessionManager.getBusinessId());
	}

	//Start of Budget Details
	@Transactional
	@Override
	public long saveCawhbudgetdt(Cawhbudgetdt cawhbudgetdt) {
		if ( cawhbudgetdt == null)return 0;
		cawhbudgetdt.setZid(sessionManager.getBusinessId());
		cawhbudgetdt.setZauserid(getAuditUser());
		long count = cawhbudgetMapper.saveCawhbudgetdt(cawhbudgetdt);
		if(count != 0) {
			count = updateCawhbudgetTotalAmt(cawhbudgetdt);
		}
		return count;
	}

	@Transactional
	@Override
	public long updateCawhbudgetdt(Cawhbudgetdt cawhbudgetdt) {
		if ( cawhbudgetdt == null)return 0;
		cawhbudgetdt.setZid(sessionManager.getBusinessId());
		cawhbudgetdt.setZauserid(getAuditUser());
		long count = cawhbudgetMapper.updateCawhbudgetdt(cawhbudgetdt);
		if(count != 0) {
			count = updateCawhbudgetTotalAmt(cawhbudgetdt);
		}
		return count;
	}

	@Transactional
	@Override
	public long deleteCawhbudgetdt(Cawhbudgetdt cawhbudgetdt) {
		if(cawhbudgetdt == null) return 0;
		long count = cawhbudgetMapper.deleteCawhbudgetdt(cawhbudgetdt);
		if(count != 0) {
			count = updateCawhbudgetTotalAmt(cawhbudgetdt);
		}
		return count;
	}

	@Override
	public List<Cawhbudgetdt> getAllCawhbudgetdt() {
		return cawhbudgetMapper.getAllCawhbudgetdt(sessionManager.getBusinessId());
	}

	@Override
	public List<Cawhbudgetdt> findByCawhbudgetdt(String xbudget) {
		if(StringUtils.isBlank(xbudget)) return null;
		return cawhbudgetMapper.findByCawhbudgetdt(xbudget,sessionManager.getBusinessId());
	}

	@Override
	public Cawhbudgetdt findCawhbudgetdtByXbudgetAndXrow(String xbudget, int xrow) {
		if(StringUtils.isBlank(xbudget) || xrow == 0) return null;
		return cawhbudgetMapper.findCawhbudgetdtByXbudgetAndXrow(xbudget,xrow,sessionManager.getBusinessId());
	}

	@Transactional
	@Override
	public long updateCawhbudgetTotalAmt(Cawhbudgetdt cawhbudgetdt) {
		if(cawhbudgetdt == null) return 0;
		return cawhbudgetMapper.updateCawhbudgetTotalAmt(cawhbudgetdt);
	}

	@Override
	public void procIM_confirmProjectBudget(String xbudget, String p_seq) {
		cawhbudgetMapper.procIM_confirmProjectBudget(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xbudget, p_seq);
	}

	@Override
	public List<Cawhbudget> getAllCawhbudgetByXwh(String xwh) {
		if(StringUtils.isBlank(xwh)) return null;
		return cawhbudgetMapper.getAllCawhbudgetByXwh(xwh,sessionManager.getBusinessId());
	}

}
