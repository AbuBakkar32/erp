package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Arhed;
import com.asl.entity.Pdmst;

@Component
public interface ArhedService {
	public long save(Arhed arhed);

	public long update(Arhed arhed);

	public Arhed findArhedByXvoucher(String xvoucher);

	public List<Arhed> getAllArheds();
	
	public List<Arhed> getAllArhedByXtrn(String xtrn);
	
	public List<Arhed> getAllArhedByXtype(String xtype);
	
	public long updatexamtar(String xvoucher, String xland, String xcus);
	
	public long updatexamtrv(String xvoucher,String xland, String xcus);


	// Supplier Opening Entry
	public Arhed findObapByXcus(String xcus);
	
	public List<Arhed> getAllObaps();
	
	// Customer Opening Entry
	public Arhed findObarByXcus(String xcus);

	public List<Arhed> getAllObars();
	

	// Supplier Adjustment
	public Arhed findAdapByXcus(String xcus);
	
	public List<Arhed> getAllAdaps();

	// Customer Adjustment
	public Arhed findAdarByXcus(String xcus);
	
	public List<Arhed> getAllAdars();

	// Search Field
	public List<Pdmst> findByXstaff(String xstaff);
	
	public List<Arhed> searchPurpose(String xpurpose);

	public long deleteVoucher(String xvoucher);

	public List<Arhed> getAllArhedByXtrnarhedAndXtype(String xtrnarhed, String xtype);
	
	// Bank Head
	public long findBankHead(String xbank,String xvoucher);
	
	// Procedure calls
	public void procTransferARtoGL(String xvoucher, String p_seq);
	public void procTransferARAP_Adjustment(String xvoucher, String xtrn,String p_seq);
	public void AP_transferAPtoGL(String xvoucher, String p_seq);
	
	//for Land info
	
}
