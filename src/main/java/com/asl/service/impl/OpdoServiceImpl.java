package com.asl.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.asl.mapper.OpdoMapper;
import com.asl.model.BranchesRequisitions;
import com.asl.model.ServiceException;
import com.asl.service.CaitemService;
import com.asl.service.OpdoService;
import com.asl.service.OpordService;

@Service
public class OpdoServiceImpl extends AbstractGenericService implements OpdoService {

	@Autowired private OpdoMapper opdoMapper;
	@Autowired private OpordService opordService;
	@Autowired private CaitemService caitemService;

	@Transactional
	@Override
	public long save(Opdoheader opdoHeader) {
		if (opdoHeader == null)
			return 0;
		opdoHeader.setZid(sessionManager.getBusinessId());
		opdoHeader.setZauserid(getAuditUser());
		opdoHeader.setCentralzid(getBusinessId());
		return opdoMapper.saveOpdoHeader(opdoHeader);
	}

	@Override
	@Transactional
	public long update(Opdoheader opdoHeader) {
		if (opdoHeader == null || StringUtils.isBlank(opdoHeader.getXdornum())) 
			return 0;
		if(StringUtils.isBlank(opdoHeader.getZid()))
			opdoHeader.setZid(sessionManager.getBusinessId());
		opdoHeader.setZuuserid(getAuditUser());
		return opdoMapper.updateOpdoHeader(opdoHeader);
	}

	@Transactional
	@Override
	public long delete(String xdornum) {
		if(StringUtils.isBlank(xdornum)) return 0;
		return opdoMapper.deleteOpdoHeader(xdornum, sessionManager.getBusinessId());
	}

	@Override
	public long saveDetail(Opdodetail opdoDetail) {
		if(opdoDetail == null || StringUtils.isBlank(opdoDetail.getXdornum())) return 0;
		opdoDetail.setZid(sessionManager.getBusinessId());
		opdoDetail.setZauserid(getAuditUser());
		long count = opdoMapper.saveOpdoDetail(opdoDetail);
		if(count != 0) { count = updateOpdoHeaderTotalAmtAndGrandTotalAmt(opdoDetail.getXdornum()); }
		return count;
	}

	@Override
	public long updateDetail(Opdodetail opdoDetail) {
		if(opdoDetail == null || StringUtils.isBlank(opdoDetail.getXdornum())) return 0;
		opdoDetail.setZid(sessionManager.getBusinessId());
		opdoDetail.setZuuserid(getAuditUser());
		long count = opdoMapper.updateOpdoDetail(opdoDetail);
		if(count != 0) { count = updateOpdoHeaderTotalAmtAndGrandTotalAmt(opdoDetail.getXdornum()); }
		return count;
	}

	@Override
	public long deleteDetail(Opdodetail opdoDetail) {
		if(opdoDetail == null) return 0;
		long count = opdoMapper.deleteOpdoDetail(opdoDetail);
		if(count != 0) { count = updateOpdoHeaderTotalAmtAndGrandTotalAmt(opdoDetail.getXdornum()); }
		return count;
	}

	@Override
	public Opdoheader findOpdoHeaderByXdornum(String xdornum) {
		if (StringUtils.isBlank(xdornum))
			return null;

		return opdoMapper.findOpdoHeaderByXdornum(xdornum, sessionManager.getBusinessId());
	}

	@Override
	public List<Opdoheader> getAllOpdoHeader() {
		return opdoMapper.getAllOpdoHeader(sessionManager.getBusinessId());
	}
	
	@Override
	public List<Opdoheader> getAllRandomOpdoHeader() {
		return opdoMapper.getAllRandomOpdoHeader(sessionManager.getBusinessId());
	}

	@Override
	public Opdodetail findOpdoDetailByXdornumAndXrow(String xdornum, int xrow) {
		if(StringUtils.isBlank(xdornum) || xrow == 0) return null;
		return opdoMapper.findOpdoDetailByXdornumAndXrow(xdornum, xrow, sessionManager.getBusinessId());
	}

	@Override
	public Opdodetail findOpdoDetailByXdornumAndXitem(String xdornum, String xitem) {
		if(StringUtils.isBlank(xdornum) || StringUtils.isBlank(xitem)) return null;
		return opdoMapper.findOpdoDetailByXdornumAndXitem(xdornum, xitem, sessionManager.getBusinessId());
	}

	@Override
	public List<Opdodetail> findOpdoDetailByXdornum(String xdornum) {
		if(StringUtils.isBlank(xdornum)) return Collections.emptyList();
		return opdoMapper.findOpdoDetailByXdornum(xdornum, sessionManager.getBusinessId(), getBusinessId());
	}

	@Override
	public List<Opdodetail> findAssignedOpdoDetailByChalan(String chalan) {
		if(StringUtils.isBlank(chalan)) return Collections.emptyList();

		List<Opdoheader> invoices = findAllInvoiceOrderByChalan(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode(),TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode(), chalan);
		if(invoices == null || invoices.isEmpty()) return Collections.emptyList();

		List<Opdodetail> finaldetails = new ArrayList<>();

		Map<String, Opdodetail> map = new HashMap<>();
		for(Opdoheader invoice : invoices) {
			List<Opdodetail> details = findOpdoDetailByXdornum(invoice.getXdornum());
			if(details == null || details.isEmpty()) continue;

			for(Opdodetail detail : details) {
				if(map.get(detail.getXitem()) != null) {
					Opdodetail d = map.get(detail.getXitem());
					d.setXqtyord(d.getXqtyord().add(detail.getXqtyord()));
					map.put(detail.getXitem(), d);
				} else {
					map.put(detail.getXitem(), detail);
				}
			}
		}

		for(Map.Entry<String, Opdodetail> m : map.entrySet()) {
			finaldetails.add(m.getValue());
		}

		finaldetails.sort(Comparator.comparing(Opdodetail::getXitem));
		return finaldetails;
	}

	@Override
	public long updateOpdoHeaderTotalAmt(String xdornum) {
		if(StringUtils.isBlank(xdornum)) return 0;
		return opdoMapper.updateOpdoHeaderTotalAmt(xdornum, sessionManager.getBusinessId());
	}
	

	@Transactional
	@Override
	public long updateOpdoHeaderTotalAmtAndGrandTotalAmt(String xdornum) {
		if(StringUtils.isBlank(xdornum)) return 0;
		return opdoMapper.updateOpdoHeaderTotalAmtAndGrandTotalAmt(xdornum, sessionManager.getBusinessId());
	}
	
	@Override
	public long updateOpdoHeaderGrandTotalAmt(String xdornum) {
		if(StringUtils.isBlank(xdornum)) return 0;
		return opdoMapper.updateOpdoHeaderGrandTotalAmt(xdornum, sessionManager.getBusinessId());
	}

	@Override
	public void procConfirmDO(String xdornum, String p_seq) {
		opdoMapper.procConfirmDO(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xdornum, p_seq);
	}

	@Override
	public void procIssuePricing(String xdornum, String xwh, String p_screen, String p_seq) {
		opdoMapper.procIssuePricing(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xdornum, xwh,p_screen, p_seq);
	}

	@Override
	public void procTransferOPtoAR(String xdornum, String p_screen, String p_seq) {
		opdoMapper.procTransferOPtoAR(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xdornum, p_screen, p_seq);
	}
	
	@Override
	public void procTransferOPtoGL(String xdornum, String p_screen, String p_seq) {
		opdoMapper.procTransferOPtoGL(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xdornum, p_screen, p_seq);
	}
	
	@Override
	public void procrequpdate(String xdornum) {
		opdoMapper.procrequpdate(sessionManager.getBusinessId(), xdornum);
	}

	@Override
	public List<Opdoheader> findAllInvoiceOrder(String xtypetrn, String xtrn, String xstatus, Date date) {
		if(StringUtils.isBlank(xtypetrn) || StringUtils.isBlank(xtrn) || StringUtils.isBlank(xstatus)) return Collections.emptyList();
		if(date == null) date = new Date();
		return opdoMapper.findAllInvoiceOrder(xtypetrn, xtrn, xstatus, sdf.format(date), sessionManager.getBusinessId());
	}

	@Override
	public List<Opdoheader> findAllInvoiceOrderByChalan(String xtypetrn, String xtrn, String xchalanref) {
		if(StringUtils.isBlank(xtypetrn) || StringUtils.isBlank(xtrn) || StringUtils.isBlank(xchalanref)) return Collections.emptyList();
		return opdoMapper.findAllInvoiceOrderByChalan(xtypetrn, xtrn, xchalanref, sessionManager.getBusinessId());
	}

	@Override
	public List<Opdoheader> findAllOpdoHeaderByXtypetrnAndXtrn(String xtypetrn, String xtrn) {
		if(StringUtils.isBlank(xtypetrn) || StringUtils.isBlank(xtrn)) return Collections.emptyList();
		return opdoMapper.findAllOpdoHeaderByXtypetrnAndXtrn(xtypetrn, xtrn, sessionManager.getBusinessId());
	}

	@Override
	public List<Opdoheader> searchOpdoHeader(String xtypetrn, String xdornum) {
		return opdoMapper.searchOpdoHeader(xtypetrn, xdornum.toUpperCase(), sessionManager.getBusinessId());
	}
	
	@Override
	public List<Opdoheader> findOpdoXdornum(String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		return opdoMapper.findOpdoXdornum(hint.toUpperCase(), sessionManager.getBusinessId());
	}
	
	
	@Override
	public List<Opdoheader> searchOpdoHeader(String xtypetrn, String xstatusord, String xdornum) {
		return opdoMapper.searchOpdoHeaderWithSatus(xtypetrn, xdornum.toUpperCase(), xstatusord, sessionManager.getBusinessId());
	}

	@Transactional
	@Override
	public long createSalesFromChalan(String xordernum) throws ServiceException {
		if(StringUtils.isBlank(xordernum)) return 0;

		Opordheader productionChalan = opordService.findOpordHeaderByXordernum(xordernum);
		if(productionChalan == null) return 0;

		List<Opordheader> salesOrdersOfProductionChalan = opordService.findAllSalesOrderByChalan(TransactionCodeType.SALES_ORDER.getCode(), TransactionCodeType.SALES_ORDER.getdefaultCode(), xordernum);
		if(salesOrdersOfProductionChalan == null || salesOrdersOfProductionChalan.isEmpty()) return 0;

		// check delivery chalan exist (it referes that already delivery chalan created)
		Opdoheader existDeliveryChalan = opdoMapper.findPoordHeaderByXordernum(productionChalan.getXordernum(), sessionManager.getBusinessId());
		if(existDeliveryChalan != null) return 0;

		// create delivery chalan first
		Opdoheader deliveryChalan = new Opdoheader();
		deliveryChalan.setXtypetrn(TransactionCodeType.CHALAN_NUMBER.getCode());
		deliveryChalan.setXtrn(TransactionCodeType.CHALAN_NUMBER.getdefaultCode());
		deliveryChalan.setXdate(new Date());
		deliveryChalan.setXstatusord("Open");
		deliveryChalan.setXstatusar("Open");
		deliveryChalan.setXstatusjv("Open");
		deliveryChalan.setZid(sessionManager.getBusinessId());
		deliveryChalan.setXordernum(productionChalan.getXordernum());  // production chalan reference ]
		deliveryChalan.setXreqnum(productionChalan.getXreqnum());
		long deliveryChalanCount = opdoMapper.saveOpdoHeader(deliveryChalan);
		if(deliveryChalanCount == 0) return 0;

		// Create sales from sales order of production chalan first
		int salesSavedCount = 0;
		for(Opordheader salesOrder : salesOrdersOfProductionChalan) {
			Opdoheader sales = new Opdoheader();
			sales.setXtypetrn(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode());
			sales.setXtrn(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode());
			sales.setXdate(new Date());
			sales.setXstatusord("Open");
			sales.setXstatusjv("Open");
			sales.setXstatusar("Open");
			sales.setXdocnum(deliveryChalan.getXdornum());   // assign this sales to delivery chalan
			sales.setXchalancreated(true);
			sales.setXvatait("No Vat");
			sales.setXtotamt(BigDecimal.ZERO);
			sales.setXait(BigDecimal.ZERO);
			sales.setXvatamt(BigDecimal.ZERO);
			sales.setXdiscamt(BigDecimal.ZERO);
			sales.setXgrandtot(BigDecimal.ZERO);
			sales.setXwh(StringUtils.isNotBlank(salesOrder.getXwh()) ? salesOrder.getXwh() : StringUtils.isNotBlank(sessionManager.getLoggedInUserDetails().getXwh()) ? sessionManager.getLoggedInUserDetails().getXwh()  : "01");  // set for central store invoice
			sales.setXpaymenttype("Other");
			sales.setXtype("SO");
			sales.setXcus(salesOrder.getXcus());  // set branch reference for this sales, customer
			sales.setXordernum(salesOrder.getXordernum());  // set sales order reference
			sales.setRequisitionnumber(salesOrder.getXpornum());  // set branch requisition number reference
			sales.setXreqnum(salesOrder.getXreqnum());
			sales.setZid(sessionManager.getBusinessId());

			long count = opdoMapper.saveOpdoHeader(sales);
			if(count == 0) continue;
			salesSavedCount++;

			// find recently saved sales header
//			Opdoheader savedSales = opdoMapper.findPoordHeaderByXordernumAndRequisitionnumber(salesOrder.getXordernum(), salesOrder.getXpornum(), sessionManager.getBusinessId());
//			if(savedSales == null) continue;

			salesOrder.setXdornum(sales.getXdornum()); // now set sales reference back to sales order
			opordService.updateOpordHeader(salesOrder); // updated sales order with sales reference

			// now prepare item details from sales order to sales
			List<Oporddetail> salesOrdeItems = opordService.findOporddetailByXordernum(salesOrder.getXordernum());
			if(salesOrdeItems == null || salesOrdeItems.isEmpty()) continue;
			procrequpdate(salesOrder.getXdornum());

			for(Oporddetail salesOrderItem : salesOrdeItems) {
				if(salesOrderItem.getXqtydel() == null) salesOrderItem.setXqtydel(BigDecimal.ZERO);
				if(salesOrderItem.getXqtyord() == null) salesOrderItem.setXqtyord(BigDecimal.ZERO);

				Caitem caitem = caitemService.findByXitem(salesOrderItem.getXitem());
				if(caitem == null) continue;

				Opdodetail salesItem = new Opdodetail();
				salesItem.setXdornum(sales.getXdornum());  // set sales reference for its items
				salesItem.setXitem(salesOrderItem.getXitem());
				salesItem.setXqtyord(salesOrderItem.getXqtyord() != null ? salesOrderItem.getXqtyord() : BigDecimal.ZERO);
				salesItem.setXunitsel(salesOrderItem.getXunit());
				salesItem.setXrate(salesOrderItem.getXrate() != null ? salesOrderItem.getXrate() : BigDecimal.ZERO);
				salesItem.setXlineamt(salesItem.getXqtyord().multiply(salesItem.getXrate()));
				salesItem.setXcatitem(salesOrderItem.getXcatitem());
				salesItem.setXgitem(salesOrderItem.getXgitem());
				salesItem.setXdorrow(salesOrderItem.getXrow());
				salesItem.setXopreqrow(salesOrderItem.getXdocrow());
				salesItem.setZid(sessionManager.getBusinessId());
				salesItem.setXqtyord(salesOrderItem.getXqtydel());

				// if item has no qty, then it don't need to save
				if(BigDecimal.ZERO.equals(salesItem.getXqtyord())) continue;

				long salesItemCount = saveDetail(salesItem);
				if(salesItemCount == 0) throw new ServiceException("Can't save invoice detail");

				salesOrderItem.setXqtydel(salesOrderItem.getXqtydel().add(salesItem.getXqtyord()));
				
				if(sales.getXtotamt() == null) sales.setXtotamt(BigDecimal.ZERO);
				sales.setXtotamt(sales.getXtotamt().add(salesItem.getXlineamt() == null ? BigDecimal.ZERO : salesItem.getXlineamt()));
			}

			// now update sales order details with invoice qty
			for(Oporddetail salesOrderItem : salesOrdeItems) {
				long dcount = opordService.updateOpordDetail(salesOrderItem);
				if(dcount == 0) throw new ServiceException("Can't update sales detail");
			}

			// Now update sales order header status
			salesOrder.setXstatusord("Full Received");
			long phcount = opordService.updateOpordHeader(salesOrder);
			if(phcount == 0) throw new ServiceException("Can't update purchase order status");

			// now update sales total amount
			long countsales = update(sales);
			if(countsales == 0) throw new ServiceException("Can't update sales total amount");
		}
		
		if(salesSavedCount == salesOrdersOfProductionChalan.size()) {
			productionChalan.setInvoicecreated(true);
			productionChalan.setXdornum(deliveryChalan.getXdornum());
			opordService.updateOpordHeader(productionChalan);
		}
			
		
		return salesSavedCount == salesOrdersOfProductionChalan.size() ? 1 : 0;
	}

	@Override
	public List<BranchesRequisitions> getSalesInvoiceMatrxi(Date xdate) {
		if(xdate == null) return Collections.emptyList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return opdoMapper.getSalesInvoiceMatrxi(sdf.format(xdate), sessionManager.getBusinessId());
	}

	@Override
	public List<Opdoheader> findOpdoheaderByXordernum(String xordernum) {
		if(StringUtils.isBlank(xordernum)) return null;
		return opdoMapper.findOpdoheaderByXordernum(xordernum, sessionManager.getBusinessId());
	}

	@Override
	public List<Opdoheader> getAllDirectOpdoHeader() {
		return opdoMapper.getAllDirectOpdoHeader(sessionManager.getBusinessId());
	}

	@Override
	public List<Opdoheader> findAllDirectOpdoHeaderByXtypetrnAndXtrn(String xtypetrn, String xtrn) {
		if(StringUtils.isBlank(xtypetrn) || StringUtils.isBlank(xtrn)) return Collections.emptyList();
		return opdoMapper.findAllDirectOpdoHeaderByXtypetrnAndXtrn(xtypetrn, xtrn, sessionManager.getBusinessId());
	}

	@Transactional
	@Override
	public boolean saveInvoiceDetail(Opdodetail opdoDetail) throws ServiceException {
		if (opdoDetail.getXaitamt() == null) opdoDetail.setXaitamt(BigDecimal.ZERO);
		if (opdoDetail == null || StringUtils.isBlank(opdoDetail.getXdornum())) throw new ServiceException("Invoice header reference not found");
		if (StringUtils.isBlank(opdoDetail.getXitem())) throw new ServiceException("Item selection required");
		if (opdoDetail.getXqtyord() == null || BigDecimal.ZERO.equals(opdoDetail.getXqtyord()) || opdoDetail.getXqtyord().compareTo(BigDecimal.ZERO) == -1) throw new ServiceException("Quantity should be greater then 0");

		Caitem caitem = caitemService.findByXitem(opdoDetail.getXitem());
		if (caitem == null) throw new ServiceException("Item not found in this system");

		if (caitem.getXvatrate() == null) caitem.setXvatrate(BigDecimal.ZERO);

		// Line amount
		opdoDetail.setXlineamt(opdoDetail.getXqtyord().multiply(opdoDetail.getXrate().setScale(2, RoundingMode.DOWN)));

		// vat amount
		BigDecimal vat = null;
		if("Bonus".equalsIgnoreCase(opdoDetail.getXcomtype())) {
			vat = (opdoDetail.getXqtyord().multiply(caitem.getXrate()).multiply(caitem.getXvatrate())).divide(BigDecimal.valueOf(100));
		} else {
			vat = (opdoDetail.getXlineamt().multiply(caitem.getXvatrate())).divide(BigDecimal.valueOf(100));
		}
		opdoDetail.setXvatamt(vat);
		opdoDetail.setXvatrate(caitem.getXvatrate());

		if(opdoDetail.getXrow() == 0) {
			long count = saveDetail(opdoDetail);
			if(count == 0) throw new ServiceException("Can't save invoice detail");
		} else {
			long count = updateDetail(opdoDetail);
			if(count == 0) throw new ServiceException("Can't update invoice detail");
		}

		long hcount = updateOpdoHeaderTotalAmtAndGrandTotalAmt(opdoDetail.getXdornum());
		if(hcount == 0) throw new ServiceException("Can't update header info");

		return true;
	}

	@Transactional
	@Override
	public boolean deleteInvoiceDetail(String xdornum, int xrow) throws ServiceException {
		if(StringUtils.isBlank(xdornum) || xrow == 0) return false;

		Opdodetail pd = findOpdoDetailByXdornumAndXrow(xdornum, xrow);
		if (pd == null) throw new ServiceException("Detail item can't found to do delete");

		long count = deleteDetail(pd);
		if (count == 0) throw new ServiceException("Can't delete invoice detail");

		long counth = updateOpdoHeaderTotalAmtAndGrandTotalAmt(xdornum);
		if(counth == 0) throw new ServiceException("Can't update invoice header");

		return true;
	}

	

}
