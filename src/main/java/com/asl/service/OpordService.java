package com.asl.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.asl.entity.Caitem;
import com.asl.entity.Oporddetail;
import com.asl.entity.Opordheader;
import com.asl.model.BranchesRequisitions;
import com.asl.model.ResponseHelper;
import com.asl.model.ServiceException;

/**
 * @author Zubayer Ahamed
 * @since Mar 8, 2021
 */
@Component
public interface OpordService {

	public long saveOpordHeader(Opordheader opordheader);

	public long updateOpordHeader(Opordheader opordheader);
	
	public long deleteOpordHeader(String xordernum);

	public long saveOpordDetail(Oporddetail oporddetail);

	public long saveBatchOpordDetail(List<Oporddetail> opordDetails);

	public long updateOpordDetail(Oporddetail oporddetail);

	public long deleteOpordDetail(Oporddetail oporddetail);
	
	public long batchDeleteOpordDetail(List<Oporddetail> oporddetail);

	public Opordheader findOpordHeaderByXordernum(String xordernum);

	public Oporddetail findOporddetailByXordernumAndXrow(String xordernum, int xrow);

	public List<Oporddetail> findOporddetailByXordernum(String xordernum);

	public List<Opordheader> getAllOpordheader();

	public Opordheader findOpordHeaderByXtypetrnAndXpornumAndXdateAndXcus(String xtypetrn, String xpornum, String xcus,
			Date xdate);

	public Opordheader findOpordHeaderByXtypetrnAndXtrnAndXdate(String xtypetrn, String xtrn, Date xdate);

	public List<Opordheader> findAllOpordHeaderByXtypetrnAndXtrnAndXdate(String xtypetrn, String xtrn, Date xdate);

	public List<Opordheader> findAllOpordHeaderByXtypetrnAndXtrnAndXdateAndXstatus(String xtypetrn, String xtrn,
			String xstatus);

	public Oporddetail findOporddetailByXordernumAndXitem(String xordernum, String xitem);

	public List<Oporddetail> findAllOporddetailByXordernumAndXitem(String xordernum, String xitem);

	public List<BranchesRequisitions> getSalesOrderMatrxi(Date xdate);

	public List<Opordheader> findAllOpordHeaderByXtypetrnAndXtrn(String xtypetrn, String xtrn);
	
	public List<Opordheader> findAllOpenOpordHeaderByXtypetrnAndXtrn(String xtypetrn, String xtrn);

	public List<Opordheader> findAllProductionCompletedChalan(String xtypetrn, String xtrn);

	public List<Opordheader> findAllSalesOrder(String xtypetrn, String xtrn, String xstatusord, Date xdate);

	public List<Opordheader> findAllSalesOrderByChalan(String xtypetrn, String xtrn, String xchalanref);

	public List<Opordheader> searchOpordheaderByXtypetrnAndXtrnAndXordernum(String xtypetrn, String xtrn,
			String xordernum, String xstatus);
	public List<Opordheader> findAllOpenOpordHeaderByRawMaterials(String xtypetrn, String xtrn);
	
	public List<Opordheader> searchXpornum(String xpornum);
	
	public List<Caitem> findAvailableRoomsByDate(Date xcheckindate);
	
	public List<Caitem> findBookedRoomsByDate(Date xfuncdate);
	
	public List<Oporddetail> findBookedRoomsByXordernum(String xordernum);
	
	public List<Caitem> findAvailableHallsByDate(Date xfuncdate);
	
	public List<Caitem> findBookedHallsByXfuncdate(Date xfuncdate);
	
	public List<Oporddetail> findBookedHallsByXordernum(String xordernum);

	public long updateOpordHeaderTotalAmtAndGrandTotalAmt(String xordernum);

	public long archiveAllOporddetailByXordernum(String xordernum);

	public List<Oporddetail> findAllSubitemDetail(String xordernum, int xparentrow, String xtype);

	public long deleteSubItems(String xordernum, int xparentrow, String xtype);

	public Map<String, Object> createSalesOrderToInvoice(String xordernum, ResponseHelper response) throws ServiceException;

	public List<Oporddetail> searchSalesOrderAvailableItem(String xordernum, String hint);

	public Map<String, Object> createSalesOrderToChalan(String[] xordernum) throws ServiceException;
}
