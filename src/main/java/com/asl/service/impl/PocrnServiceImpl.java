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

import com.asl.entity.Pocrndetail;
import com.asl.entity.Pocrnheader;
import com.asl.entity.PogrnDetail;
import com.asl.entity.PogrnHeader;
import com.asl.enums.Modules;
import com.asl.enums.TransactionCodeType;
import com.asl.mapper.PocrnMapper;
import com.asl.model.ResponseHelper;
import com.asl.model.ServiceException;
import com.asl.service.PocrnService;
import com.asl.service.PogrnService;
import com.asl.service.XtrnService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PocrnServiceImpl extends AbstractGenericService implements PocrnService {

	@Autowired private PocrnMapper pocrnMapper;
	@Autowired private PogrnService pogrnService;
	@Autowired private XtrnService xtrnService;

	@Transactional
	@Override
	public long save(Pocrnheader pocrnheader) {
		if(pocrnheader == null) return 0;
		pocrnheader.setZid(sessionManager.getBusinessId());
		pocrnheader.setZauserid(getAuditUser());
		return pocrnMapper.savePocrnHeader(pocrnheader);
	}

	@Transactional
	@Override
	public long update(Pocrnheader pocrnheader) {
		if (pocrnheader == null || StringUtils.isBlank(pocrnheader.getXcrnnum())) return 0;
		if(StringUtils.isBlank(pocrnheader.getZid()))
			pocrnheader.setZid(sessionManager.getBusinessId());
		pocrnheader.setZuuserid(getAuditUser());
		return pocrnMapper.updatePocrnHeader(pocrnheader);
	}

	@Transactional
	@Override
	public long deletePocrnHeader(String xcrnnum) {
		if(StringUtils.isBlank(xcrnnum)) return 0;
		return pocrnMapper.deletePocrnHeader(xcrnnum, sessionManager.getBusinessId());
	}

	@Transactional
	@Override
	public long saveDetail(Pocrndetail pocrndetail) {
		if(pocrndetail == null || StringUtils.isBlank(pocrndetail.getXcrnnum())) return 0;
		pocrndetail.setZid(sessionManager.getBusinessId());
		pocrndetail.setZauserid(getAuditUser());
		long count = pocrnMapper.savePocrnDetail(pocrndetail);
		if(count != 0) count = updatePocrnHeaderWithDetail(pocrndetail.getXcrnnum());

		return count;
	}

	@Transactional
	@Override
	public long saveDetails(List<Pocrndetail> pocrndetail) throws ServiceException {
		if(pocrndetail == null || pocrndetail.isEmpty()) return 0;
		long totalCount = 0;
		for(Pocrndetail pocrnDetail : pocrndetail) {
			pocrnDetail.setZid(sessionManager.getBusinessId());
			pocrnDetail.setZauserid(getAuditUser());
			long count = pocrnMapper.savePocrnDetail(pocrnDetail);
			if(count == 0) throw new ServiceException("Can't save return details");
			totalCount += count;
		}
		return totalCount;
	}

	@Transactional
	@Override
	public long updateDetail(Pocrndetail pocrndetail) {
		if(pocrndetail == null || StringUtils.isBlank(pocrndetail.getXcrnnum())) return 0;
		pocrndetail.setZid(sessionManager.getBusinessId());
		pocrndetail.setZuuserid(getAuditUser());
		long count = pocrnMapper.updatePocrnDetail(pocrndetail);
		if(count != 0) count = updatePocrnHeaderWithDetail(pocrndetail.getXcrnnum());
		return count;
	}

	@Transactional
	@Override
	public long deleteDetail(Pocrndetail pocrndetail) {
		if(pocrndetail == null) return 0;
		long count = pocrnMapper.deletePocrnDetail(pocrndetail);
		if(count != 0) count = updatePocrnHeaderWithDetail(pocrndetail.getXcrnnum());
		return pocrnMapper.deletePocrnDetail(pocrndetail);
	}

	@Override
	public List<Pocrnheader> getAllPocrnheader() {
		return pocrnMapper.getAllPocrnheader(sessionManager.getBusinessId());
	}

	@Override
	public List<Pocrndetail> findPocrnDetailByXcrnnum(String xcrnnum) {
		if(StringUtils.isBlank(xcrnnum)) return Collections.emptyList();
		return pocrnMapper.findPocrnDetailByXcrnnum(xcrnnum, sessionManager.getBusinessId());
	}

	@Override
	public Pocrnheader findPocrnHeaderByXcrnnum(String xcrnnum) {
		if(StringUtils.isBlank(xcrnnum)) return null;
		return pocrnMapper.findPocrnHeaderByXcrnnum(xcrnnum, sessionManager.getBusinessId());
	}

	@Override
	public Pocrnheader findPocrnHeaderByXgrnnum(String xgrnnum) {
		if(StringUtils.isBlank(xgrnnum)) return null;
		return pocrnMapper.findPocrnHeaderByXgrnnum(xgrnnum, sessionManager.getBusinessId());
	}

	@Override
	public Pocrndetail findPocrnDetailByXcrnnumAndXrow(String xcrnnum, int xrow) {
		if(StringUtils.isBlank(xcrnnum) || xrow == 0) return null;
		return pocrnMapper.findPocrndetailByXcrnnumAndXrow(xcrnnum, xrow, sessionManager.getBusinessId());
	}

	@Transactional
	@Override
	public void procConfirmCRN(String xcrnnum, String p_seq) {
		pocrnMapper.procConfirmCRN(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xcrnnum, p_seq);
	}

	@Transactional
	@Override
	public void procIssuePricing(String xtrnnum, String xwh, String p_seq) {
		pocrnMapper.procIssuePricing(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xtrnnum, xwh, p_seq);
	}

	@Transactional
	@Override
	public void SRPR_transferARtoGL(String xcrnnum, String p_seq) {
		pocrnMapper.SRPR_transferARtoGL(sessionManager.getBusinessId(), getAuditUser(), xcrnnum, p_seq);
	}

	@Transactional
	@Override
	public void procTransferPRtoAP(String xcrnnum, String p_seq) {
		pocrnMapper.procTransferPRtoAP(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xcrnnum, p_seq);
	}	

	@Override
	public List<Pocrnheader> findPocrnXstatuscrn(String hint) {
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		return pocrnMapper.findPocrnXstatuscrn(hint.toUpperCase(), sessionManager.getBusinessId());
	}

	@Transactional
	@Override
	public Map<String, Object> save(ResponseHelper responseHelper, Pocrnheader pocrnHeader, PogrnHeader pgh) {
		// get available grn details
		List<PogrnDetail> availableGrndetails = new ArrayList<>();
		List<PogrnDetail> grndetails = pogrnService.findPogrnDetailByXgrnnum(pgh.getXgrnnum());
		for(PogrnDetail d : grndetails) {
			if(!d.getXqtygrn().equals(d.getXqtyprn() == null ? BigDecimal.ZERO : d.getXqtyprn())) {
				availableGrndetails.add(d);
			}
		}
		if(availableGrndetails.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("GRN has no available item to return");
			return responseHelper.getResponse();
		}

		long count = save(pocrnHeader);
		if (count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't create purchase return");
			return responseHelper.getResponse();
		}

		// create remaining details for crn
		List<Pocrndetail> details = new ArrayList<>();
		for(PogrnDetail d : availableGrndetails) {
			Pocrndetail p = new Pocrndetail();
			p.setXcrnnum(pocrnHeader.getXcrnnum());
			p.setXunit(d.getXunitpur());
			p.setXitem(d.getXitem());
			p.setXqtyord(d.getXqtygrn().subtract(d.getXqtyprn() == null ? BigDecimal.ZERO : d.getXqtyprn()));
			p.setXrate(d.getXrate());
			p.setXlineamt(p.getXqtyord().multiply(p.getXrate()));
			p.setXdocrow(d.getXrow());
			p.setXcfpur(d.getXcfpur());
			details.add(p);

			// update grn xqtycrn
			d.setXqtyprn(p.getXqtyord().add(d.getXqtyprn() == null ? BigDecimal.ZERO : d.getXqtyprn()));
		}

		// now save pocrndetail first
		try {
			long countd = saveDetails(details);
			if(countd == 0) {
				responseHelper.setErrorStatusAndMessage("Can't create purchase return details");
				return responseHelper.getResponse();
			}
		} catch (ServiceException e) {
			log.error(ERROR, e.getMessage(), e);
			responseHelper.setErrorStatusAndMessage(e.getMessage());
			return responseHelper.getResponse();
		}

		// now update grn details
		for(PogrnDetail d : availableGrndetails) {
			long countd = pogrnService.updateDetail(d);
			if(countd == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update grn details");
				return responseHelper.getResponse();
			}
		}

		responseHelper.setSuccessStatusAndMessage("Purchase return created successfully");
		responseHelper.setRedirectUrl("/procurements/purchasereturn/" + pocrnHeader.getXcrnnum());
		return responseHelper.getResponse();
	}

	@Transactional
	@Override
	public void confirmCRN(Pocrnheader pocrnHeader) throws ServiceException {
		String p_seq;
		if (!"Confirmed".equalsIgnoreCase(pocrnHeader.getXstatuscrn())) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			procConfirmCRN(pocrnHeader.getXcrnnum(), p_seq);
			// Error check for procConfirmCRN
			String em = getProcedureErrorMessages(p_seq);
			if (StringUtils.isNotBlank(em)) {
				throw new ServiceException(em);
			}

			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			procIssuePricing(pocrnHeader.getXgrnnum(), pocrnHeader.getXwh(), p_seq);
			// Error check for procIssuePricing
			em = getProcedureErrorMessages(p_seq);
			if (StringUtils.isNotBlank(em)) {
				throw new ServiceException(em);
			}

		}
		if (!"Confirmed".equalsIgnoreCase(pocrnHeader.getXstatusap())) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			procTransferPRtoAP(pocrnHeader.getXcrnnum(), p_seq);
			// Error check for procTransferPRtoAP
			String em = getProcedureErrorMessages(p_seq);
			if (StringUtils.isNotBlank(em)) {
				throw new ServiceException(em);
			}
		}

		if(isModuleActive(Modules.ACCOUNTING)) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			SRPR_transferARtoGL(pocrnHeader.getXcrnnum(), p_seq);
			// Error check for procTransferPRtoAP
			String em = getProcedureErrorMessages(p_seq);
			if (StringUtils.isNotBlank(em)) {
				throw new ServiceException(em);
			}
		}

	}

	@Override
	public long updatePocrnHeaderWithDetail(String xcrnnum) {
		if(StringUtils.isBlank(xcrnnum)) return 0;
		return pocrnMapper.updatePocrnHeaderWithDetail(xcrnnum, sessionManager.getBusinessId());
	}
}
