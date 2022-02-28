package com.asl.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Opordheader;
import com.asl.entity.Opreqdetail;
import com.asl.entity.Opreqheader;
import com.asl.mapper.OpreqMapper;
import com.asl.model.ServiceException;
import com.asl.service.OpreqService;


/**
 * @author Zubayer Ahamed
 * @since Mar 8, 2021
 */

@Service
public class OpreqServiceImpl extends AbstractGenericService implements OpreqService {

	@Autowired private OpreqMapper opreqMapper;

	@Transactional
	@Override
	public long saveOpreqheader(Opreqheader opreqheader) {
		if(opreqheader == null) return 0;
		opreqheader.setZid(sessionManager.getBusinessId());
		opreqheader.setZauserid(getAuditUser());
		return opreqMapper.saveOpreqheader(opreqheader);
	}

	@Transactional
	@Override
	public long updateOpreqheader(Opreqheader opreqheader) {
		if(opreqheader == null) return 0;
		opreqheader.setZid(sessionManager.getBusinessId());
		opreqheader.setZuuserid(getAuditUser());
		long count = opreqMapper.updateOpreqheader(opreqheader);
		updateOpreqHeaderTotalAmtAndGrandTotalAmt(opreqheader.getXdoreqnum());
		return count;
	}

	@Override
	public List<Opordheader> getAllSOCreated(String xreqnum){
		if(StringUtils.isBlank(xreqnum)) return null;
		return opreqMapper.getAllSOCreated(sessionManager.getBusinessId(), xreqnum);
	}

	@Transactional
	@Override
	public long deleteOpreqheader(String xdoreqnum) {
		if(StringUtils.isBlank(xdoreqnum)) return 0;
		long count = deleteOpreqdetailData(xdoreqnum);
		if(count == 0) return 0;
		return opreqMapper.deleteOpreqheader(xdoreqnum, sessionManager.getBusinessId());
	}

	@Override
	public Opreqheader findOpreqHeaderByXdoreqnum(String xdoreqnum) {
		if(StringUtils.isBlank(xdoreqnum)) return null;
		return opreqMapper.findOpreqHeaderByXdoreqnum(xdoreqnum, sessionManager.getBusinessId());
	}
	
	@Override
	public List<Opreqheader> getAllOpreqheader() {
		return opreqMapper.getAllOpreqheader(sessionManager.getBusinessId());
	}

	@Override
	public List<Opreqheader> getAllStatusOpenOpreqheader(){
		return opreqMapper.getAllStatusOpenOpreqheader(sessionManager.getBusinessId());
	}
	
	@Override
	public List<Opreqheader> getAllStatusConfirmedOpreqheader(){
		return opreqMapper.getAllStatusConfirmedOpreqheader(sessionManager.getBusinessId());
	}
	
	@Transactional
	@Override
	public long saveOpreqdetail(Opreqdetail opreqdetail) {
		if(opreqdetail == null) return 0;
		opreqdetail.setZid(sessionManager.getBusinessId());
		opreqdetail.setZauserid(getAuditUser());
		long count = opreqMapper.saveOpreqdetail(opreqdetail);
		if(count != 0) {
		updateOpreqHeaderTotalAmtAndGrandTotalAmt(opreqdetail.getXdoreqnum());
		}
		return count;
	}

	@Transactional
	@Override
	public long updateOpreqdetail(Opreqdetail opreqdetail) {
		if(opreqdetail == null) return 0;
		opreqdetail.setZid(sessionManager.getBusinessId());
		opreqdetail.setZuuserid(getAuditUser());
		long count = opreqMapper.updateOpreqdetail(opreqdetail);
		if(count != 0) {
			updateOpreqHeaderTotalAmtAndGrandTotalAmt(opreqdetail.getXdoreqnum());
			}
		return count;
	}

	@Transactional
	@Override
	public long deleteOpreqdetail(Opreqdetail opreqdetail) {
		if(opreqdetail == null) return 0;
		String opdoreqnum = opreqdetail.getXdoreqnum();
		opreqdetail.setZid(sessionManager.getBusinessId());
		long count = opreqMapper.deleteOpreqdetail(opreqdetail);
		if(count != 0) {
			updateOpreqHeaderTotalAmtAndGrandTotalAmt(opdoreqnum);
		}
		return count;
	}

	@Transactional
	@Override
	public long deleteOpreqdetailData(String xdoreqnum) {
		if(StringUtils.isBlank(xdoreqnum)) return 0;
		long count = opreqMapper.deleteOpreqdetailData(xdoreqnum, sessionManager.getBusinessId());
		if(count != 0) {
			updateOpreqHeaderTotalAmtAndGrandTotalAmt(xdoreqnum);
		}
		return count;
	}

	@Override
	@Transactional
	public long saveDetail(List<Opreqdetail> opreqdetail) throws ServiceException {
		if(opreqdetail == null || opreqdetail.isEmpty()) return 0;
		long f_count = 0;
		for(Opreqdetail pd : opreqdetail) {
			if(StringUtils.isBlank(pd.getXdoreqnum())) throw new ServiceException("Requesition reference empty");
			pd.setZid(sessionManager.getBusinessId());
			pd.setZauserid(getAuditUser());
			f_count += opreqMapper.savePoordDetailWithRow(pd);
			updateOpreqHeaderTotalAmtAndGrandTotalAmt(pd.getXdoreqnum());
		}
		return f_count;
	}

	
	@Override
	public List<Opreqdetail> findOpreqDetailByXdoreqnum(String xdoreqnum){
		if(StringUtils.isBlank(xdoreqnum)) return null;
		return opreqMapper.findOpreqDetailByXdoreqnum(xdoreqnum, sessionManager.getBusinessId());
	}

	@Override
	public Opreqdetail findOpreqdetailByXordernumAndXrow(String xdoreqnum, int xrow) {
		if(StringUtils.isBlank(xdoreqnum) || xrow == 0) return null;
		return opreqMapper.findOpreqdetailByXordernumAndXrow(xdoreqnum, xrow, sessionManager.getBusinessId());
	}
	
	@Override
	public Opreqdetail findOpreqdetailByXdoreqnumAndXitem(String xdoreqnum, String xitem) {
		if(StringUtils.isBlank(xdoreqnum) || StringUtils.isBlank(xitem)) return null;
		return opreqMapper.findOpreqdetailByXdoreqnumAndXitem(xdoreqnum, xitem, sessionManager.getBusinessId());
	}
	
	@Transactional
	@Override
	public long updateOpreqHeaderTotalAmtAndGrandTotalAmt(String xdoreqnum) {
		if(StringUtils.isBlank(xdoreqnum)) return 0;
		return opreqMapper.updateOpreqHeaderTotalAmtAndGrandTotalAmt(xdoreqnum, sessionManager.getBusinessId());
	}
	
	//For Individual Customer
	@Override
	public Opreqheader findOpreqCusHeaderByXdoreqnum() {
		if(StringUtils.isBlank(sessionManager.getLoggedInUserDetails().getXcus())) return null;
		return opreqMapper.findOpreqCusHeaderByXdoreqnum(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getXcus());
	}
	
	@Override
	public List<Opreqheader> getAllOpreqCusheader() {
		if(StringUtils.isBlank(sessionManager.getLoggedInUserDetails().getXcus())) return null;
		return opreqMapper.getAllOpreqCusheader(sessionManager.getBusinessId(),sessionManager.getLoggedInUserDetails().getXcus());
	}

	@Override
	public List<Opreqheader> getAllStatusOpenOpreqCusheader(){
		if(StringUtils.isBlank(sessionManager.getLoggedInUserDetails().getXcus())) return null;
		return opreqMapper.getAllStatusOpenOpreqCusheader(sessionManager.getBusinessId(),sessionManager.getLoggedInUserDetails().getXcus());
	}
	
//Procedure Calls
	@Override
	public void procCreateSOFromSRQ(String xdoreqnum,String p_seq) {
		opreqMapper.procCreateSOFromSRQ(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xdoreqnum, p_seq);
	}

	@Override
	public void procCreateSOFromSRQ_khanas(String xdoreqnum) {
		opreqMapper.procCreateSOFromSRQ_Khanas(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xdoreqnum);
	}
	
	
	
}
