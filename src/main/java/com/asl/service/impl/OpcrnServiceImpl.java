package com.asl.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Caitem;
import com.asl.entity.Opcrndetail;
import com.asl.entity.Opcrnheader;
import com.asl.entity.Opdodetail;
import com.asl.entity.Opdoheader;
import com.asl.enums.Modules;
import com.asl.enums.TransactionCodeType;
import com.asl.mapper.OpcrnMapper;
import com.asl.model.ResponseHelper;
import com.asl.model.ServiceException;
import com.asl.service.CaitemService;
import com.asl.service.OpcrnService;
import com.asl.service.OpdoService;
import com.asl.service.XtrnService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OpcrnServiceImpl extends AbstractGenericService implements OpcrnService {

	@Autowired private OpcrnMapper opcrnMapper;
	@Autowired private OpdoService opdoService;
	@Autowired private XtrnService xtrnService;
	@Autowired private CaitemService caitemService;

	@Transactional
	@Override
	public long save(Opcrnheader opcrnheader) {
		if(opcrnheader == null) return 0;
		opcrnheader.setZid(sessionManager.getBusinessId());
		opcrnheader.setZauserid(getAuditUser());
		return opcrnMapper.saveOpcrnHeader(opcrnheader);
	}

	@Transactional
	@Override
	public long update(Opcrnheader opcrnheader) {
		if (opcrnheader == null || StringUtils.isBlank(opcrnheader.getXcrnnum())) return 0;
		opcrnheader.setZid(sessionManager.getBusinessId());
		opcrnheader.setZuuserid(getAuditUser());
		return opcrnMapper.updateOpcrnHeader(opcrnheader);
	}

	@Transactional
	@Override
	public long deleteOpcrnHeader(Opcrnheader opcrnheader) {
		if(opcrnheader == null) return 0;
		return opcrnMapper.deleteOpcrnHeader(opcrnheader);
	}

	@Transactional
	@Override
	public long saveDetail(Opcrndetail opcrndetail) {
		if(opcrndetail == null || StringUtils.isBlank(opcrndetail.getXcrnnum())) return 0;
		opcrndetail.setZid(sessionManager.getBusinessId());
		opcrndetail.setZauserid(getAuditUser());
		return opcrnMapper.saveOpcrnDetail(opcrndetail);

	}

	@Transactional
	@Override
	public long saveDetail(List<Opcrndetail> opcrndetail) throws ServiceException {
		if(opcrndetail == null || opcrndetail.isEmpty()) return 0;
		long totalCount = 0;
		for(Opcrndetail opcrnDetail : opcrndetail) {
			opcrnDetail.setZid(sessionManager.getBusinessId());
			opcrnDetail.setZauserid(getAuditUser());
			long count = opcrnMapper.saveOpcrnDetail(opcrnDetail);
			if(count == 0) throw new ServiceException("Can't save return details");
			totalCount += count;
		}
		return totalCount;
	}

	@Transactional
	@Override
	public long updateDetail(Opcrndetail opcrndetail) {
		if(opcrndetail == null || StringUtils.isBlank(opcrndetail.getXcrnnum())) return 0;
		opcrndetail.setZid(sessionManager.getBusinessId());
		opcrndetail.setZuuserid(getAuditUser());
		long hcount =  opcrnMapper.updateOpcrnDetail(opcrndetail);
		if(hcount != 0) { hcount = updateOpcrnHeaderWithDetail(opcrndetail.getXcrnnum()); }
		return hcount;
	}

	@Transactional
	@Override
	public long deleteDetail(Opcrndetail opcrndetail) {
		if(opcrndetail == null) return 0;
		long hcount =  opcrnMapper.deleteOpcrnDetail(opcrndetail);
		if(hcount != 0) { hcount = updateOpcrnHeaderWithDetail(opcrndetail.getXcrnnum()); }
		return hcount;
	}

	@Override
	public List<Opcrnheader> getAllOpcrnheader() {
		return opcrnMapper.getAllOpcrnheader(sessionManager.getBusinessId());
	}

	@Override
	public List<Opcrndetail> findOpcrnDetailByXcrnnum(String xcrnnum) {
		if(StringUtils.isBlank(xcrnnum)) return Collections.emptyList();
		return opcrnMapper.findOpcrnDetailByXcrnnum(xcrnnum, sessionManager.getBusinessId());
	}

	@Override
	public Opcrnheader findOpcrnHeaderByXcrnnum(String xcrnnum) {
		if(StringUtils.isBlank(xcrnnum)) return null;
		return opcrnMapper.findOpcrnHeaderByXcrnnum(xcrnnum, sessionManager.getBusinessId());
	}

	@Override
	public Opcrnheader findOpcrnHeaderByXdornum(String xdornum) {
		if(StringUtils.isBlank(xdornum)) return null;
		return opcrnMapper.findOpcrnHeaderByXdornum(xdornum, sessionManager.getBusinessId());
	}

	@Override
	public Opcrndetail findOpcrnDetailByXcrnnumAndXrow(String xcrnnum, int xrow) {
		if(StringUtils.isBlank(xcrnnum) || xrow == 0) return null;
		return opcrnMapper.findOpcrndetailByXcrnnumAndXrow(xcrnnum, xrow, sessionManager.getBusinessId());
	}

	@Override
	public void procConfirmCRN(String xcrnnum, String p_seq) {
		opcrnMapper.procConfirmCRN(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xcrnnum, p_seq);
	}

	@Override
	public void procTransferOPtoAR(String xdornum, String p_screen, String p_seq) {
		opcrnMapper.procTransferOPtoAR(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xdornum, p_screen, p_seq);
	}

	@Transactional
	@Override
	public Map<String, Object> save(ResponseHelper responseHelper, Opcrnheader opcrnHeader, Opdoheader odh) {
		// get available invoice details
		List<Opdodetail> availableInvoiceDetails = new ArrayList<>();
		List<Opdodetail> invoiceDetails = opdoService.findOpdoDetailByXdornum(odh.getXdornum());
		for(Opdodetail d : invoiceDetails) {
			if(!d.getXqtyord().equals(d.getXqtycrn() == null ? BigDecimal.ZERO : d.getXqtycrn())) {
				availableInvoiceDetails.add(d);
			}
		}

		if(availableInvoiceDetails.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Invoice has no available item to return");
			return responseHelper.getResponse();
		}

		long count = save(opcrnHeader);
		if (count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't create sales return");
			return responseHelper.getResponse();
		}

		// create remaining details for crn
		List<Opcrndetail> details = new ArrayList<>();
		for(Opdodetail d : availableInvoiceDetails) {
			Opcrndetail p = new Opcrndetail();
			p.setXcrnnum(opcrnHeader.getXcrnnum());
			p.setXunitsel(d.getXunitsel());
			p.setXitem(d.getXitem());
			p.setXqtyord(d.getXqtyord().subtract(d.getXqtycrn() == null ? BigDecimal.ZERO : d.getXqtycrn()));
			p.setXrate(d.getXrate());
			p.setXlineamt(p.getXqtyord().multiply(p.getXrate()));
			p.setXdocrow(d.getXrow());
			p.setXrategrn(d.getXrategrn());
			p.setXaitamt(d.getXaitamt());
			p.setXcfsel(d.getXcfsel());
			details.add(p);

			// update grn xqtycrn
			d.setXqtycrn(p.getXqtyord().add(d.getXqtycrn() == null ? BigDecimal.ZERO : d.getXqtycrn()));

			Caitem caitem = caitemService.findByXitem(p.getXitem());
			if(caitem.getXvatrate() == null) caitem.setXvatrate(BigDecimal.ZERO);
			if(p.getXlineamt() == null) p.setXlineamt(BigDecimal.ZERO);
			// vat amount
			BigDecimal vat = null;
			vat = (p.getXlineamt().multiply(caitem.getXvatrate())).divide(BigDecimal.valueOf(100));
			p.setXvatamt(vat);  
		}

		// now save pocrndetail first
		try {
			long countd = saveDetail(details);
			if(countd == 0) {
				responseHelper.setErrorStatusAndMessage("Can't create sales return details");
				return responseHelper.getResponse();
			}
		} catch (ServiceException e) {
			log.error(ERROR, e.getMessage(), e);
			responseHelper.setErrorStatusAndMessage(e.getMessage());
			return responseHelper.getResponse();
		}

		// now update invoice details
		for(Opdodetail d : availableInvoiceDetails) {
			long countd = opdoService.updateDetail(d);
			if(countd == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update invoice details");
				return responseHelper.getResponse();
			}
		}
		
		// now update header total amount
		long counth = updateOpcrnHeaderWithDetail(opcrnHeader.getXcrnnum());
		if(counth == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update return total amount");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Sales return created successfully");
		responseHelper.setRedirectUrl("/salesninvoice/salesreturn/" + opcrnHeader.getXcrnnum());
		return responseHelper.getResponse();
	}

	@Transactional
	@Override
	public void confirmCRN(Opcrnheader opcrnHeader) throws ServiceException {
 		String p_seq;
		if (!"Confirmed".equalsIgnoreCase(opcrnHeader.getXstatuscrn())) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			procConfirmCRN(opcrnHeader.getXcrnnum(), p_seq);
			// Error check for procConfirmCRN
			String em = getProcedureErrorMessages(p_seq);
			if(StringUtils.isNotBlank(em)) {
				throw new ServiceException(em);
			}
		}
		if (!"Confirmed".equalsIgnoreCase(opcrnHeader.getXstatusar())) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			procTransferOPtoAR(opcrnHeader.getXcrnnum(), "opcrnheader", p_seq);
			// Error check for procTransferPRtoAP
			String em = getProcedureErrorMessages(p_seq);
			if(StringUtils.isNotBlank(em)) {
				throw new ServiceException(em);
			}
		}
		
		if(isModuleActive(Modules.ACCOUNTING)) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			opdoService.procTransferOPtoGL(opcrnHeader.getXcrnnum(), "opcrnheader", p_seq);
			// Error check here for procTransferOPtoAR 
			String em = getProcedureErrorMessages(p_seq); 
			if(StringUtils.isNotBlank(em)) {
				throw new ServiceException(em);
			}
		}
	}

	@Override
	public void procTransferOPtoGL(String xdornum, String p_screen, String p_seq) {
		opcrnMapper.procTransferOPtoGL(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xdornum, p_screen, p_seq);
	}

	@Transactional
	@Override
	public long updateOpcrnHeaderWithDetail(String xcrnnum) {
		if(StringUtils.isBlank(xcrnnum)) return 0;
		return opcrnMapper.updateOpcrnHeaderWithDetail(xcrnnum, sessionManager.getBusinessId());
	
	}
	

}
