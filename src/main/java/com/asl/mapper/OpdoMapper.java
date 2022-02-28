package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Opdodetail;
import com.asl.entity.Opdoheader;
import com.asl.entity.PogrnHeader;
import com.asl.model.BranchesRequisitions;

@Mapper
public interface OpdoMapper {
	public long saveOpdoHeader(Opdoheader opdoheader);
	public long updateOpdoHeader(Opdoheader opdoheader);
	public long deleteOpdoHeader(String xdornum, String zid);
	public long updateOpdoHeaderTotalAmt(String xdornum, String zid);
	public long updateOpdoHeaderGrandTotalAmt(String xdornum, String zid);
	public long updateOpdoHeaderTotalAmtAndGrandTotalAmt(String xdornum, String zid);

	public long saveOpdoDetail(Opdodetail opdodetail);
	public long updateOpdoDetail(Opdodetail opdodetail);
	public long deleteOpdoDetail(Opdodetail opdodetail);

	public Opdoheader findOpdoHeaderByXdornum(String xdornum, String zid);
	public List<Opdoheader> getAllOpdoHeader(String zid);
	public List<Opdoheader> getAllRandomOpdoHeader(String zid);	
	public List<Opdoheader> getAllDirectOpdoHeader(String zid);

	public Opdodetail findOpdoDetailByXdornumAndXrow(String xdornum, int xrow, String zid);
	public Opdodetail findOpdoDetailByXdornumAndXitem(String xdornum, String xitem, String zid);
	public List<Opdodetail> findOpdoDetailByXdornum(String xdornum, String zid, String centralzid);
	public List<Opdoheader> findOpdoXdornum(String xdornum, String zid);
	
	public List<Opdoheader> searchOpdoHeader(String xtypetrn, String xdornum, String zid);
	public List<Opdoheader> searchOpdoHeaderWithSatus(String xtypetrn, String xdornum, String xstatusord, String zid);
	
	//For Delivery Chalan
	public List<Opdoheader> findAllInvoiceOrder(String xtypetrn, String xtrn, String xstatusord, String xdate, String zid);
	public List<Opdoheader> findAllInvoiceOrderByChalan(String xtypetrn, String xtrn, String xchalanref, String zid);
	public List<Opdoheader> findAllOpdoHeaderByXtypetrnAndXtrn(String xtypetrn, String xtrn,  String zid);
	public List<Opdoheader> findAllDirectOpdoHeaderByXtypetrnAndXtrn(String xtypetrn, String xtrn,  String zid);
	//Procedure Calls
	public void procConfirmDO(String zid, String user, String xdornum, String p_seq);
	public void procIssuePricing(String zid, String user, String xdornum, String xwh, String p_screen, String p_seq);
	public void procTransferOPtoAR(String zid, String user, String xdornum, String p_screen, String p_seq);
	public void procTransferOPtoGL(String zid, String user, String xdornum, String p_screen, String p_seq);
	public void procrequpdate(String zid, String xdornum);
	

	public Opdoheader findPoordHeaderByXordernum(String xordernum, String zid);
	public Opdoheader findPoordHeaderByXordernumAndRequisitionnumber(String xordernum, String requisitionnumber, String zid);

	public List<BranchesRequisitions> getSalesInvoiceMatrxi(String xdate, String zid);

	public List<Opdoheader> findOpdoheaderByXordernum(String xordernum, String zid);
}
