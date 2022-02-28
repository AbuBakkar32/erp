package com.asl.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Acdetail;
import com.asl.entity.Cacus;
import com.asl.entity.LandDagDetails;
import com.asl.entity.LandInfo;
import com.asl.entity.PogrnDetail;
import com.asl.entity.PogrnHeader;

@Mapper
public interface PogrnMapper {
	public long savePogrnHeader(PogrnHeader pogrnHeader);

	public long updatePogrnHeader(PogrnHeader pogrnHeader);

	public long updatePogrnHeaderTotalAmt(PogrnDetail pogrnDetail);

	public long updatePogrnHeaderTotalAmtAndGrandTotalAmt(String xgrnnum, String zid);

	public long savePogrnDetail(PogrnDetail pogrnDetail);

	public long updatePogrnDetail(PogrnDetail pogrnDetail);

	public long deletePogrnDetail(PogrnDetail pogrnDetail);

	public PogrnHeader findPogrnHeaderByXgrnnum(String xgrnnum, String zid);

	public List<PogrnHeader> findPogrnHeaderByXpornum(String xpornum, String zid);

	public PogrnDetail findPogrnDetailByXgrnnumAndXrow(String xgrnnum, int xrow, String zid);

	public List<PogrnDetail> findPogrnDetailByXgrnnum(String xgrnnum, String zid);

	public List<PogrnHeader> getAllPogrnHeader(String zid);
	
	public List<PogrnHeader> getAllDirectPogrnHeader(String zid);

	public List<PogrnHeader> searchPoord(String xpornum, String zid);

	public long archiveDetailsByXgrnnum(String xgrnnum, String zuuserid, String zid);

	public long countOfPogrndetailByXgrnnum(String xgrnnum, String zid);

	public long deletePogrnheader(String xgrnnum, String zid);

	public BigDecimal getTotalGRNQtyOfConfirmedGRNDetail(int xdocrow, String xpornum, String zid);

	public List<PogrnHeader> searchGrn(String hint, String xstatusgrn, String zid);

	//Procedures
	public void procTransferPOtoAP(String zid, String user, String xgrnnum, String p_seq);

	public void poTransferPOtoGL(String zid, String user, String xgrnnum, String pscreen, String p_seq);
	
	public void procPO_confirmGRN(String zid, String user, String xgrnnum, String p_seq);

	public void procPO_confirmGRN_ProjectCost(String zid, String user, String xgrnnum, String p_seq);

	public void procPO_confirmProjectQC(String zid, String user, String xgrnnum, String p_seq);


	//For Document Retirement
	public List<PogrnHeader> getAllDocumentPogrnHeader(String zid);

	//For QC
	public List<PogrnHeader> getAllQCPogrnHeader(String zid);

	public long updatePogrnHeaderWithDetail(String xgrnnum, String zid);
}
