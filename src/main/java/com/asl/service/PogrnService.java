package com.asl.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.PogrnDetail;
import com.asl.entity.PogrnHeader;
import com.asl.model.ServiceException;

@Component
public interface PogrnService {

	public long save(PogrnHeader pogrnHeader);

	public long update(PogrnHeader pogrnHeader);

	public long updatePogrnHeaderTotalAmt(PogrnDetail pogrnDetail);
	
	public long updatePogrnHeaderTotalAmtAndGrandTotalAmt(String xgrnnum);

	public long saveDetail(PogrnDetail pogrnDetail);

	public long saveDetails(List<PogrnDetail> pogrnDetail)  throws ServiceException;

	public long updateDetail(PogrnDetail pogrnDetail);

	public long deleteDetail(PogrnDetail pogrnDetail);

	public long archiveDetailsByXgrnnum(String xgrnnum);

	public PogrnHeader findPogrnHeaderByXgrnnum(String xgrnnum);

	public List<PogrnHeader> findPogrnHeaderByXpornum(String xpornum);

	public PogrnDetail findPogrnDetailByXgrnnumAndXrow(String xgrnnum, int xrow);

	public List<PogrnDetail> findPogrnDetailByXgrnnum(String xgrnnum);

	public List<PogrnHeader> getAllPogrnHeader();
	
	public List<PogrnHeader> getAllDirectPogrnHeader();

	// Search
	public List<PogrnHeader> searchPoord(String xpornum);

	public long countOfPogrndetailByXgrnnum(String xgrnnum);

	public long deletePogrnheader(String xgrnnum);

	public BigDecimal getTotalGRNQtyOfConfirmedGRNDetail(int xdocrow, String xpornum);

	public List<PogrnHeader> searchGrn(String hint, String xstatusgrn);

	// Procedures
	public void procTransferPOtoAP(String xgrnnum, String p_seq);

	public void poTransferPOtoGL(String xgrnnum, String pscreen, String p_seq);

	public void procPO_confirmGRN(String xgrnnum, String p_seq);

	public void procPO_confirmGRN_ProjectCost(String xgrnnum, String p_seq);

	public void procPO_confirmProjectQC(String xgrnnum, String p_seq);

	//For Document Retirement
	public List<PogrnHeader> getAllDocumentPogrnHeader();

	//For QC
	public List<PogrnHeader> getAllQCPogrnHeader();
	
	public long updatePogrnHeaderWithDetail(String xgrnnum);

}
