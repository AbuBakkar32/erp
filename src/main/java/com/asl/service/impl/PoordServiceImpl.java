package com.asl.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Cacus;
import com.asl.entity.Caitem;
import com.asl.entity.Oporddetail;
import com.asl.entity.Opordheader;
import com.asl.entity.Opreqdetail;
import com.asl.entity.Opreqheader;
import com.asl.entity.PogrnDetail;
import com.asl.entity.PogrnHeader;
import com.asl.entity.PoordDetail;
import com.asl.entity.PoordHeader;
import com.asl.entity.Poterms;
import com.asl.entity.Termsdef;
import com.asl.entity.Termstrn;
import com.asl.enums.Modules;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.mapper.PogrnMapper;
import com.asl.mapper.PoordMapper;
import com.asl.model.ResponseHelper;
import com.asl.model.ServiceException;
import com.asl.model.report.RM0301;
import com.asl.service.CacusService;
import com.asl.service.CaitemService;
import com.asl.service.OpordService;
import com.asl.service.OpreqService;
import com.asl.service.PoordService;
import com.asl.service.XtrnService;

@Service
public class PoordServiceImpl extends AbstractGenericService implements PoordService {

	@Autowired private PoordMapper poordMapper;
	@Autowired private PogrnMapper pogrnMapper;
	@Autowired private OpordService opordService;
	@Autowired private CaitemService caitemService;
	@Autowired private CacusService cacusService;
	@Autowired private OpreqService opreqService;
	@Autowired private XtrnService xtrnService;

	@Override
	@Transactional
	public long save(PoordHeader poordHeader) {
		if (poordHeader == null || StringUtils.isBlank(poordHeader.getXtrn())) return 0;
		poordHeader.setZid(sessionManager.getBusinessId());
		poordHeader.setZauserid(getAuditUser());
		return poordMapper.savePoordHeader(poordHeader);
	}

	@Override
	@Transactional
	public long update(PoordHeader poordHeader) {
		if (poordHeader == null || StringUtils.isBlank(poordHeader.getXpornum())) return 0;
		if(StringUtils.isBlank(poordHeader.getZid())) poordHeader.setZid(sessionManager.getBusinessId());
		poordHeader.setZuuserid(getAuditUser());
		return poordMapper.updatePoordHeader(poordHeader);
	}

	@Override
	public List<PoordHeader> getAllPoordHeaders() {
		return poordMapper.getAllPoordHeader(sessionManager.getBusinessId());
	}

	@Override
	public PoordHeader findPoordHeaderByXpornum(String xpornum) {
		if (StringUtils.isBlank(xpornum)) return null;
		return poordMapper.findPoordHeaderByXpornum(xpornum, sessionManager.getBusinessId());
	}

	@Override
	public PoordHeader findBranchPoordHeaderByXpornumForCentral(String xpornum, String branchzid) {
		if (StringUtils.isBlank(xpornum)) return null;
		return poordMapper.findBranchPoordHeaderByXpornumForCentral(xpornum, branchzid);
	}

	@Override
	@Transactional
	public long saveDetail(PoordDetail poordDetail) {
		if(poordDetail == null || StringUtils.isBlank(poordDetail.getXpornum())) return 0;
		poordDetail.setZid(sessionManager.getBusinessId());
		poordDetail.setZauserid(getAuditUser());
		long count = poordMapper.savePoordDetail(poordDetail);
		if(count != 0) {
			count = updatePoordHeaderTotalAmt(poordDetail);
		}
		return count;
	}

	@Override
	@Transactional
	public long saveDetail(List<PoordDetail> poordDetails) throws ServiceException {
		if(poordDetails == null || poordDetails.isEmpty()) return 0;
		long f_count = 0;
		for(PoordDetail pd : poordDetails) {
			if(StringUtils.isBlank(pd.getXpornum())) throw new ServiceException("Requesition reference empty");
			pd.setZid(sessionManager.getBusinessId());
			pd.setZauserid(getAuditUser());
			f_count += poordMapper.savePoordDetailWithRow(pd);
		}
		return f_count;
	}

	@Override
	@Transactional
	public long updatePoordHeaderTotalAmt(PoordDetail poordDetail) {
		if(poordDetail == null) return 0;
		return poordMapper.updatePoordHeaderTotalAmt(poordDetail);
	}

	@Override
	public long updateDetail(PoordDetail poordDetail) {
		if(poordDetail == null || StringUtils.isBlank(poordDetail.getXpornum())) return 0;
		poordDetail.setZid(sessionManager.getBusinessId());
		poordDetail.setZuuserid(getAuditUser());
		long count = poordMapper.updatePoordDetail(poordDetail);
		if(count != 0) {
			count = updatePoordHeaderTotalAmt(poordDetail);
		}
		return count;
	}

	@Override
	@Transactional
	public long deleteDetail(PoordDetail poordDetail) {
		if(poordDetail == null) return 0;
		long count = poordMapper.deletePoordDetail(poordDetail);
		if(count != 0) {
			count = updatePoordHeaderTotalAmt(poordDetail);
		}
		return count;
	}

	@Override
	@Transactional
	public long deleteDetailByXpornum(String xpornum) {
		if(StringUtils.isBlank(xpornum)) return 0;
		return poordMapper.deleteDetailByXpornum(xpornum, sessionManager.getBusinessId());
	}

	@Override
	public long archiveAllPoordDetailByXpornum(String xpornum) {
		if(StringUtils.isBlank(xpornum)) return 0;
		return poordMapper.archiveAllPoordDetailByXpornum(xpornum, sessionManager.getBusinessId());
	}

	@Override
	public long countOfRequisitionDetailsByXpornum(String xpornum) {
		if(StringUtils.isBlank(xpornum)) return 0;
		return poordMapper.countOfRequisitionDetailsByXpornum(xpornum, sessionManager.getBusinessId());
	}

	@Override
	public PoordDetail findPoorddetailByXpornumAndXrow(String xpornum, int xrow) {
		if(StringUtils.isBlank(xpornum) || xrow == 0) return null;
		return poordMapper.findPoorddetailByXpornumAndXrow(xpornum, xrow, sessionManager.getBusinessId());
	}

	@Override
	public List<PoordDetail> findPoorddetailByXpornum(String xpornum) {
		if(StringUtils.isBlank(xpornum)) return Collections.emptyList();

		String centralZid = null;
		if(Boolean.TRUE.equals(sessionManager.getZbusiness().getCentral())) {
			centralZid = sessionManager.getBusinessId();
		} else {
			centralZid = sessionManager.getZbusiness().getCentralzid();
		}

		return poordMapper.findPoorddetailByXpornum(xpornum, sessionManager.getBusinessId(), centralZid);
	}

	@Override
	public List<PoordDetail> findPoordDetailsByXpornumAndBranchZid(String xpornum, String branchzid) {
		if(StringUtils.isBlank(xpornum) || StringUtils.isBlank(branchzid)) return Collections.emptyList();
		return poordMapper.findPoordDetailsByXpornumAndBranchZid(xpornum, branchzid);
	}

	@Override
	public List<PoordHeader> getPoordHeadersByXtypetrn(String xtypetrn) {
		if(StringUtils.isBlank(xtypetrn)) return Collections.emptyList();
		return poordMapper.getPoordHeadersByXtypetrn(xtypetrn, sessionManager.getBusinessId());
	}

	@Override
	public PoordDetail findPoorddetailByXpornumAndXitem(String xpornum, String xitem) {
		if(StringUtils.isBlank(xpornum) || StringUtils.isBlank(xitem)) return null;
		return poordMapper.findPoorddetailByXpornumAndXitem(xpornum, xitem, sessionManager.getBusinessId());
	}

	@Override
	public Cacus findBranchCustomerByRequsitionNumber(String xpornum, String branchid) {
		if(StringUtils.isBlank(xpornum)) return null;
		return poordMapper.findBranchCustomerByRequsitionNumber(xpornum, branchid, sessionManager.getBusinessId());
	}

	
	@Override
	public List<PoordHeader> searchXpornum(String xpornum){
		return poordMapper.searchXpornum(xpornum.toUpperCase(), sessionManager.getBusinessId());
	}

	@Override
	public List<PoordHeader> findPoordHeaderByXporeqnum(String xporeqnum) {
		if(StringUtils.isBlank(xporeqnum)) return null;
		return poordMapper.findPoordHeaderByXporeqnum(xporeqnum,sessionManager.getBusinessId());
	}

	@Override
	public Cacus findBranchCustomerByRequsitionNumber(String xpornum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RM0301> getRM0301(String fdate, String tdate, String xcus, String xstatuspor, String xitem) {
		if(StringUtils.isBlank(fdate) || StringUtils.isBlank(tdate)) return Collections.emptyList();
		return poordMapper.getRM0301(fdate, tdate, xcus, xstatuspor, xitem, sessionManager.getBusinessId());
	}

	@Override
	@Transactional
	public long deletePoordheaderByXpornum(String xpornum) {
		if(StringUtils.isBlank(xpornum)) return 0;
		return poordMapper.deletePoordheaderByXpornum(xpornum, sessionManager.getBusinessId());
	}

	@Transactional
	@Override
	public Map<String, Object> createPurchaseOrderToGRN(ResponseHelper responseHelper, String xpornum) throws ServiceException {
		PoordHeader poordHeader = findPoordHeaderByXpornum(xpornum);
		if(poordHeader == null) {
			responseHelper.setErrorStatusAndMessage("Can't find purchase order : " + xpornum);
			return responseHelper.getResponse();
		}
		if("Open".equalsIgnoreCase(poordHeader.getXstatuspor())) {
			responseHelper.setErrorStatusAndMessage("Purchase order not confirmed");
			return responseHelper.getResponse();
		}

		// check purchase order has item details
		List<PoordDetail> poordDetailList = findPoorddetailByXpornum(xpornum);
		if(poordDetailList == null || poordDetailList.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("This purchase order has no item");
			return responseHelper.getResponse();
		}
		// check is there is any available item to make GRN
		boolean itemavailable = false;
		for(PoordDetail p : poordDetailList) {
			if(p.getXqtygrn() == null) p.setXqtygrn(BigDecimal.ZERO);
			if(!p.getXqtygrn().equals(p.getXqtypur())) itemavailable = true;
		}
		if(!itemavailable) {
			responseHelper.setErrorStatusAndMessage("This purchase order has no item available to make GRN");
			return responseHelper.getResponse();
		}

		// Create a GRN header first
		PogrnHeader pogrnHeader = new PogrnHeader();
		pogrnHeader.setXpornum(xpornum);
		pogrnHeader.setXtypetrn(TransactionCodeType.GRN_NUMBER.getCode());
		pogrnHeader.setXtrn(TransactionCodeType.GRN_NUMBER.getdefaultCode());
		pogrnHeader.setXdate(new Date());
		pogrnHeader.setXstatusgrn("Open");
		pogrnHeader.setXtotamt(BigDecimal.ZERO);
		pogrnHeader.setXwh(StringUtils.isNotBlank(poordHeader.getXwh()) ? poordHeader.getXwh() : StringUtils.isNotBlank(sessionManager.getLoggedInUserDetails().getXwh()) ? sessionManager.getLoggedInUserDetails().getXwh() : "01");
		pogrnHeader.setXcus(poordHeader.getXcus());
		pogrnHeader.setZid(sessionManager.getBusinessId());
		pogrnHeader.setZauserid(getAuditUser());
		pogrnHeader.setXstatusap("Open");
		pogrnHeader.setXstatusjv("Open");
		pogrnHeader.setXdategl(pogrnHeader.getXdate());
		pogrnHeader.setXtype("Other");
		pogrnHeader.setXnote(poordHeader.getXnote());

		long count = pogrnMapper.savePogrnHeader(pogrnHeader);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't create GRN for purchase order : " + xpornum);
			return responseHelper.getResponse();
		}

		// Create grn details from purchase details
		for(int i = 0; i < poordDetailList.size(); i++) {
			PoordDetail poorddetail = poordDetailList.get(i);
			if(poorddetail.getXqtygrn() == null) poorddetail.setXqtygrn(BigDecimal.ZERO);
			if(poorddetail.getXqtypur() == null) poorddetail.setXqtypur(BigDecimal.ZERO);

			PogrnDetail detail = new PogrnDetail();
			detail.setXgrnnum(pogrnHeader.getXgrnnum());
			detail.setXitem(poorddetail.getXitem());
			detail.setXdocrow(poorddetail.getXrow());
			detail.setXqtygrn(poorddetail.getXqtypur().subtract(poorddetail.getXqtygrn()));
			detail.setXrate(poorddetail.getXrate());
			detail.setXunitpur(poorddetail.getXunitpur());
			detail.setXlineamt(poorddetail.getXqtypur().multiply(poorddetail.getXrate()));
			detail.setZid(sessionManager.getBusinessId());
			detail.setZauserid(getAuditUser());
			
			// if item has no qty, then it don't need to save
			if(BigDecimal.ZERO.equals(detail.getXqtygrn())) continue;

			long dcount = pogrnMapper.savePogrnDetail(detail);
			if(dcount == 0) throw new ServiceException("Can't save detail");

			poorddetail.setXqtygrn(poorddetail.getXqtygrn().add(detail.getXqtygrn()));
			
			// update grn total amt
			pogrnHeader.setXtotamt(pogrnHeader.getXtotamt().add(detail.getXlineamt()));
		}

		// now update poorddetails with grn qty
		for(int i = 0; i < poordDetailList.size(); i++) {
			PoordDetail poorddetail = poordDetailList.get(i);
			long dcount = updateDetail(poorddetail);
			if(dcount == 0) throw new ServiceException("Can't update purchase detail");
		}

		// now update pogrnheader with total amount
		long countupdate = pogrnMapper.updatePogrnHeader(pogrnHeader);
		if(countupdate == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update GRN total amount for purchase order : " + xpornum);
			return responseHelper.getResponse();
		}

		

		// now update poordheader status
		poordHeader.setXstatuspor("Full Received");
		long phcount = update(poordHeader);
		if(phcount == 0) throw new ServiceException("Can't update purchase order status");

		responseHelper.setSuccessStatusAndMessage("GRN created successfully");
		responseHelper.setRedirectUrl("/procurements/poord/" + poordHeader.getXpornum());
		return responseHelper.getResponse();
	}
	
	@Transactional
	@Override
	public Map<String, Object> createAndConfirmGRN(ResponseHelper responseHelper, String xpornum) throws ServiceException {
		PoordHeader poordHeader = findPoordHeaderByXpornum(xpornum);
		
		if("Open".equalsIgnoreCase(poordHeader.getXstatuspor())) {
			responseHelper.setErrorStatusAndMessage("Purchase order not confirmed");
			return responseHelper.getResponse();
		}
		

		// check purchase order has item details
		List<PoordDetail> poordDetailList = findPoorddetailByXpornum(xpornum);
		if(poordDetailList == null || poordDetailList.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("This purchase order has no item");
			return responseHelper.getResponse();
		}
		// check is there is any available item to make GRN
		boolean itemavailable = false;
		for(PoordDetail p : poordDetailList) {
			if(p.getXqtygrn() == null) p.setXqtygrn(BigDecimal.ZERO);
			if(!p.getXqtygrn().equals(p.getXqtypur())) itemavailable = true;
		}
		if(!itemavailable) {
			responseHelper.setErrorStatusAndMessage("This purchase order has no item available to make GRN");
			return responseHelper.getResponse();
		}

		// Create a GRN header first
		PogrnHeader pogrnHeader = new PogrnHeader();
		pogrnHeader.setXpornum(xpornum);
		pogrnHeader.setXtypetrn(TransactionCodeType.GRN_NUMBER.getCode());
		pogrnHeader.setXtrn(TransactionCodeType.GRN_NUMBER.getdefaultCode());
		pogrnHeader.setXdate(new Date());
		pogrnHeader.setXstatusgrn("Open");
		pogrnHeader.setXtotamt(BigDecimal.ZERO);
		pogrnHeader.setXwh(StringUtils.isNotBlank(poordHeader.getXwh()) ? poordHeader.getXwh() : StringUtils.isNotBlank(sessionManager.getLoggedInUserDetails().getXwh()) ? sessionManager.getLoggedInUserDetails().getXwh() : "01");
		pogrnHeader.setXcus(poordHeader.getXcus());
		pogrnHeader.setZid(sessionManager.getBusinessId());
		pogrnHeader.setZauserid(getAuditUser());
		pogrnHeader.setXstatusap("Open");
		pogrnHeader.setXstatusjv("Open");
		pogrnHeader.setXdategl(pogrnHeader.getXdate());
		pogrnHeader.setXtype("Other");
		pogrnHeader.setXnote(poordHeader.getXnote());
		pogrnHeader.setXinvnum(pogrnHeader.getXinvnum());

		long count = pogrnMapper.savePogrnHeader(pogrnHeader);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't create GRN for purchase order : " + xpornum);
			return responseHelper.getResponse();
		}

		// Create grn details from purchase details
		for(int i = 0; i < poordDetailList.size(); i++) {
			PoordDetail poorddetail = poordDetailList.get(i);
			if(poorddetail.getXqtygrn() == null) poorddetail.setXqtygrn(BigDecimal.ZERO);
			if(poorddetail.getXqtypur() == null) poorddetail.setXqtypur(BigDecimal.ZERO);

			PogrnDetail detail = new PogrnDetail();
			detail.setXgrnnum(pogrnHeader.getXgrnnum());
			detail.setXitem(poorddetail.getXitem());
			detail.setXdocrow(poorddetail.getXrow());
			detail.setXqtygrn(poorddetail.getXqtypur().subtract(poorddetail.getXqtygrn()));
			detail.setXrate(poorddetail.getXrate());
			detail.setXunitpur(poorddetail.getXunitpur());
			detail.setXlineamt(poorddetail.getXqtypur().multiply(poorddetail.getXrate()));
			detail.setZid(sessionManager.getBusinessId());
			detail.setZauserid(getAuditUser());
			
			// if item has no qty, then it don't need to save
			if(BigDecimal.ZERO.equals(detail.getXqtygrn())) continue;

			long dcount = pogrnMapper.savePogrnDetail(detail);
			if(dcount == 0) throw new ServiceException("Can't save detail");

			poorddetail.setXqtygrn(poorddetail.getXqtygrn().add(detail.getXqtygrn()));
			
			// update grn total amt
			pogrnHeader.setXtotamt(pogrnHeader.getXtotamt().add(detail.getXlineamt()));
		}

		// now update poorddetails with grn qty
		for(int i = 0; i < poordDetailList.size(); i++) {
			PoordDetail poorddetail = poordDetailList.get(i);
			long dcount = updateDetail(poorddetail);
			if(dcount == 0) throw new ServiceException("Can't update purchase detail");
		}

		// now update pogrnheader with total amount
		long countupdate = pogrnMapper.updatePogrnHeader(pogrnHeader);
		if(countupdate == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update GRN total amount for purchase order : " + xpornum);
			return responseHelper.getResponse();
		}

		

		// now update poordheader status
		poordHeader.setXstatuspor("Full Received");
		long phcount = update(poordHeader);
		if(phcount == 0) throw new ServiceException("Can't update purchase order status");

		
		// Validate
		if(pogrnHeader.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("GRN date required");
			return responseHelper.getResponse();
		}
		if(StringUtils.isBlank(pogrnHeader.getXwh())) {
			responseHelper.setErrorStatusAndMessage("Warehouse required");
			return responseHelper.getResponse();
		}
		
		if (StringUtils.isBlank(pogrnHeader.getXcus())) {
			responseHelper.setErrorStatusAndMessage("Supplier required");
			return responseHelper.getResponse();
		}
		if(StringUtils.isBlank(pogrnHeader.getXtype())) {
			responseHelper.setErrorStatusAndMessage("Purchase Type Required");
			return responseHelper.getResponse();
		}
		if (!"Open".equalsIgnoreCase(pogrnHeader.getXstatusgrn())) {
			responseHelper.setErrorStatusAndMessage("GRN already confirmed");
			return responseHelper.getResponse();
		}

		String p_seq;
		if (!"Confirmed".equalsIgnoreCase(pogrnHeader.getXstatusgrn())) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			procPO_confirmGRN(pogrnHeader.getXgrnnum(), p_seq);
			String em = getProcedureErrorMessages(p_seq);
			if (StringUtils.isNotBlank(em)) {
				responseHelper.setErrorStatusAndMessage(em);
				return responseHelper.getResponse();
			}
		}

		if (!"Confirmed".equalsIgnoreCase(pogrnHeader.getXstatusap())) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			procTransferPOtoAP(pogrnHeader.getXgrnnum(), p_seq);
			String em = getProcedureErrorMessages(p_seq);
			if (StringUtils.isNotBlank(em)) {
				responseHelper.setErrorStatusAndMessage(em);
				return responseHelper.getResponse();
			}
		}

		if(isModuleActive(Modules.ACCOUNTING)) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			poTransferPOtoGL(pogrnHeader.getXgrnnum(), "pogrnheaderac", p_seq);
			String em = getProcedureErrorMessages(p_seq);
			if (StringUtils.isNotBlank(em)) {
				responseHelper.setErrorStatusAndMessage(em);
				return responseHelper.getResponse();
			}
		}

		responseHelper.setSuccessStatusAndMessage("GRN '"+ pogrnHeader.getXgrnnum()+"' Created & Confirmed successfully");
		responseHelper.setRedirectUrl("/procurements/poord/" + poordHeader.getXpornum());
		return responseHelper.getResponse();
	}

	@Override
	public List<PoordDetail> searchPurchaseOrderAvailableItem(String xpornum, String xitem){
		if(StringUtils.isBlank(xpornum) || StringUtils.isBlank(xitem)) return Collections.emptyList();
		return poordMapper.searchPurchaseOrderAvailableItem(xpornum, xitem.toUpperCase(), sessionManager.getBusinessId());
	}

	@Transactional
	@Override
	public boolean confirmRequisitionsOfBranch(String xdoreqnum) throws ServiceException {
		// Change requisition order status
		Opreqheader ph = opreqService.findOpreqHeaderByXdoreqnum(xdoreqnum);
		if(ph == null) throw new ServiceException("Can't find any requisition in this system");

		// find all order requisition details first
		List<Opreqdetail> poordDetailsList =  opreqService.findOpreqDetailByXdoreqnum(xdoreqnum);
		if(poordDetailsList == null || poordDetailsList.isEmpty()) throw new ServiceException("Requisition has no item added");

		// Create sales order header
		Opordheader oh = new Opordheader();
		oh.setXtypetrn(TransactionCodeType.SALES_ORDER.getCode());
		oh.setXtrn(TransactionCodeType.SALES_ORDER.getdefaultCode());
		oh.setXpornum(ph.getXdoreqnum());
		oh.setXdate(new Date());
		oh.setXstatus("Open");
		oh.setXstatusord("Open");
		oh.setXnote(ph.getXnote());
		oh.setXdiscamt(BigDecimal.ZERO);
		oh.setXvatait("No Vat");
		oh.setXwh(StringUtils.isNotBlank(ph.getXwh()) ? ph.getXwh() : StringUtils.isNotBlank(sessionManager.getLoggedInUserDetails().getXwh()) ? sessionManager.getLoggedInUserDetails().getXwh() : "01");
		oh.setXtype("SO");

		// Tag with branch customer    xgcus   xcuszid
		Cacus cacus = cacusService.findByXcus(ph.getXcus());
		if(cacus == null) throw new ServiceException("There is no customer found for this branch");
		oh.setXcus(cacus.getXcus());

		long ohCount = opordService.saveOpordHeader(oh);
		if(ohCount == 0) throw new ServiceException("Can't crete sales order");

		// if header saved successfully, then find it again from db to get xordernum
		// find oh by  xpornum, xdate, xtypetrn and xcus 
		Opordheader savedoh = opordService.findOpordHeaderByXtypetrnAndXpornumAndXdateAndXcus(oh.getXtypetrn(), oh.getXpornum(), oh.getXcus(), oh.getXdate());
		if(savedoh == null) throw new ServiceException("Can't found any sales order");

		// if detail data exist
		List<Oporddetail> detailsList = new ArrayList<>();
		for(Opreqdetail pd : poordDetailsList) {
			// create all sales details from requisition details
			Caitem c = caitemService.findByXitem(pd.getXitem());
			if(c == null) throw new ServiceException("Item "+ pd.getXitem() +" not found");

			Oporddetail od = new Oporddetail();
			od.setXordernum(savedoh.getXordernum());
			od.setXitem(pd.getXitem());
			od.setXunit(pd.getXunit());
			od.setXqtyord(pd.getXqtyord() == null ? BigDecimal.ZERO : pd.getXqtyord());
			od.setXrate(pd.getXrate() == null ? BigDecimal.ZERO : pd.getXrate());
			od.setXdesc(c.getXdesc());
			od.setXcatitem(c.getXcatitem());
			od.setXgitem(c.getXgitem());
			od.setXlineamt(od.getXqtyord().multiply(od.getXrate()));

			detailsList.add(od);
		}

		// now save all details
		long countOD = opordService.saveBatchOpordDetail(detailsList);
		if(countOD == 0) throw new ServiceException("Can't create sales order detail");

		// Update status and order reference
		ph.setXstatus("SO Created");
		ph.setXstatusreq("Confirmed");
		ph.setXordernum(savedoh.getXordernum());
		long count = opreqService.updateOpreqheader(ph);
		if(count == 0) throw new ServiceException("Can't update requisition status");

		return true;
	}

	@Override
	public List<PoordHeader> getAllLCMPoordHeaders(String xtypetrn) {
		if(StringUtils.isBlank(xtypetrn)) return Collections.emptyList();
		return poordMapper.getAllLCMPoordHeaders(xtypetrn, sessionManager.getBusinessId());
	}

	@Override
	public List<Termsdef> getDefaultTermsdef() {
		return poordMapper.getDefaultTermsdef(sessionManager.getBusinessId());
	}

	@Override
	public void procSP_CREATEQC_FROMWO(String xpornum, String p_seq) {
		poordMapper.procSP_CREATEQC_FROMWO(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xpornum, p_seq);
	}

	@Override
	public List<PoordHeader> getALllPoordHeaderByXpreparer(String xpreparer) {
		return poordMapper.getALllPoordHeaderByXpreparer(sessionManager.getBusinessId(), xpreparer);
	}

	@Transactional
	@Override
	public long saveTermstrn(Termstrn termstrn) {
		if(termstrn == null || StringUtils.isBlank(termstrn.getXtype())) return 0;
		termstrn.setZid(sessionManager.getBusinessId());
		termstrn.setZauserid(getAuditUser());
		long count = poordMapper.saveTermstrn(termstrn);
		
		return count;
	}

	@Transactional
	@Override
	public long updateTermstrn(Termstrn termstrn) {
		if(termstrn == null || StringUtils.isBlank(termstrn.getXdocnum())) return 0;
		termstrn.setZid(sessionManager.getBusinessId());
		termstrn.setZauserid(getAuditUser());
		long count = poordMapper.updateTermstrn(termstrn);
		return count;
	}

	@Transactional
	@Override
	public long deleteTermstrn(Termstrn termstrn) {
		if(termstrn == null) return 0;
		long count = poordMapper.deleteTermstrn(termstrn);
		return count;
	}

	@Override
	public List<Termstrn> getAllTermstrn() {
		return poordMapper.getAllTermstrn(sessionManager.getBusinessId());
	}

	@Override
	public List<Termstrn> findTermstrnByXdocnum(String xdocnum) {
		if(StringUtils.isBlank(xdocnum)) return null;
		return poordMapper.findTermstrnByXdocnum(xdocnum, sessionManager.getBusinessId());
	}

	@Transactional
	@Override
	public void procAdd_default_terms(String xpornum, String xtype, String p_seq) {
		poordMapper.procAdd_default_terms(sessionManager.getBusinessId(), getAuditUser(), xpornum,xtype, p_seq);
	}

	@Override
	public Termstrn findTermstrnByXtypeAndXterm(String xtype, String xterm) {
		if(StringUtils.isBlank(xtype) || StringUtils.isBlank(xterm)) return null;
		return poordMapper.findTermstrnByXtypeAndXterm(xtype, xterm, sessionManager.getBusinessId());
	}

	@Override
	public Termstrn findTermstrnByXtypeAndXtermAndXdocnum(String xtype, String xterm, String xdocnum) {
		if(StringUtils.isBlank(xtype) || StringUtils.isBlank(xterm) || StringUtils.isBlank(xdocnum)) return null;
		return poordMapper.findTermstrnByXtypeAndXtermAndXdocnum(xtype, xterm,xdocnum, sessionManager.getBusinessId());
	}
	
	@Transactional
	@Override
	public void procPO_confirmGRN(String xgrnnum, String p_seq) {
		pogrnMapper.procPO_confirmGRN(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xgrnnum, p_seq);
	}
	
	@Transactional
	@Override
	public void poTransferPOtoGL(String xgrnnum, String pscreen, String p_seq) {
		pogrnMapper.poTransferPOtoGL(sessionManager.getBusinessId(), getAuditUser(), xgrnnum, pscreen, p_seq);
	}
	
	@Override
	@Transactional
	public void procTransferPOtoAP(String xgrnnum, String p_seq) {
		pogrnMapper.procTransferPOtoAP(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xgrnnum, p_seq);
	}

	@Override
	public void procSP_CREATEGRN_FROMPO(String xpornum, String p_screen, String p_seq) {
		poordMapper.procSP_CREATEGRN_FROMPO(sessionManager.getBusinessId(), getAuditUser(), xpornum, p_screen, p_seq);
	}
}
