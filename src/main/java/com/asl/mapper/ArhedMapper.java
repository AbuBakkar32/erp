package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Arhed;
import com.asl.entity.Cacus;
import com.asl.entity.LandInfo;
import com.asl.entity.Pdmst;

@Mapper
public interface ArhedMapper {

	public long saveArhed(Arhed arhed);

	public long updateArhed(Arhed arhed);

	public Arhed findArhedByXvoucher(String xvoucher, String zid);

	public List<Arhed> getAllArhed(String zid);
	
	public List<Arhed> getAllArhedByXtrn(String xtrn, String zid);
	
	public List<Arhed> getAllArhedByXtype(String xtype, String zid);
	
	public long updatexamtar(String xvoucher,String xland, String xcus, String zid);
	
	public long updatexamtrv(String xvoucher,String xland, String xcus, String zid);

	// Supplier Opening Entry
	public Arhed findObapByXcus(String xcus, String xtrn, String zid);

	public List<Arhed> getAllObaps(String xtrn, String zid);
	
	// Customer Opening Entry
	public Arhed findObarByXcus(String xcus, String xtrn, String zid);

	public List<Arhed> getAllObars(String xtrn, String zid);
	
	// Supplier Adjustment
	public Arhed findAdapByXcus(String xcus, String xtrn, String zid);
	
	public List<Arhed> getAllAdaps(String xtrn, String zid);

	// Customer Adjustment
	public Arhed findAdarByXcus(String xcus, String xtrn, String zid);
	
	public List<Arhed> getAllAdars(String xtrn, String zid);

	// Search Field
	public List<Pdmst> findByXstaff(String xstaff, String zid);
	
	public List<Arhed> searchPurpose(String xpurpose, String zid);

	public long deleteVoucher(String xvoucher, String zid);

	public List<Arhed> getAllArhedByXtrnarhedAndXtype(String xtrnarhed, String xtype, String zid);
	
	// Bank HeadR
	public long findBankHead(String xbank,String xvoucher, String zid);
	
	// Procedure Calls
	public void procTransferARtoGL(String zid, String user, String xvoucher, String p_seq);
	public void procTransferARAP_Adjustment(String zid, String user, String xvoucher, String xtrn,String p_seq);
	public void AP_transferAPtoGL(String zid, String user, String xvoucher, String p_seq);
	
	//for Land info
	public Arhed findReceivableAmt(String xvoucher,String xland,String xcus, String zid);
	
}
