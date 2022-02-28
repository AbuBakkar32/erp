package com.asl.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Xtrnp;
import com.asl.mapper.XtrnpMapper;
import com.asl.service.XtrnpService;


/**
 * @author Zubayer Ahamed
 * @since Feb 27, 2021
 */
@Service
public class XtrnpServiceImpl extends AbstractGenericService implements XtrnpService {

	@Autowired private XtrnpMapper xtrnpMapper;


	@Override
	public long save(Xtrnp xtrnp){
		if(xtrnp == null || StringUtils.isBlank(xtrnp.getXtypetrn()) || StringUtils.isBlank(xtrnp.getXtrn())) return 0;
		xtrnp.setZid(sessionManager.getBusinessId());
		xtrnp.setZauserid(getAuditUser());
		return xtrnpMapper.save(xtrnp);
	}


	@Override
	public long update(Xtrnp xtrnp){
		if(xtrnp == null || StringUtils.isBlank(xtrnp.getXtypetrn()) || StringUtils.isBlank(xtrnp.getXtrn())) return 0;
		xtrnp.setZid(sessionManager.getBusinessId());
		xtrnp.setZuuserid(getAuditUser());
		return xtrnpMapper.update(xtrnp);
	}

	@Override
	public List<Xtrnp> getAllXtrnp() {
		return xtrnpMapper.getAllXtrnp(sessionManager.getBusinessId());
	}


	@Override
	public Xtrnp findXtrnpByXvoucher(String xtypetrn, String xtrn, String xtyperel) {
		if(StringUtils.isBlank(xtypetrn) || StringUtils.isBlank(xtrn) || StringUtils.isBlank(xtyperel)) return null;
		return xtrnpMapper.findXtrnpByXvoucher(xtypetrn, xtrn,xtyperel, sessionManager.getBusinessId());
	}

	@Transactional
	@Override
	public long delete(Xtrnp xtrnp) {
		if(StringUtils.isBlank(xtrnp.getXtypetrn()) || StringUtils.isBlank(xtrnp.getXtrn()) || StringUtils.isBlank(xtrnp.getXtyperel())) return 0;
		return xtrnpMapper.delete(xtrnp);
	}
}
