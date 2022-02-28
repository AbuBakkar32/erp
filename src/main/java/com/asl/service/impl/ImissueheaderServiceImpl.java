package com.asl.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Imissuedetail;
import com.asl.entity.Imissueheader;
import com.asl.entity.Poreqheader;
import com.asl.mapper.ImissueheaderMapper;
import com.asl.service.ImissueheaderService;
@Service
public class ImissueheaderServiceImpl extends AbstractGenericService implements ImissueheaderService{

	@Autowired private ImissueheaderMapper imissueheaderMapper;
	
	@Transactional
	@Override
	public long saveImissueheader(Imissueheader imissueheader) {
		if (imissueheader == null)
			return 0;
		imissueheader.setZid(sessionManager.getBusinessId());
		imissueheader.setZauserid(getAuditUser());
		return imissueheaderMapper.saveImissueheader(imissueheader);
	}

	@Transactional
	@Override
	public long updateImissueheader(Imissueheader imissueheader) {
		if (imissueheader == null)
			return 0;
		imissueheader.setZid(sessionManager.getBusinessId());
		imissueheader.setZauserid(getAuditUser());
		return imissueheaderMapper.updateImissueheader(imissueheader);
	}

	@Transactional
	@Override
	public long updateImissueheaderTotalAmt(Imissuedetail imissuedetail) {
		if(imissuedetail == null) return 0;
		return imissueheaderMapper.updateImissueheaderTotalAmt(imissuedetail);
	}

	@Transactional
	@Override
	public long deleteImissueheader(Imissueheader imissueheader) {
		if(imissueheader == null) return 0;
		long count = imissueheaderMapper.deleteImissueheader(imissueheader);
		return count;
	}

	@Override
	public List<Imissueheader> getAllImissueheader() {
		return imissueheaderMapper.getAllImissueheader(sessionManager.getBusinessId());
	}

	@Override
	public Imissueheader findByImissueheader(String xissuenum) {
		if (StringUtils.isBlank(xissuenum))
			return null;
		return imissueheaderMapper.findByImissueheader(xissuenum, sessionManager.getBusinessId());
	}

	//Issue Details
	@Transactional
	@Override
	public long saveImissuedetail(Imissuedetail imissuedetail) {
		if ( imissuedetail == null)return 0;
		imissuedetail.setZid(sessionManager.getBusinessId());
		imissuedetail.setZauserid(getAuditUser());
		long count = imissueheaderMapper.saveImissuedetail(imissuedetail);
		if(count != 0) {
			count = updateImissueheaderTotalAmt(imissuedetail);
		}
		return count;
	}

	@Transactional
	@Override
	public long updateImissuedetail(Imissuedetail imissuedetail) {
		if ( imissuedetail == null)return 0;
		imissuedetail.setZid(sessionManager.getBusinessId());
		imissuedetail.setZauserid(getAuditUser());
		long count = imissueheaderMapper.updateImissuedetail(imissuedetail);
		if(count != 0) {
			count = updateImissueheaderTotalAmt(imissuedetail);
		}
		return count;
	}

	@Transactional
	@Override
	public long deleteImissuedetail(Imissuedetail imissuedetail) {
		if(imissuedetail == null) return 0;
		long count = imissueheaderMapper.deleteImissuedetail(imissuedetail);
		if(count != 0) {
			count = updateImissueheaderTotalAmt(imissuedetail);
		}
		return count;
	}

	@Override
	public List<Imissuedetail> getAllImissuedetail() {
		return imissueheaderMapper.getAllImissuedetail(sessionManager.getBusinessId());
	}

	@Override
	public List<Imissuedetail> findByImissuedetail(String xissuenum) {
		if(StringUtils.isBlank(xissuenum)) return null;
		return imissueheaderMapper.findByImissuedetail(xissuenum,sessionManager.getBusinessId());
	}

	@Override
	public Imissuedetail findImissuedetailByXissuenumAndXrow(String xissuenum, int xrow) {
		if(StringUtils.isBlank(xissuenum) || xrow == 0) return null;
		return imissueheaderMapper.findImissuedetailByXissuenumAndXrow(xissuenum,xrow,sessionManager.getBusinessId());
	}

	@Override
	public Imissuedetail findImissuedetailByXissuenumAndXitem(String xissuenum, String xitem) {
		if(StringUtils.isBlank(xissuenum) || StringUtils.isBlank(xitem)) return null;
		return imissueheaderMapper.findImissuedetailByXissuenumAndXitem(xissuenum,xitem,sessionManager.getBusinessId());
	}

	@Override
	public void procIM_confirmProjectIssue(String xissuenum, String p_seq) {
		imissueheaderMapper.procIM_confirmProjectIssue(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xissuenum, p_seq);
	}

	@Override
	public void procIM_confirmProjectIssueGL(String xissuenum, String p_seq) {
		imissueheaderMapper.procIM_confirmProjectIssueGL(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xissuenum, p_seq);
	}

	@Override
	public List<Imissueheader> getALllImissueheaderByXwh(String xwh) {
		if(StringUtils.isBlank(xwh)) return null;
		return imissueheaderMapper.getALllImissueheaderByXwh(xwh,sessionManager.getBusinessId());
	}

}
