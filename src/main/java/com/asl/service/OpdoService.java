package com.asl.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Opdodetail;
import com.asl.entity.Opdoheader;
import com.asl.model.BranchesRequisitions;
import com.asl.model.ServiceException;

@Component
public interface OpdoService {

	public long save(Opdoheader opdoHeader);

	public long update(Opdoheader opdoHeader);
	
	public long delete(String xdornum);

	public long updateOpdoHeaderTotalAmt(String xdornum);

	public long updateOpdoHeaderTotalAmtAndGrandTotalAmt(String xdornum);

	public long updateOpdoHeaderGrandTotalAmt(String xdornum);

	public long saveDetail(Opdodetail opdoDetail);

	public long updateDetail(Opdodetail opdoDetail);

	public long deleteDetail(Opdodetail opdoDetail);

	public Opdoheader findOpdoHeaderByXdornum(String xdornum);

	public List<Opdoheader> getAllOpdoHeader();

	public List<Opdoheader> getAllRandomOpdoHeader();
	
	public List<Opdoheader> getAllDirectOpdoHeader();

	public Opdodetail findOpdoDetailByXdornumAndXrow(String xdornum, int xrow);

	public Opdodetail findOpdoDetailByXdornumAndXitem(String xdornum, String xitem);

	public List<Opdodetail> findOpdoDetailByXdornum(String xdornum);
	
	public List<Opdodetail> findAssignedOpdoDetailByChalan(String chalan);

	public List<Opdoheader> findAllInvoiceOrder(String xtypetrn, String xtrn, String xstatus, Date date);

	public List<Opdoheader> findAllInvoiceOrderByChalan(String xtypetrn, String xtrn, String xchalanref);

	public List<Opdoheader> findAllOpdoHeaderByXtypetrnAndXtrn(String xtypetrn, String xtrn);
	
	public List<Opdoheader> findAllDirectOpdoHeaderByXtypetrnAndXtrn(String xtypetrn, String xtrn);

	public List<Opdoheader> searchOpdoHeader(String xtypetrn, String xdornum);

	public List<Opdoheader> searchOpdoHeader(String xtypetrn, String xstatusord, String xdornum);

	public long createSalesFromChalan(String xordernum) throws ServiceException;

	// Procedure Calls
	public void procConfirmDO(String xdornum, String p_seq);

	public void procIssuePricing(String xdornum, String xwh, String p_screen, String p_seq);

	public void procTransferOPtoAR(String xdornum, String p_screen, String p_seq);
	public void procTransferOPtoGL(String xdornum, String p_screen, String p_seq);
	public void procrequpdate(String xdornum);

	// Search menu
	public List<Opdoheader> findOpdoXdornum(String xdornum);

	public List<BranchesRequisitions> getSalesInvoiceMatrxi(Date xdate);

	public List<Opdoheader> findOpdoheaderByXordernum(String xordernum);

	public boolean saveInvoiceDetail(Opdodetail opdodetail) throws ServiceException;

	public boolean deleteInvoiceDetail(String xdornum, int xrow) throws ServiceException;
}
