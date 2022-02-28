package com.asl.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Arhed;
import com.asl.entity.Pdeducation;
import com.asl.entity.Pdexperience;
import com.asl.entity.Pdgradedt;
import com.asl.entity.Pdmst;
import com.asl.entity.Pdpromodt;
import com.asl.entity.Pdsalarydetail;
import com.asl.entity.Pdtransdt;
import com.asl.entity.Xcodes;
import com.asl.entity.Xtrn;

/**
 * @author Zubayer Ahamed
 * @since Mar 8, 2021
 */
@Mapper
public interface PdmstMapper {
	public long save(Pdmst pdmst);

	public long update(Pdmst pdmst);
	
	public long delete(Pdmst pdmst);
	
	public List<Pdmst> getAllPdmstByXtypetrn(String xtypetrn, String zid);

	public Pdmst findPdmstByXstaff(String xstaff, String zid, Boolean zactive);

	public List<Pdmst> getAllPdmst(String zid, Boolean zactive);
	
	public List<Pdmst> getAllHRPdmst(String zid);
	
	public List<Pdmst> getAllPdmstByXtrn(String xtypetrn, String zid);
	
	public List<Pdmst> getAllKhanasPdmst(String zid);
	
	public Pdmst findAllPdmst(String xstaff, String zid);
	
	public List<Pdmst> searchStaff(String xstaff, String xtypetrn, String zid);
	
	public List<Pdmst> searchPayrollStaff(String xstaff, String xtypetrn, String zid);
	
	//for HRQualification
	public long savePdeducation(Pdeducation pdqua);
	
	public long updatePdeducation(Pdeducation pdqua);
	
	public long deletePdeducation(Pdeducation pdqua);
	
	public List<Pdeducation> getAllPdeducation(String zid);
	
	public List<Pdeducation> findByPdeducation(String xstaff, String zid);
	
	public Pdeducation findPdeducationByXstaffAndXrow(String xstaff, int xrow, String zid);
	
	//for HRExperience
	public long savePdexperience(Pdexperience pdex);
	
	public long updatePdexperience(Pdexperience pdex);
	
	public long deletePdexperience(Pdexperience pdex);
	
	public List<Pdexperience> getAllPdexperience(String zid);
	
	public List<Pdexperience> findByPdexperience(String xstaff, String zid);
	
	public Pdexperience findPdexperienceByXstaffAndXrow(String xstaff, int xrow, String zid);
	
	//for HRPromotion
	public long savePdpromodt(Pdpromodt pdpr);
	
	public long updatePdpromodt(Pdpromodt pdpr);
	
	public long deletePdpromodt(Pdpromodt pdpr);
	
	public List<Pdpromodt> getAllPdpromodt(String zid);
	
	public List<Pdpromodt> findByPdpromodt(String xstaff, String zid);
	
	public Pdpromodt findPdpromodtByXstaffAndXrow(String xstaff, int xrow, String zid);
	
	//for HRTransfer
	public long savePdtransdt(Pdtransdt pdtr);
	
	public long updatePdtransdt(Pdtransdt pdtr);
	
	public long deletePdtransdt(Pdtransdt pdtr);
	
	public List<Pdtransdt> getAllPdtransdt(String zid);
	
	public List<Pdtransdt> findByPdtransdt(String xstaff, String zid);
	
	public Pdtransdt findPdtransdtByXstaffAndXrow(String xstaff, int xrow, String zid);
	
	//for HRDesignation
	public long savePdgradedt(Pdgradedt pdde);
	
	public long updatePdgradedt(Pdgradedt pdde);
	
	public long deletePdgradedt(Pdgradedt pdde);
	
	public List<Pdgradedt> getAllPdgradedt(String zid);
	
	public List<Pdgradedt> findByPdgradedt(String xstaff, String zid);
	
	public Pdgradedt findPdgradedtByXstaffAndXrow(String xstaff, int xrow, String zid);
	
	//for Payroll Salary Details
	public long savePdsalarydetail(Pdsalarydetail pdsa);
	
	public long updatePdsalarydetail(Pdsalarydetail pdsa);
	
	public long deletePdsalarydetail(Pdsalarydetail pdsa);
	
	public List<Pdsalarydetail> getAllPdsalarydetail(String zid);
	
	public List<Pdsalarydetail> findByPdsalarydetail(String xstaff, String zid);
	
	public Pdsalarydetail findPdsalarydetailByXstaffAndXrow(String xstaff, int xrow, String zid);
	
	public List<Xcodes> getXcodes(String zid);
	
	public Xcodes getXsign(String zid, String xtype);
	
	// Procedure Calls(Auto-Breakdown)
	public void procPD_Salary_Upload(String zid, String user, String xstaff,String xgrade,String xgross, Date xdatejoin,String p_seq);

}
