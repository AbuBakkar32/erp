package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Cacus;
import com.asl.entity.LandDagDetails;
import com.asl.entity.PogrnHeader;
import com.asl.entity.PoordDetail;
import com.asl.entity.PoordHeader;
import com.asl.entity.Poreqheader;
import com.asl.entity.Poterms;
import com.asl.entity.Termsdef;
import com.asl.entity.Termstrn;

@Mapper
public interface PoordMapper {

	public long savePoordHeader(PoordHeader poordHeader);

	public long updatePoordHeader(PoordHeader poordHeader);

	public long updatePoordHeaderTotalAmt(PoordDetail poordDetail);

	public long savePoordDetail(PoordDetail poordDetail);

	public long savePoordDetailWithRow(PoordDetail poordDetail);

	public long updatePoordDetail(PoordDetail poordDetail);

	public long deletePoordDetail(PoordDetail poordDetail);

	public long deleteDetailByXpornum(String xpornum, String zid);

	public long archiveAllPoordDetailByXpornum(String xpornum, String zid);

	public long countOfRequisitionDetailsByXpornum(String xpornum, String zid);

	public PoordHeader findPoordHeaderByXpornum(String xpornum,String zid);

	public PoordHeader findBranchPoordHeaderByXpornumForCentral(String xpornum, String branchzid);

	public List<PoordHeader> findPoordHeaderByXporeqnum(String xporeqnum, String zid);
	
	public PoordDetail findPoorddetailByXpornumAndXrow(String xpornum, int xrow, String zid);

	public List<PoordDetail> findPoorddetailByXpornum(String xpornum, String zid, String centralzid);

	public List<PoordDetail> findPoordDetailsByXpornumAndBranchZid(String xpornum, String branchzid);

	public List<PoordHeader> getAllPoordHeader(String zid);

	public List<PoordHeader> getPoordHeadersByXtypetrn(String xtypetrn, String zid);

	public List<PoordHeader> getALllPoordHeaderByXpreparer(String zid, String xpreparer);

	public PoordDetail findPoorddetailByXpornumAndXitem(String xpornum, String xitem, String zid);
	
	public List<PoordHeader> searchXpornum(String xpornum, String zid);

	public Cacus findBranchCustomerByRequsitionNumber(String xpornum, String branchid, String zid);

	public Cacus findBranchCustomerByXcus(String xcus, String zid);

	public List<com.asl.model.report.RM0301> getRM0301(String fdate, String tdate, String xcus, String xstatuspor, String xitem, String zid);

	public long deletePoordheaderByXpornum(String xpornum, String zid);

	public List<PoordDetail> searchPurchaseOrderAvailableItem(String xpornum, String xitem, String zid);
	
	//For LCM
	public List<PoordHeader> getAllLCMPoordHeaders(String xtypetrn,String zid);
	
	//For Terms & Conditions
	public long saveTermstrn(Termstrn termstrn);

	public long updateTermstrn(Termstrn termstrn);

	public long deleteTermstrn(Termstrn termstrn);
	
	public List<Termstrn> getAllTermstrn(String zid);
	
	public List<Termstrn> findTermstrnByXdocnum(String xdocnum, String zid);

	public List<Termsdef> getDefaultTermsdef(String zid);

	public Termstrn findTermstrnByXtypeAndXterm(String xtype, String xterm, String zid);

	public Termstrn findTermstrnByXtypeAndXtermAndXdocnum(String xtype, String xterm,String xdocnum, String zid);

	// Procedure Calls
	public void procSP_CREATEQC_FROMWO(String zid, String user, String xpornum, String p_seq);

	public void procAdd_default_terms(String zid, String user, String xpornum, String xtype, String p_seq);

	public void procPO_confirmGRN(String zid, String user, String xgrnnum, String p_seq);

	public void poTransferPOtoGL(String zid, String user, String xgrnnum, String pscreen, String p_seq);

	public void procTransferPOtoAP(String zid, String user, String xgrnnum, String p_seq);

	public void procSP_CREATEGRN_FROMPO(String zid, String user, String xpornum, String p_screen, String p_seq);

}
