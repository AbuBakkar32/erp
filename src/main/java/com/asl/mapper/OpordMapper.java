package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Caitem;
import com.asl.entity.ConventionBookedDetails;
import com.asl.entity.Opdoheader;
import com.asl.entity.Oporddetail;
import com.asl.entity.Opordheader;
import com.asl.model.BranchesRequisitions;

/**
 * @author Zubayer Ahamed
 * @since Mar 8, 2021
 */
@Mapper
public interface OpordMapper {

	public long saveOpordHeader(Opordheader opordheader);

	public long updateOpordHeader(Opordheader opordheader);

	public long deleteOpordHeader(String xordernum, String zid);

	public long saveOpordDetail(Oporddetail oporddetail);

	public long updateOpordDetail(Oporddetail oporddetail);

	public long deleteOpordDetail(Oporddetail oporddetail);

	public Opordheader findOpordHeaderByXordernum(String xordernum, String zid);

	public Oporddetail findOporddetailByXordernumAndXrow(String xordernum, int xrow, String zid, String centralzid);

	public List<Oporddetail> findOporddetailByXordernum(String xordernum, String zid, String centralzid);

	public List<Opordheader> getAllOpordheader(String zid);

	public Opordheader findOpordHeaderByXtypetrnAndXpornumAndXdateAndXcus(String xtypetrn, String xpornum, String xcus,
			String xdate, String zid);

	public Opordheader findOpordHeaderByXtypetrnAndXtrnAndXdate(String xtypetrn, String xtrn, String xdate, String zid);

	public List<Opordheader> findAllOpordHeaderByXtypetrnAndXtrnAndXdate(String xtypetrn, String xtrn, String xdate,
			String zid);

	public List<Opordheader> findAllOpordHeaderByXtypetrnAndXtrnAndXdateAndXstatus(String xtypetrn, String xtrn,
			String xstatus, String zid);

	public List<Oporddetail> findOporddetailByXordernumAndXitem(String xordernum, String xitem, String zid);

	public List<BranchesRequisitions> getSalesOrderMatrxi(String xdate, String zid);

	public List<Opordheader> findAllOpordHeaderByXtypetrnAndXtrn(String xtypetrn, String xtrn, String zid);

	public List<Opordheader> findAllOpenOpordHeaderByXtypetrnAndXtrn(String xtypetrn, String xtrn, String zid);

	public List<Opordheader> findAllProductionCompletedChalan(String xtypetrn, String xtrn, String zid);

	public List<Opordheader> findAllSalesOrder(String xtypetrn, String xtrn, String xstatusord, String xdate, String zid);

	public List<Opordheader> findAllSalesOrderByChalan(String xtypetrn, String xtrn, String xchalanref, String zid);

	public List<Opordheader> searchOpordheaderByXtypetrnAndXtrnAndXordernum(String xtypetrn, String xtrn,
			String xordernum, String xstatus, String zid);
	public List<Opordheader> findAllOpenOpordHeaderByRawMaterials(String xtypetrn, String xtrn, String zid);

	// search
	public List<Opordheader> searchXpornum(String xpornum, String zid);

	public List<Caitem> findAvailableRoomsByDate(String xcheckindate, String zid);

	public List<Caitem> findBookedRoomsByDate(String xcheckindate, String zid);

	public List<Oporddetail> findBookedRoomsByXordernum(String xordernum, String zid);

	public List<Caitem> findAvailableHallsByDate(String xfuncdate, String zid);

	public List<Caitem> findBookedHallsByXfuncdate(String xfuncdate, String zid);

	public List<Oporddetail> findBookedHallsByXordernum(String xordernum, String zid);

	public long updateOpordHeaderTotalAmtAndGrandTotalAmt(String xordernum, String zid);

	public long archiveAllOporddetailByXordernum(String xordernum, String zid);

	public List<String> allBookedHallsInDateRange(String xstartdate, String xenddate, String xordernum, String zid);
	
	public List<ConventionBookedDetails> allBookedHallsInDateRange2(String xstartdate, String xenddate, String zid);

	public List<Oporddetail> findAllSubitemDetail(String xordernum, int xparentrow, String xtype, String zid);

	public long deleteSubItems(String xordernum, int xparentrow, String xtype, String zid);

	public List<Opdoheader> getAllOpdoHeaderByXordernum(String xordernum, String zid);
	
	public List<Oporddetail> searchSalesOrderAvailableItem(String xordernum, String xitem, String zid);
}
