package com.asl.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.Imtag;
import com.asl.entity.Imtdet;
import com.asl.mapper.ImtagMapper;
import com.asl.service.ImtagService;

@Service
public class ImtagServiceImpl extends AbstractGenericService implements ImtagService {
	
	@Autowired
	private ImtagMapper imtagMapper;

	@Override
	public long saveImtag(Imtag imtag) {
		if (imtag == null)
			return 0;
		imtag.setZid(sessionManager.getBusinessId());
		imtag.setZauserid(getAuditUser());
		return imtagMapper.saveImtag(imtag);
	}

	@Override
	public long updateImtag(Imtag imtag) {
		if (imtag == null || StringUtils.isBlank(imtag.getXtagnum())) return 0;
		imtag.setZid(sessionManager.getBusinessId());
		imtag.setZuuserid(getAuditUser());
		return imtagMapper.updateImtag(imtag);
	}
	
	@Override
	public long deleteImtag(Imtag imtag) {
		if(imtag == null) return 0;
		long count = imtagMapper.deleteImtag(imtag);
		return count;
	}

	@Override
	public long saveImtdet(Imtdet imtdet) {
		if(imtdet == null || StringUtils.isBlank(imtdet.getXtagnum())) return 0;
		imtdet.setZid(sessionManager.getBusinessId());
		imtdet.setZauserid(getAuditUser());
		long count = imtagMapper.saveImtdet(imtdet);
		return count;
	}

	@Override
	public long updateImtdet(Imtdet imtdet) {
		if(imtdet == null || StringUtils.isBlank(imtdet.getXtagnum())) return 0;
		imtdet.setZid(sessionManager.getBusinessId());
		imtdet.setZuuserid(getAuditUser());
		long count = imtagMapper.updateImtdet(imtdet);
		return count;
	}

	@Override
	public long deleteImtdet(Imtdet imtdet) {
		if(imtdet == null) return 0;
		long count = imtagMapper.deleteImtdet(imtdet);
		return count;
	}

	@Override
	public Imtag findImtagByXtagnum(String xtagnum) {
		if(StringUtils.isBlank(xtagnum))
			return null;
		return imtagMapper.findImtagByXtagnum(xtagnum, sessionManager.getBusinessId());
	}

	@Override
	public List<Imtag> getAllImTag() {
		return imtagMapper.getAllImTag(sessionManager.getBusinessId());
	}

	@Override
	public Imtdet findImtdetByXtagnumAndXrow(String xtagnum, int xrow) {
		if(StringUtils.isBlank(xtagnum) || xrow == 0) return null;
		return imtagMapper.findImtdetByXtagnumAndXrow(xtagnum, xrow, sessionManager.getBusinessId());
	}

	@Override
	public Imtdet findImtdetByXtagnumAndXitem(String xtagnum, String xitem) {
		if(StringUtils.isBlank(xtagnum) || StringUtils.isBlank(xitem)) return null;
		return imtagMapper.findImtdetByXtagnumAndXitem(xtagnum, xitem, sessionManager.getBusinessId());
	}

	@Override
	public List<Imtdet> findImtdetByXtagnum(String xtagnum) {
		if(StringUtils.isBlank(xtagnum)) return Collections.emptyList();
		return imtagMapper.findImtdetByXtagnum(xtagnum, sessionManager.getBusinessId());
	}

	@Override
	public void procStockTake(Date xdate, String xtagnum, String p_seq) {
		imtagMapper.procStockTake(sessionManager.getBusinessId(),  sessionManager.getLoggedInUserDetails().getUsername(), xdate, xtagnum, p_seq);
	}
}
