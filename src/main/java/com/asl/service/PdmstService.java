package com.asl.service;

import java.util.Date;
import java.util.List;

import com.asl.entity.Arhed;
import com.asl.entity.Pdeducation;
import com.asl.entity.Pdexperience;
import com.asl.entity.Pdgradedt;
import com.asl.entity.Pdmst;
import com.asl.entity.Pdpromodt;
import com.asl.entity.Pdsalarydetail;
import com.asl.entity.Pdtransdt;
import com.asl.entity.Xcodes;

/**
 * @author Zubayer Ahamed
 * @since May 8, 2021
 */
public interface PdmstService {

	public long save(Pdmst pdmst);

	public long update(Pdmst pdmst);

	public long delete(Pdmst pdmst);

	public List<Pdmst> getAll(Boolean zactive);

	public List<Pdmst> getAllHRPdmst();

	public List<Pdmst> getAllPdmstByXtrn(String xtypetrn);

	public List<Pdmst> getAllPdmstByXtypetrn(String xtypetrn);

	public List<Pdmst> getAllKhanasPdmst();

	public Pdmst findAllPdmst(String xstaff);

	public Pdmst findByXstaff(String xstaff, Boolean zactive);

	public List<Pdmst> searchStaff(String hint, String xtypetrn);
	
	public List<Pdmst> searchPayrollStaff(String hint, String xtypetrn);

	// for HRQualification
	public long savePdeducation(Pdeducation pdqua);

	public long updatePdeducation(Pdeducation pdqua);

	public long deletePdeducation(Pdeducation pdqua);

	public List<Pdeducation> getAllPdeducation();

	public List<Pdeducation> findByPdeducation(String xstaff);

	public Pdeducation findPdeducationByXstaffAndXrow(String xstaff, int xrow);

	// for HRExperience
	public long savePdexperience(Pdexperience pdex);

	public long updatePdexperience(Pdexperience pdex);

	public long deletePdexperience(Pdexperience pdex);

	public List<Pdexperience> getAllPdexperience();

	public List<Pdexperience> findByPdexperience(String xstaff);

	public Pdexperience findPdexperienceByXstaffAndXrow(String xstaff, int xrow);

	// for HRPromotion
	public long savePdpromodt(Pdpromodt pdpr);

	public long updatePdpromodt(Pdpromodt pdpr);

	public long deletePdpromodt(Pdpromodt pdpr);

	public List<Pdpromodt> getAllPdpromodt();

	public List<Pdpromodt> findByPdpromodt(String xstaff);

	public Pdpromodt findPdpromodtByXstaffAndXrow(String xstaff, int xrow);

	// for HRTransfer
	public long savePdtransdt(Pdtransdt pdtr);

	public long updatePdtransdt(Pdtransdt pdtr);

	public long deletePdtransdt(Pdtransdt pdtr);

	public List<Pdtransdt> getAllPdtransdt();

	public List<Pdtransdt> findByPdtransdt(String xstaff);

	public Pdtransdt findPdtransdtByXstaffAndXrow(String xstaff, int xrow);

	// for HRDesignation
	public long savePdgradedt(Pdgradedt pdde);

	public long updatePdgradedt(Pdgradedt pdde);

	public long deletePdgradedt(Pdgradedt pdde);

	public List<Pdgradedt> getAllPdgradedt();

	public List<Pdgradedt> findByPdgradedt(String xstaff);

	public Pdgradedt findPdgradedtByXstaffAndXrow(String xstaff, int xrow);

	// for Payroll Salary Details
	public long savePdsalarydetail(Pdsalarydetail pdsa);

	public long updatePdsalarydetail(Pdsalarydetail pdsa);

	public long deletePdsalarydetail(Pdsalarydetail pdsa);

	public List<Pdsalarydetail> getAllPdsalarydetail();

	public List<Pdsalarydetail> findByPdsalarydetail(String xstaff);

	public Pdsalarydetail findPdsalarydetailByXstaffAndXrow(String xstaff, int xrow);
	
	public List<Xcodes> getXcodes();
	
	public Xcodes getXsign(String xtype);
	
	// Procedure Calls(Auto-Breakdown)
	public void procPD_Salary_Upload(String xstaff,String xgrade,String xgross, Date xdatejoin,String p_seq);

}
