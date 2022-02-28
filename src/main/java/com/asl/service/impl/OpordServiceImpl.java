package com.asl.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Caitem;
import com.asl.entity.Opdodetail;
import com.asl.entity.Opdoheader;
import com.asl.entity.Oporddetail;
import com.asl.entity.Opordheader;
import com.asl.enums.TransactionCodeType;
import com.asl.mapper.OpordMapper;
import com.asl.model.BranchesRequisitions;
import com.asl.model.ResponseHelper;
import com.asl.model.ServiceException;
import com.asl.service.CaitemService;
import com.asl.service.OpdoService;
import com.asl.service.OpordService;
import com.asl.service.ProductionSuggestionService;


/**
 * @author Zubayer Ahamed
 * @since Mar 8, 2021
 */
@Service
public class OpordServiceImpl extends AbstractGenericService implements OpordService {

	@Autowired private OpordMapper opordMapper;
	@Autowired private OpdoService opdoService;
	@Autowired private CaitemService caitemService;
	@Autowired private ProductionSuggestionService productionSuggestionService;

	@Transactional
	@Override
	public long saveOpordHeader(Opordheader opordheader) {
		if(opordheader == null) return 0;
		opordheader.setZid(sessionManager.getBusinessId());
		opordheader.setZauserid(getAuditUser());
		opordheader.setCentralzid(getBusinessId());
		return opordMapper.saveOpordHeader(opordheader);
	}

	@Transactional
	@Override
	public long updateOpordHeader(Opordheader opordheader) {
		if(opordheader == null) return 0;
		opordheader.setZid(sessionManager.getBusinessId());
		opordheader.setZuuserid(getAuditUser());
		long count = opordMapper.updateOpordHeader(opordheader);
		updateOpordHeaderTotalAmtAndGrandTotalAmt(opordheader.getXordernum());
		return count;
	}

	@Transactional
	@Override
	public long deleteOpordHeader(String xordernum) {
		if(StringUtils.isBlank(xordernum)) return 0;
		return opordMapper.deleteOpordHeader(xordernum, sessionManager.getBusinessId());
	}

	@Transactional
	@Override
	public long saveOpordDetail(Oporddetail oporddetail) {
		if(oporddetail == null) return 0;
		oporddetail.setZid(sessionManager.getBusinessId());
		oporddetail.setZauserid(getAuditUser());
		long count = opordMapper.saveOpordDetail(oporddetail);
		if(count != 0) {
			updateOpordHeaderTotalAmtAndGrandTotalAmt(oporddetail.getXordernum());
		}
		return count;
	}

	@Transactional
	@Override
	public long updateOpordDetail(Oporddetail oporddetail) {
		if(oporddetail == null) return 0;
		oporddetail.setZid(sessionManager.getBusinessId());
		oporddetail.setZuuserid(getAuditUser());
		long count = opordMapper.updateOpordDetail(oporddetail);
		if(count != 0) {
			updateOpordHeaderTotalAmtAndGrandTotalAmt(oporddetail.getXordernum());
		}
		return count;
	}

	@Transactional
	@Override
	public long deleteOpordDetail(Oporddetail oporddetail) {
		if(oporddetail == null) return 0;
		String xordernum = oporddetail.getXordernum();
		oporddetail.setZid(sessionManager.getBusinessId());
		long count = opordMapper.deleteOpordDetail(oporddetail);
		if(count != 0) {
			updateOpordHeaderTotalAmtAndGrandTotalAmt(xordernum);
		}
		return count;
	}

	@Transactional
	@Override
	public long batchDeleteOpordDetail(List<Oporddetail> oporddetails) {
		if(oporddetails == null || oporddetails.isEmpty()) return 0;
		int fcount = 0;
		for(Oporddetail d : oporddetails) {
			fcount += deleteOpordDetail(d);
		}
		return fcount;
	}

	@Override
	public Opordheader findOpordHeaderByXordernum(String xordernum) {
		if(StringUtils.isBlank(xordernum)) return null;
		return opordMapper.findOpordHeaderByXordernum(xordernum, sessionManager.getBusinessId());
	}

	@Override
	public Oporddetail findOporddetailByXordernumAndXrow(String xordernum, int xrow) {
		if(StringUtils.isBlank(xordernum) || xrow == 0) return null;
		return opordMapper.findOporddetailByXordernumAndXrow(xordernum, xrow, sessionManager.getBusinessId(), getBusinessId());
	}

	@Override
	public List<Oporddetail> findOporddetailByXordernum(String xordernum) {
		if(StringUtils.isBlank(xordernum)) return Collections.emptyList();
		return opordMapper.findOporddetailByXordernum(xordernum, sessionManager.getBusinessId(), getBusinessId());
	}

	@Override
	public List<Opordheader> getAllOpordheader() {
		return opordMapper.getAllOpordheader(sessionManager.getBusinessId());
	}

	@Override
	public Opordheader findOpordHeaderByXtypetrnAndXpornumAndXdateAndXcus(String xtypetrn, String xpornum, String xcus, Date xdate) {
		if(StringUtils.isBlank(xtypetrn) || StringUtils.isBlank(xpornum) || StringUtils.isBlank(xcus) || xdate == null) return null;
		return opordMapper.findOpordHeaderByXtypetrnAndXpornumAndXdateAndXcus(xtypetrn, xpornum, xcus, sdf.format(xdate), sessionManager.getBusinessId());
	}

	@Override
	public Opordheader findOpordHeaderByXtypetrnAndXtrnAndXdate(String xtypetrn, String xtrn, Date xdate) {
		if(StringUtils.isBlank(xtypetrn) || StringUtils.isBlank(xtrn) || xdate == null) return null;
		return opordMapper.findOpordHeaderByXtypetrnAndXtrnAndXdate(xtypetrn, xtrn, sdf.format(xdate), sessionManager.getBusinessId());
	}

	@Override
	public List<Opordheader> findAllOpordHeaderByXtypetrnAndXtrnAndXdateAndXstatus(String xtypetrn, String xtrn, String xstatus) {
		if(StringUtils.isBlank(xtypetrn) || StringUtils.isBlank(xtrn) || StringUtils.isBlank(xstatus)) return Collections.emptyList();
		return opordMapper.findAllOpordHeaderByXtypetrnAndXtrnAndXdateAndXstatus(xtypetrn, xtrn, xstatus, sessionManager.getBusinessId());
	}

	@Override
	public List<Opordheader> findAllOpordHeaderByXtypetrnAndXtrnAndXdate(String xtypetrn, String xtrn, Date xdate) {
		if(StringUtils.isBlank(xtypetrn) || StringUtils.isBlank(xtrn) || xdate == null) return Collections.emptyList();
		return opordMapper.findAllOpordHeaderByXtypetrnAndXtrnAndXdate(xtypetrn, xtrn, sdf.format(xdate), sessionManager.getBusinessId());
	}

	@Override
	public Oporddetail findOporddetailByXordernumAndXitem(String xordernum, String xitem) {
		if(StringUtils.isBlank(xordernum) || StringUtils.isBlank(xitem)) return null;
		return opordMapper.findOporddetailByXordernumAndXitem(xordernum, xitem, sessionManager.getBusinessId()).stream().findFirst().orElse(null);
	}

	@Override
	public List<Oporddetail> findAllOporddetailByXordernumAndXitem(String xordernum, String xitem) {
		if(StringUtils.isBlank(xordernum) || StringUtils.isBlank(xitem)) return null;
		return opordMapper.findOporddetailByXordernumAndXitem(xordernum, xitem, sessionManager.getBusinessId());
	}

	@Override
	public List<BranchesRequisitions> getSalesOrderMatrxi(Date xdate) {
		return opordMapper.getSalesOrderMatrxi(sdf.format(xdate), sessionManager.getBusinessId());
	}

	@Override
	public List<Opordheader> findAllOpordHeaderByXtypetrnAndXtrn(String xtypetrn, String xtrn) {
		if(StringUtils.isBlank(xtypetrn) || StringUtils.isBlank(xtrn)) return Collections.emptyList();
		return opordMapper.findAllOpordHeaderByXtypetrnAndXtrn(xtypetrn, xtrn, sessionManager.getBusinessId());
	}

	@Override
	public List<Opordheader> findAllOpenOpordHeaderByXtypetrnAndXtrn(String xtypetrn, String xtrn) {
		if(StringUtils.isBlank(xtypetrn) || StringUtils.isBlank(xtrn)) return Collections.emptyList();
		return opordMapper.findAllOpenOpordHeaderByXtypetrnAndXtrn(xtypetrn, xtrn, sessionManager.getBusinessId());
	}

	@Override
	public List<Opordheader> findAllProductionCompletedChalan(String xtypetrn, String xtrn) {
		if(StringUtils.isBlank(xtypetrn) || StringUtils.isBlank(xtrn)) return Collections.emptyList();
		return opordMapper.findAllProductionCompletedChalan(xtypetrn, xtrn, sessionManager.getBusinessId());
	}

	@Override
	public List<Opordheader> findAllSalesOrder(String xtypetrn, String xtrn, String xstatusord, Date date) {
		if(StringUtils.isBlank(xtypetrn) || StringUtils.isBlank(xtrn) || StringUtils.isBlank(xstatusord)) return Collections.emptyList();
		if(date == null) date = new Date();
		return opordMapper.findAllSalesOrder(xtypetrn, xtrn, xstatusord, sdf.format(date), sessionManager.getBusinessId());
	}

	@Override
	public List<Opordheader> findAllSalesOrderByChalan(String xtypetrn, String xtrn, String xchalanref) {
		if(StringUtils.isBlank(xtypetrn) || StringUtils.isBlank(xtrn) || StringUtils.isBlank(xchalanref)) return Collections.emptyList();
		return opordMapper.findAllSalesOrderByChalan(xtypetrn, xtrn, xchalanref, sessionManager.getBusinessId());
	}

	@Override
	public List<Opordheader> searchOpordheaderByXtypetrnAndXtrnAndXordernum(String xtypetrn, String xtrn, String xordernum, String xstatus) {
		if(StringUtils.isBlank(xtypetrn) || StringUtils.isBlank(xtrn) || StringUtils.isBlank(xordernum)) return Collections.emptyList();
		return opordMapper.searchOpordheaderByXtypetrnAndXtrnAndXordernum(xtypetrn, xtrn, xordernum.toUpperCase(), xstatus, sessionManager.getBusinessId());
	}
	
	@Override
	public List<Opordheader> searchXpornum(String xpornum){
		return opordMapper.searchXpornum(xpornum.toUpperCase(), sessionManager.getBusinessId());
	}

	@Override
	public List<Caitem> findAvailableRoomsByDate(Date xcheckindate) {
		SimpleDateFormat formater = new SimpleDateFormat("dd-MMM-yyyy");
		return opordMapper.findAvailableRoomsByDate(formater.format(xcheckindate), sessionManager.getBusinessId());
	}
	
	@Override
	public List<Caitem> findBookedRoomsByDate(Date xcheckindate) {
		SimpleDateFormat formater = new SimpleDateFormat("dd-MMM-yyyy");
		return opordMapper.findBookedRoomsByDate(formater.format(xcheckindate), sessionManager.getBusinessId());
	}
	
	@Override
	public List<Oporddetail> findBookedRoomsByXordernum(String xordernum) {
		return opordMapper.findBookedRoomsByXordernum(xordernum, sessionManager.getBusinessId());
	}
	
	@Override
	public List<Caitem> findAvailableHallsByDate(Date xfuncdate) {
		SimpleDateFormat formater = new SimpleDateFormat("dd-MMM-yyyy");
		return opordMapper.findAvailableHallsByDate(formater.format(xfuncdate), sessionManager.getBusinessId());
	}
	
	@Override
	public List<Caitem> findBookedHallsByXfuncdate(Date xfuncdate) {
		SimpleDateFormat formater = new SimpleDateFormat("dd-MMM-yyyy");
		return opordMapper.findBookedHallsByXfuncdate(formater.format(xfuncdate), sessionManager.getBusinessId());
	}
	
	@Override
	public List<Oporddetail> findBookedHallsByXordernum(String xordernum) {
		return opordMapper.findBookedHallsByXordernum(xordernum, sessionManager.getBusinessId());
	}

	@Transactional
	@Override
	public long updateOpordHeaderTotalAmtAndGrandTotalAmt(String xordernum) {
		if(StringUtils.isBlank(xordernum)) return 0;
		return opordMapper.updateOpordHeaderTotalAmtAndGrandTotalAmt(xordernum, sessionManager.getBusinessId());
	}

	@Transactional
	@Override
	public long saveBatchOpordDetail(List<Oporddetail> opordDetails) {
		if(opordDetails == null || opordDetails.isEmpty()) return 0;
		long totalCount = 0;
		for(Oporddetail detail : opordDetails) {
			long scount = saveOpordDetail(detail);
			if(scount != 0) updateOpordHeaderTotalAmtAndGrandTotalAmt(detail.getXordernum());
			totalCount += scount;
		}
		return totalCount;
	}

	@Transactional
	@Override
	public long archiveAllOporddetailByXordernum(String xordernum) {
		if(StringUtils.isBlank(xordernum)) return 0;
		return opordMapper.archiveAllOporddetailByXordernum(xordernum, sessionManager.getBusinessId());
	}

	@Override
	public List<Oporddetail> findAllSubitemDetail(String xordernum, int xparentrow, String xtype) {
		if(StringUtils.isBlank(xordernum) || xparentrow == 0 || StringUtils.isBlank(xtype)) return Collections.emptyList();
		return opordMapper.findAllSubitemDetail(xordernum, xparentrow, xtype, sessionManager.getBusinessId());
	}

	@Override
	@Transactional
	public long deleteSubItems(String xordernum, int xparentrow, String xtype) {
		if(StringUtils.isBlank(xordernum) || xparentrow == 0 || StringUtils.isBlank(xtype)) return 0;
		return opordMapper.deleteSubItems(xordernum, xparentrow, xtype, sessionManager.getBusinessId());
	}
	
	@Override
	public List<Opordheader> findAllOpenOpordHeaderByRawMaterials(String xtypetrn, String xtrn) {
		if(StringUtils.isBlank(xtypetrn) || StringUtils.isBlank(xtrn)) return Collections.emptyList();
		return opordMapper.findAllOpenOpordHeaderByRawMaterials(xtypetrn, xtrn, sessionManager.getBusinessId());
	}

	@Transactional
	@Override
	public Map<String, Object> createSalesOrderToInvoice(String xordernum, ResponseHelper response) throws ServiceException {
		Opordheader opordHeader = findOpordHeaderByXordernum(xordernum);
		if(opordHeader == null) throw new ServiceException("Can't find Sales order : " + xordernum);

		if("Open".equalsIgnoreCase(opordHeader.getXstatusord())) throw new ServiceException("Sales order not confirmed");

		// check Sales order has item details
		List<Oporddetail> opordDetailList = findOporddetailByXordernum(xordernum);
		if(opordDetailList == null || opordDetailList.isEmpty()) throw new ServiceException("This purchase order has no item");

		// check is there is any available item to make Invoice
		boolean itemavailable = false;
		for(Oporddetail p : opordDetailList) {
			if(p.getXqtydel() == null) p.setXqtydel(BigDecimal.ZERO);
			if(!p.getXqtydel().equals(p.getXqtyord())) itemavailable = true;
		}
		if(!itemavailable) throw new ServiceException("This Sales order has no item available to make Invoice");

		// Create Invoice header first
		Opdoheader opdoHeader = new Opdoheader();
		opdoHeader.setXordernum(xordernum);
		opdoHeader.setXtypetrn(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode());
		opdoHeader.setXtrn(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode());
		opdoHeader.setXdate(new Date());
		opdoHeader.setXstatusord("Open");
		opdoHeader.setXstatusjv("Open");
		opdoHeader.setXstatusar("Open");
		opdoHeader.setXtotamt(BigDecimal.ZERO);
		//opdoHeader.setXwh(StringUtils.isNotBlank(opordHeader.getXwh()) ? opordHeader.getXwh() : StringUtils.isNotBlank(sessionManager.getLoggedInUserDetails().getXwh()) ? sessionManager.getLoggedInUserDetails().getXwh() : "01");
		opdoHeader.setXtype("From SO");
		opdoHeader.setXpaymenttype("Other");
		opdoHeader.setXcus(opordHeader.getXcus());
		opdoHeader.setXhwh(opordHeader.getXhwh());
		opdoHeader.setXwh(opordHeader.getXwh());
		opdoHeader.setXref(opordHeader.getXref());
		opdoHeader.setXnote(opordHeader.getXnote());
		long count = opdoService.save(opdoHeader);
		if(count == 0) throw new ServiceException("Can't create Invoice for sales order : " + xordernum);

		// Create invoice details from purchase details
		for(Oporddetail p : opordDetailList) {
			if(p.getXqtydel() == null) p.setXqtydel(BigDecimal.ZERO); 
			if(p.getXqtyord() == null) p.setXqtyord(BigDecimal.ZERO); 

			Opdodetail detail = new Opdodetail();
			detail.setXdornum(opdoHeader.getXdornum());
			detail.setXitem(p.getXitem());
			detail.setXdorrow(p.getXrow());
			detail.setXunitsel(p.getXunitsel());
			detail.setXqtyord(p.getXqtyord().subtract(p.getXqtydel()));
			detail.setXrate(p.getXrate() != null ? p.getXrate() : BigDecimal.ZERO);
			detail.setXlineamt(detail.getXqtyord().multiply(detail.getXrate()));
			detail.setXcatitem(p.getXcatitem());
			detail.setXgitem(p.getXgitem());
			detail.setXqtydel(p.getXqtydel());
			detail.setXlineamt(p.getXlineamt());
			detail.setXlong(p.getXlong());
			detail.setXcfsel(p.getXcfsel());

			// if item has no qty, then it don't need to save
			if(BigDecimal.ZERO.equals(detail.getXqtyord())) continue;

			// Now save detail
			long dcount = opdoService.saveDetail(detail);
			if(dcount == 0) throw new ServiceException("Can't save invoice detail");

			p.setXqtydel(p.getXqtydel().add(detail.getXqtyord()));

			// now add total amount to header
			opdoHeader.setXtotamt(opdoHeader.getXtotamt().add(detail.getXlineamt()));
			opdoHeader.setXgrandtot((opdoHeader.getXtotamt()));
		}

		// now update sales order details with invoice qty
		for(Oporddetail p : opordDetailList) {
			long dcount = updateOpordDetail(p);
			if(dcount == 0) throw new ServiceException("Can't update sales detail");
		}

		// now update sales order header status
		Opordheader oh = findOpordHeaderByXordernum(opordHeader.getXordernum());
		oh.setXstatusord("Full Received");
		long phcount = updateOpordHeader(oh);
		if(phcount == 0) throw new ServiceException("Can't update sales order status");

		// now update invoice header with total amount
		long counth = opdoService.update(opdoHeader);
		if(counth == 0) throw new ServiceException("Can't update invoice with total amount");

		response.setSuccessStatusAndMessage("Invoice "+ opdoHeader.getXdornum()+" created successfully");
		//response.setRedirectUrl("/salesninvoice/salesandinvoice/invoicedetails/" + opdoHeader.getXdornum());
		response.setRedirectUrl("/salesninvoice/opord/" + opordHeader.getXordernum());
		return response.getResponse();
	}

	@Override
	public List<Oporddetail> searchSalesOrderAvailableItem(String xordernum, String hint) {
		if(StringUtils.isBlank(xordernum) || StringUtils.isBlank(hint)) return Collections.emptyList();
		return opordMapper.searchSalesOrderAvailableItem(xordernum, hint.toUpperCase(), sessionManager.getBusinessId());
	}

	@Transactional
	@Override
	public Map<String, Object> createSalesOrderToChalan(String[] salesorders) throws ServiceException {
		if(salesorders.length == 0) throw new ServiceException("No sales order selected to create production chalan");

		List<Opordheader> opordheaderList = new ArrayList<>();
		for(String xordernum : salesorders) {
			Opordheader oh = findOpordHeaderByXordernum(xordernum);
			if(oh == null) throw new ServiceException(xordernum + " not found in this system");
			if("Confirmed".equalsIgnoreCase(oh.getXstatusord())) throw new ServiceException(xordernum + " already confirmed");
			if(StringUtils.isNotBlank(oh.getXchalanref())) throw new ServiceException("Sales order already added to chalan : " + oh.getXchalanref() + " . Please reload this page again");
			opordheaderList.add(oh);
		}

		// Create a production chalan first
		Opordheader chalan = new Opordheader();
		chalan.setXtypetrn(TransactionCodeType.CHALAN_NUMBER.getCode());
		chalan.setXtrn(TransactionCodeType.CHALAN_NUMBER.getdefaultCode());
		chalan.setXdate(new Date());
		chalan.setXstatus("Open");
		long count = saveOpordHeader(chalan);
		if(count == 0) throw new ServiceException("Chalan not created");

		// Loop to each sales orders
		for(Opordheader oh : opordheaderList) {
			List<Oporddetail> details = findOporddetailByXordernum(oh.getXordernum());
			if(details == null || details.isEmpty()) throw new ServiceException("This " + oh.getXordernum() + " Sales Order has no item to add to production chalan");

			// create or update production chalan detail first
			for(Oporddetail pd : details) {
				// if not production item, then don't add it to production chalan
				Caitem caitem = caitemService.findByXitem(pd.getXitem());
				if(caitem == null || !caitem.isXproditem()) continue;

				// check chalan detail already exist using this item
				Oporddetail existChalanDetail = findOporddetailByXordernumAndXitem(chalan.getXordernum(), pd.getXitem());
				if(existChalanDetail != null) {  // update existing with qty
					existChalanDetail.setXqtyord(existChalanDetail.getXqtyord().add(pd.getXqtyord() == null ? BigDecimal.ZERO : pd.getXqtyord()));
					long countChalanDetail = updateOpordDetail(existChalanDetail);
					if(countChalanDetail == 0) throw new ServiceException("Can't update chalan detail");
				} else {  // create new detail
					Oporddetail chalanDetail = new Oporddetail();
					chalanDetail.setXordernum(chalan.getXordernum());
					chalanDetail.setXitem(pd.getXitem());
					chalanDetail.setXunit(pd.getXunit());
					chalanDetail.setXqtyord(pd.getXqtyord() == null ? BigDecimal.ZERO : pd.getXqtyord());
					chalanDetail.setXrate(pd.getXrate() == null ? BigDecimal.ZERO : pd.getXrate());
					chalanDetail.setXlineamt(chalanDetail.getXrate().multiply(chalanDetail.getXqtyord()));
					chalanDetail.setXdesc(caitem.getXdesc());
					chalanDetail.setXcatitem(caitem.getXcatitem());
					chalanDetail.setXgitem(caitem.getXgitem());
					long countChalanDetail = saveOpordDetail(chalanDetail);
					if(countChalanDetail == 0) throw new ServiceException("Can't create chalan detail");
				}
			}

			//now update sales order with chalan reference
			oh.setXchalanref(chalan.getXordernum());
			oh.setXstatusord("Confirmed");
			long countoh = updateOpordHeader(oh);
			if(countoh == 0) throw new ServiceException("Can't Update Sales Order");
		}

		// now create suggestion
		// delete suggestion table where xordernum
		productionSuggestionService.deleteSuggestion(chalan.getXordernum(), chalan.getXdate());

		// create suggestion
		productionSuggestionService.createSuggestion(chalan.getXordernum());

		// update production chalan suggestion created status
		chalan.setSuggestionCreated(true);
		long chalancount = updateOpordHeader(chalan);
		if(chalancount == 0) throw new ServiceException("Can't update chalan suggestion status");

		Map<String, Object> returnMap = new HashMap<>();
		returnMap.put("status", true);
		returnMap.put("chalan", chalan.getXordernum());
		return returnMap;
	}

}
