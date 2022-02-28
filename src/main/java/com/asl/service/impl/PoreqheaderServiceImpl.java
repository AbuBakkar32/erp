package com.asl.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Poreqdetail;
import com.asl.entity.Poreqheader;
import com.asl.mapper.PoreqheaderMapper;
import com.asl.service.PoreqheaderService;
@Service
public class PoreqheaderServiceImpl extends AbstractGenericService implements PoreqheaderService {

	@Autowired private PoreqheaderMapper poreqheaderMapper;

	@Transactional
	@Override
	public long savePoreqheader(Poreqheader po) {
		if (po == null)
			return 0;
		po.setZid(sessionManager.getBusinessId());
		po.setZauserid(getAuditUser());
		return poreqheaderMapper.savePoreqheader(po);
	}

	@Transactional
	@Override
	public long updatePoreqheader(Poreqheader po) {
		if (po == null)
			return 0;
		po.setZid(sessionManager.getBusinessId());
		po.setZauserid(getAuditUser());
		return poreqheaderMapper.updatePoreqheader(po);
	}

	@Transactional
	@Override
	public long deletePoreqheader(Poreqheader po) {
		if(po == null) return 0;
		long count = poreqheaderMapper.deletePoreqheader(po);
		return count;
	}

	@Override
	public List<Poreqheader> getALllPoreqheaderByXpreparer(String xpreparer) {
		return poreqheaderMapper.getALllPoreqheaderByXpreparer(sessionManager.getBusinessId(), xpreparer);
	}

	@Override
	public List<Poreqheader> getALllPoreqheader() {
		return poreqheaderMapper.getALllPoreqheader(sessionManager.getBusinessId());
	}

	@Override
	public Poreqheader findByPoreqheader(String xporeqnum) {
		if (StringUtils.isBlank(xporeqnum)) return null;
		return poreqheaderMapper.findByPoreqheader(sessionManager.getBusinessId(), xporeqnum);
	}
	
	@Override
	public List<Poreqheader> getPoreqheadersByXtypetrn(String xtypetrn) {
		if (StringUtils.isBlank(xtypetrn)) return null;
		return poreqheaderMapper.getPoreqheadersByXtypetrn(xtypetrn,sessionManager.getBusinessId());
	}	

	@Transactional
	@Override
	public long savePoreqdetail(Poreqdetail poreqdetail) {
		if(poreqdetail == null || StringUtils.isBlank(poreqdetail.getXporeqnum())) return 0;
		poreqdetail.setZid(sessionManager.getBusinessId());
		poreqdetail.setZauserid(getAuditUser());
		long count = poreqheaderMapper.savePoreqdetail(poreqdetail);
		if(count != 0) 
		{ 
			count = updatePoreqheaderTotalAmt(poreqdetail.getXporeqnum()); 
		}
		return count;
	}

	@Transactional
	@Override
	public long updatePoreqdetail(Poreqdetail poreqdetail) {
		if(poreqdetail == null || StringUtils.isBlank(poreqdetail.getXporeqnum())) return 0;
		poreqdetail.setZid(sessionManager.getBusinessId());
		poreqdetail.setZauserid(getAuditUser());
		long count = poreqheaderMapper.updatePoreqdetail(poreqdetail);
		if(count != 0) 
		{ 
			count = updatePoreqheaderTotalAmt(poreqdetail.getXporeqnum()); 
		}
		return count;
	}

	@Transactional
	@Override
	public long deletePoreqdetail(Poreqdetail poreqdetail) {
		if(poreqdetail == null) return 0;
		long count = poreqheaderMapper.deletePoreqdetail(poreqdetail);
		if(count != 0) 
		{ 
			count = updatePoreqheaderTotalAmt(poreqdetail.getXporeqnum()); 
		}
		return count;
	}

	@Override
	public Poreqdetail findPoreqdetailByXporeqnumAndXrow(String xporeqnum, int xrow) {
		if (StringUtils.isBlank(xporeqnum) || xrow == 0)
			return null;
		return poreqheaderMapper.findPoreqdetailByXporeqnumAndXrow(xporeqnum, xrow, sessionManager.getBusinessId());
	}

	@Override
	public Poreqdetail findPoreqdetailByXporeqnumAndXitem(String xporeqnum, String xitem) {
		if (StringUtils.isBlank(xporeqnum) || StringUtils.isBlank(xitem))
			return null;
		return poreqheaderMapper.findPoreqdetailByXporeqnumAndXitem(xporeqnum, xitem, sessionManager.getBusinessId());
	
	}

	@Override
	public List<Poreqdetail> findPoreqdetailByXporeqnum(String xporeqnum) {
		if (StringUtils.isBlank(xporeqnum)) return null;
		return poreqheaderMapper.findPoreqdetailByXporeqnum(xporeqnum, sessionManager.getBusinessId());
	}

	@Override
	public long updatePoreqheaderTotalAmt(String xporeqnum) {
		if(StringUtils.isBlank(xporeqnum)) return 0;
		return poreqheaderMapper.updatePoreqheaderTotalAmt(xporeqnum, sessionManager.getBusinessId());
	}

	@Transactional
	@Override
	public void procSP_CREATEPO_FROMPRQ(String xporeqnum,String p_screen, String p_seq) {
		poreqheaderMapper.procSP_CREATEPO_FROMPRQ(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xporeqnum,p_screen, p_seq);
	}

	
	@Override
	public Poreqheader topPoreqheader() {
		return poreqheaderMapper.topPoreqheader(sessionManager.getBusinessId());
	}

	@Override
	public Poreqheader bottomPoreqheader() {
		return poreqheaderMapper.bottomPoreqheader(sessionManager.getBusinessId());
	}

	@Override
	public Poreqheader nextPoreqheader(String xporeqnum) {
		return poreqheaderMapper.nextPoreqheader(sessionManager.getBusinessId(), xporeqnum);
	}

	@Override
	public Poreqheader previousPoreqheader(String xporeqnum) {
		return poreqheaderMapper.previousPoreqheader(sessionManager.getBusinessId(), xporeqnum);
	}

}
