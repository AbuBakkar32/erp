package com.asl.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Pdeducation;
import com.asl.entity.Pdexperience;
import com.asl.entity.Pdgradedt;
import com.asl.entity.Pdmst;
import com.asl.entity.Pdpromodt;
import com.asl.entity.Pdsalarydetail;
import com.asl.entity.Pdtransdt;
import com.asl.entity.Xcodes;
import com.asl.mapper.PdmstMapper;
import com.asl.service.PdmstService;

/**
 * @author Zubayer Ahamed
 * @since May 8, 2021
 */
@Service
public class PdmstServiceImpl extends AbstractGenericService implements PdmstService {

	@Autowired private PdmstMapper pdmstMapper;

	@Override
	@Transactional
	public long save(Pdmst pdmst) {
		if(pdmst == null || StringUtils.isBlank(pdmst.getXtypetrn()) || StringUtils.isBlank(pdmst.getXtrn())) return 0;
		pdmst.setZid(sessionManager.getBusinessId());
		pdmst.setZauserid(getAuditUser());
		return pdmstMapper.save(pdmst);
	}

	@Override
	public long update(Pdmst pdmst) {
		if(pdmst == null || StringUtils.isBlank(pdmst.getXtypetrn())) return 0;
		pdmst.setZid(sessionManager.getBusinessId());
		pdmst.setZuuserid(getAuditUser());
		return pdmstMapper.update(pdmst);
	}
	
	@Override
	public long delete(Pdmst pdmst) {
		if(pdmst == null) return 0;
		long count = pdmstMapper.delete(pdmst);
		return count;
	}

	@Override
	public List<Pdmst> getAll(Boolean zactive) {
		return pdmstMapper.getAllPdmst(sessionManager.getBusinessId(), zactive);
	}
	
	@Override
	public List<Pdmst> getAllPdmstByXtrn(String xtypetrn) {
		if(StringUtils.isBlank(xtypetrn)) return null;
		return pdmstMapper.getAllPdmstByXtrn(xtypetrn, sessionManager.getBusinessId());
	}

	@Override
	public Pdmst findByXstaff(String xstaff, Boolean zactive) {
		if(StringUtils.isBlank(xstaff)) return null;
		return pdmstMapper.findPdmstByXstaff(xstaff, sessionManager.getBusinessId(), zactive);
	}

	@Override
	public List<Pdmst> searchStaff(String hint, String xtypetrn) {
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		return pdmstMapper.searchStaff(hint.toUpperCase(), xtypetrn, sessionManager.getBusinessId());
	}
	
	@Override
	public List<Pdmst> searchPayrollStaff(String hint, String xtypetrn) {
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		return pdmstMapper.searchPayrollStaff(hint.toUpperCase(), xtypetrn, sessionManager.getBusinessId());
	}


	@Override
	public List<Pdmst> getAllHRPdmst() {
		return pdmstMapper.getAllHRPdmst(sessionManager.getBusinessId());
	}

	@Override
	public List<Pdmst> getAllKhanasPdmst() {
		
		return pdmstMapper.getAllKhanasPdmst( sessionManager.getBusinessId());
	}
	@Override
	public Pdmst findAllPdmst(String xstaff) {
		if(StringUtils.isBlank(xstaff)) return null;
		return pdmstMapper.findAllPdmst(xstaff, sessionManager.getBusinessId());
	}

	//for HRQualification
	@Transactional
	@Override
	public long savePdeducation(Pdeducation pdqua) {
		if (pdqua == null)
			return 0;
		pdqua.setZid(sessionManager.getBusinessId());
		pdqua.setZauserid(getAuditUser());
		return pdmstMapper.savePdeducation(pdqua);
	}

	@Transactional
	@Override
	public long updatePdeducation(Pdeducation pdqua) {
		if (pdqua == null)
			return 0;
		pdqua.setZid(sessionManager.getBusinessId());
		pdqua.setZauserid(getAuditUser());
		return pdmstMapper.updatePdeducation(pdqua);
	}

	@Override
	public long deletePdeducation(Pdeducation pdqua) {
		if(pdqua == null) return 0;
		long count = pdmstMapper.deletePdeducation(pdqua);
		return count;
	}

	@Override
	public List<Pdeducation> getAllPdeducation() {
		return pdmstMapper.getAllPdeducation(sessionManager.getBusinessId());
	}

	@Override
	public List<Pdeducation> findByPdeducation(String xstaff) {
		if (StringUtils.isBlank(xstaff))
			return null;
		return pdmstMapper.findByPdeducation(xstaff, sessionManager.getBusinessId());
	}

	@Override
	public Pdeducation findPdeducationByXstaffAndXrow(String xstaff, int xrow) {
		if(StringUtils.isBlank(xstaff) || xrow == 0) return null;
		return pdmstMapper.findPdeducationByXstaffAndXrow(xstaff,xrow,sessionManager.getBusinessId());
	}
	
	//for HRExperience
	
	@Transactional
	@Override
	public long savePdexperience(Pdexperience pdex) {
		if (pdex == null)
			return 0;
		pdex.setZid(sessionManager.getBusinessId());
		pdex.setZauserid(getAuditUser());
		return pdmstMapper.savePdexperience(pdex);
	}

	@Transactional
	@Override
	public long updatePdexperience(Pdexperience pdex) {
		if (pdex == null)
			return 0;
		pdex.setZid(sessionManager.getBusinessId());
		pdex.setZauserid(getAuditUser());
		return pdmstMapper.updatePdexperience(pdex);
	}

	@Override
	public long deletePdexperience(Pdexperience pdex) {
		if(pdex == null) return 0;
		long count = pdmstMapper.deletePdexperience(pdex);
		return count;
	}

	@Override
	public List<Pdexperience> getAllPdexperience() {
		return pdmstMapper.getAllPdexperience(sessionManager.getBusinessId());
	}

	@Override
	public List<Pdexperience> findByPdexperience(String xstaff) {
		if (StringUtils.isBlank(xstaff))
			return null;
		return pdmstMapper.findByPdexperience(xstaff, sessionManager.getBusinessId());
	}

	@Override
	public Pdexperience findPdexperienceByXstaffAndXrow(String xstaff, int xrow) {
		if(StringUtils.isBlank(xstaff) || xrow == 0) return null;
		return pdmstMapper.findPdexperienceByXstaffAndXrow(xstaff,xrow,sessionManager.getBusinessId());
	}

	//for HRQualification
	@Transactional
	@Override
	public long savePdpromodt(Pdpromodt pdpr) {
		if (pdpr == null)
			return 0;
		pdpr.setZid(sessionManager.getBusinessId());
		pdpr.setZauserid(getAuditUser());
		return pdmstMapper.savePdpromodt(pdpr);
	}

	@Transactional
	@Override
	public long updatePdpromodt(Pdpromodt pdpr) {
		if (pdpr == null)
			return 0;
		pdpr.setZid(sessionManager.getBusinessId());
		pdpr.setZauserid(getAuditUser());
		return pdmstMapper.updatePdpromodt(pdpr);
	}

	@Override
	public long deletePdpromodt(Pdpromodt pdpr) {
		if(pdpr == null) return 0;
		long count = pdmstMapper.deletePdpromodt(pdpr);
		return count;
	}

	@Override
	public List<Pdpromodt> getAllPdpromodt() {
		return pdmstMapper.getAllPdpromodt(sessionManager.getBusinessId());
	}

	@Override
	public List<Pdpromodt> findByPdpromodt(String xstaff) {
		if (StringUtils.isBlank(xstaff))
			return null;
		return pdmstMapper.findByPdpromodt(xstaff, sessionManager.getBusinessId());
	}

	@Override
	public Pdpromodt findPdpromodtByXstaffAndXrow(String xstaff, int xrow) {
		if(StringUtils.isBlank(xstaff) || xrow == 0) return null;
		return pdmstMapper.findPdpromodtByXstaffAndXrow(xstaff,xrow,sessionManager.getBusinessId());
	}

	//for HRTransfer
	@Transactional
	@Override
	public long savePdtransdt(Pdtransdt pdtr) {
		if (pdtr == null)
			return 0;
		pdtr.setZid(sessionManager.getBusinessId());
		pdtr.setZauserid(getAuditUser());
		return pdmstMapper.savePdtransdt(pdtr);
	}

	@Transactional
	@Override
	public long updatePdtransdt(Pdtransdt pdtr) {
		if (pdtr == null)
			return 0;
		pdtr.setZid(sessionManager.getBusinessId());
		pdtr.setZauserid(getAuditUser());
		return pdmstMapper.updatePdtransdt(pdtr);
	}

	@Override
	public long deletePdtransdt(Pdtransdt pdtr) {
		if(pdtr == null) return 0;
		long count = pdmstMapper.deletePdtransdt(pdtr);
		return count;
	}

	@Override
	public List<Pdtransdt> getAllPdtransdt() {
		return pdmstMapper.getAllPdtransdt(sessionManager.getBusinessId());
	}

	@Override
	public List<Pdtransdt> findByPdtransdt(String xstaff) {
		if (StringUtils.isBlank(xstaff))
			return null;
		return pdmstMapper.findByPdtransdt(xstaff, sessionManager.getBusinessId());
	}

	@Override
	public Pdtransdt findPdtransdtByXstaffAndXrow(String xstaff, int xrow) {
		if(StringUtils.isBlank(xstaff) || xrow == 0) return null;
		return pdmstMapper.findPdtransdtByXstaffAndXrow(xstaff,xrow,sessionManager.getBusinessId());
	}

	//for HRDesignation
	@Transactional
	@Override
	public long savePdgradedt(Pdgradedt pdde) {
		if (pdde == null)
			return 0;
		pdde.setZid(sessionManager.getBusinessId());
		pdde.setZauserid(getAuditUser());
		return pdmstMapper.savePdgradedt(pdde);
	}

	@Transactional
	@Override
	public long updatePdgradedt(Pdgradedt pdde) {
		if (pdde == null)
			return 0;
		pdde.setZid(sessionManager.getBusinessId());
		pdde.setZauserid(getAuditUser());
		return pdmstMapper.updatePdgradedt(pdde);
	}

	@Override
	public long deletePdgradedt(Pdgradedt pdde) {
		if(pdde == null) return 0;
		long count = pdmstMapper.deletePdgradedt(pdde);
		return count;
	}

	@Override
	public List<Pdgradedt> getAllPdgradedt() {
		return pdmstMapper.getAllPdgradedt(sessionManager.getBusinessId());
	}

	@Override
	public List<Pdgradedt> findByPdgradedt(String xstaff) {
		if (StringUtils.isBlank(xstaff))
			return null;
		return pdmstMapper.findByPdgradedt(xstaff, sessionManager.getBusinessId());
	}

	@Override
	public Pdgradedt findPdgradedtByXstaffAndXrow(String xstaff, int xrow) {
		if(StringUtils.isBlank(xstaff) || xrow == 0) return null;
		return pdmstMapper.findPdgradedtByXstaffAndXrow(xstaff,xrow,sessionManager.getBusinessId());
	}

	@Override
	public List<Pdmst> getAllPdmstByXtypetrn(String xtypetrn) {
		if(StringUtils.isBlank(xtypetrn)) return Collections.emptyList();
		return pdmstMapper.getAllPdmstByXtypetrn(xtypetrn, sessionManager.getBusinessId());
	}

	@Override
	public long savePdsalarydetail(Pdsalarydetail pdsa) {
		if (pdsa == null)
			return 0;
		pdsa.setZid(sessionManager.getBusinessId());
		pdsa.setZauserid(getAuditUser());
		return pdmstMapper.savePdsalarydetail(pdsa);
	}

	@Override
	public long updatePdsalarydetail(Pdsalarydetail pdsa) {
		if (pdsa == null)
			return 0;
		pdsa.setZid(sessionManager.getBusinessId());
		pdsa.setZauserid(getAuditUser());
		return pdmstMapper.updatePdsalarydetail(pdsa);
	}

	@Override
	public long deletePdsalarydetail(Pdsalarydetail pdsa) {
		if(pdsa == null) return 0;
		long count = pdmstMapper.deletePdsalarydetail(pdsa);
		return count;
	}

	@Override
	public List<Pdsalarydetail> getAllPdsalarydetail() {
		return pdmstMapper.getAllPdsalarydetail(sessionManager.getBusinessId());
	}

	@Override
	public List<Pdsalarydetail> findByPdsalarydetail(String xstaff) {
		if (StringUtils.isBlank(xstaff))
			return null;
		return pdmstMapper.findByPdsalarydetail(xstaff, sessionManager.getBusinessId());
	}

	@Override
	public Pdsalarydetail findPdsalarydetailByXstaffAndXrow(String xstaff, int xrow) {
		if(StringUtils.isBlank(xstaff) || xrow == 0) return null;
		return pdmstMapper.findPdsalarydetailByXstaffAndXrow(xstaff,xrow,sessionManager.getBusinessId());
	}

	@Override
	public List<Xcodes> getXcodes() {
		
		return pdmstMapper.getXcodes(sessionManager.getBusinessId());
	}

	@Override
	public Xcodes getXsign(String xtype) {
		if(StringUtils.isBlank(xtype)) return null;
		return pdmstMapper.getXsign(xtype,sessionManager.getBusinessId());
	}

	@Override
	public void procPD_Salary_Upload(String xstaff, String xgrade, String xgross, Date xdatejoin, String p_seq) {
		pdmstMapper.procPD_Salary_Upload(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xstaff,xgrade,xgross, xdatejoin,p_seq);
		
	}

	
	
}
