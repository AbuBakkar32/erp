package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Arhed;
import com.asl.entity.Pdmst;
import com.asl.enums.TransactionCodeType;
import com.asl.mapper.ArhedMapper;
import com.asl.mapper.LandInfoMapper;
import com.asl.service.ArhedService;
import com.asl.service.LandInfoService;

@Service
public class ArhedServiceImpl extends AbstractGenericService implements ArhedService{
	
	@Autowired
	private ArhedMapper arhedMapper;
	@Autowired
	private LandInfoService landinfoService;

	@Transactional
	@Override
	public long save(Arhed arhed) {
		if (arhed == null) return 0;
		arhed.setZid(sessionManager.getBusinessId());
		arhed.setZauserid(getAuditUser());
		long count = arhedMapper.saveArhed(arhed);
		if(count !=0 ) {
			updatexamtar(arhed.getXvoucher(), arhed.getXland(), arhed.getXcus());
			updatexamtrv(arhed.getXvoucher(), arhed.getXland(), arhed.getXcus());
		}
		return count;
	}

	
	@Transactional
	@Override
	public long update(Arhed arhed) {
		if (arhed == null || StringUtils.isBlank(arhed.getXvoucher())) return 0;
		arhed.setZid(sessionManager.getBusinessId());
		arhed.setZuuserid(getAuditUser());
		long count =  arhedMapper.updateArhed(arhed);
		if(count !=0 ) {
			updatexamtar(arhed.getXvoucher(), arhed.getXland(), arhed.getXcus());
			updatexamtrv(arhed.getXvoucher(), arhed.getXland(), arhed.getXcus());
		}
		return count;
	}

	@Override
	public Arhed findArhedByXvoucher(String xvoucher) {
		if (StringUtils.isBlank(xvoucher)) return null;
		return arhedMapper.findArhedByXvoucher(xvoucher, sessionManager.getBusinessId());
	}

	@Override
	public List<Arhed> getAllArheds() {
		return arhedMapper.getAllArhed(sessionManager.getBusinessId());
	}
	
	@Override
	public List<Arhed> getAllArhedByXtrn(String xtrn) {
		if(StringUtils.isBlank(xtrn)) return null;
		return arhedMapper.getAllArhedByXtrn(xtrn, sessionManager.getBusinessId());
	}

	@Override
	public List<Arhed> getAllArhedByXtype(String xtype) {
		if(StringUtils.isBlank(xtype)) return null;
		return arhedMapper.getAllArhedByXtype(xtype, sessionManager.getBusinessId());
	}

	
	@Override
	public Arhed findObapByXcus(String xcus) {
		if(StringUtils.isBlank(xcus)) return null;
		return arhedMapper.findObapByXcus(xcus, TransactionCodeType.ACCOUNT_OBAP.getdefaultCode(), sessionManager.getBusinessId());
	}

	@Override
	public List<Arhed> getAllObaps() {
		return arhedMapper.getAllObaps(TransactionCodeType.ACCOUNT_OBAP.getdefaultCode(), sessionManager.getBusinessId());
	}
	@Override
	public Arhed findObarByXcus(String xcus) {
		if(StringUtils.isBlank(xcus)) return null;
		return arhedMapper.findObarByXcus(xcus, TransactionCodeType.ACCOUNT_OBAR.getdefaultCode(), sessionManager.getBusinessId());
	}

	@Override
	public List<Arhed> getAllObars() {
		return arhedMapper.getAllObars(TransactionCodeType.ACCOUNT_OBAR.getdefaultCode(), sessionManager.getBusinessId());
	}
	@Override
	public Arhed findAdapByXcus(String xcus) {
		if(StringUtils.isBlank(xcus))
			return null;		
		return arhedMapper.findAdapByXcus(xcus, TransactionCodeType.ACCOUNT_ADAP.getdefaultCode(), sessionManager.getBusinessId());
	}

	@Override
	public List<Arhed> getAllAdaps() {
		return arhedMapper.getAllAdaps(TransactionCodeType.ACCOUNT_ADAP.getdefaultCode(), sessionManager.getBusinessId());
	}	
	
	@Override
	public Arhed findAdarByXcus(String xcus) {
		if(StringUtils.isBlank(xcus)) return null;
		return arhedMapper.findAdarByXcus(xcus, TransactionCodeType.ACCOUNT_ADAR.getdefaultCode(), sessionManager.getBusinessId());
	}

	@Override
	public List<Arhed> getAllAdars() {
		return arhedMapper.getAllAdars(TransactionCodeType.ACCOUNT_ADAR.getdefaultCode(), sessionManager.getBusinessId());
	}

	@Override
	public List<Pdmst> findByXstaff(String xstaff){
		if(StringUtils.isBlank(xstaff)) return Collections.emptyList();
		return arhedMapper.findByXstaff(xstaff.toUpperCase(), sessionManager.getBusinessId());
	}

	@Transactional
	@Override
	public long deleteVoucher(String xvoucher) {
		if(StringUtils.isBlank(xvoucher)) return 0;
		return arhedMapper.deleteVoucher(xvoucher, sessionManager.getBusinessId());
	}
	
	@Override
	public List<Arhed> searchPurpose(String xpurpose){
		if(StringUtils.isBlank(xpurpose)) return null;
		return arhedMapper.searchPurpose(xpurpose, sessionManager.getBusinessId());
	}

	@Transactional
	@Override
	public List<Arhed> getAllArhedByXtrnarhedAndXtype(String xtrnarhed, String xtype) {
		if(StringUtils.isBlank(xtrnarhed) || StringUtils.isBlank(xtype)) return Collections.emptyList();
		return arhedMapper.getAllArhedByXtrnarhedAndXtype(xtrnarhed, xtype, sessionManager.getBusinessId());
	}

	@Override
	public long findBankHead(String xvoucher, String xbank) {
		if(StringUtils.isBlank(xvoucher) || StringUtils.isBlank(xbank)) return 0;
		return arhedMapper.findBankHead(xvoucher, xbank, sessionManager.getBusinessId());
	}

	@Override
	public void procTransferARtoGL(String xvoucher, String p_seq) {
		arhedMapper.procTransferARtoGL(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xvoucher, p_seq);
		
	}

	@Override
	public void procTransferARAP_Adjustment(String xvoucher,String xtrn, String p_seq) {
		arhedMapper.procTransferARAP_Adjustment(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xvoucher, xtrn, p_seq);
		
	}

	@Transactional
	@Override
	public void AP_transferAPtoGL(String xvoucher, String p_seq) {
		arhedMapper.AP_transferAPtoGL(sessionManager.getBusinessId(), getAuditUser(), xvoucher, p_seq);
	}

	@Override
	public long updatexamtar(String xvoucher, String xland, String xcus) {
		if(StringUtils.isBlank(xland) || StringUtils.isBlank(xcus) || StringUtils.isBlank(xcus)) return 0;
		arhedMapper.updatexamtar(xvoucher, xland, xcus,sessionManager.getBusinessId());
		return 0;
	}
	
	@Override
	public long updatexamtrv(String xvoucher, String xland, String xcus) {
		if(StringUtils.isBlank(xland) || StringUtils.isBlank(xcus) || StringUtils.isBlank(xcus)) return 0;
		arhedMapper.updatexamtrv(xvoucher, xland, xcus,sessionManager.getBusinessId());
		return 0;
	}

	
}
