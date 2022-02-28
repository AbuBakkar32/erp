package com.asl.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.ImtorDetail;
import com.asl.entity.ImtorHeader;
import com.asl.entity.PoordDetail;
import com.asl.entity.PoordHeader;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.mapper.ImtorMapper;
import com.asl.mapper.PoordMapper;
import com.asl.model.ResponseHelper;
import com.asl.model.ServiceException;
import com.asl.service.ImtorService;

@Service
public class ImtorServiceImpl extends AbstractGenericService implements ImtorService {

	@Autowired private ImtorMapper imtorMapper;
	@Autowired private PoordMapper poordMapper;

	@Transactional
	@Override
	public long save(ImtorHeader imtorHeader) {
		if (imtorHeader == null || StringUtils.isBlank(imtorHeader.getXtrn()))
			return 0;
		imtorHeader.setZid(sessionManager.getBusinessId());
		imtorHeader.setZauserid(getAuditUser());
		if(StringUtils.isNotBlank(imtorHeader.getXtornum()) && StringUtils.isNotBlank(imtorHeader.getXchalanref()))
			return imtorMapper.saveImtorHeaderWithChalan(imtorHeader);
		
		return imtorMapper.saveImtorHeader(imtorHeader);
	}

	@Transactional
	@Override
	public long update(ImtorHeader imtorHeader) {
		if (imtorHeader == null || StringUtils.isBlank(imtorHeader.getXtornum())) return 0;
		if(StringUtils.isBlank(imtorHeader.getZid())) imtorHeader.setZid(sessionManager.getBusinessId());
		imtorHeader.setZuuserid(getAuditUser());
		return imtorMapper.updateImtorHeader(imtorHeader);
	}

	@Transactional
	@Override
	public long delete(ImtorHeader imtorHeader) {
		if(imtorHeader == null) return 0;
		long count = imtorMapper.deleteImtorHeader(imtorHeader);
		return count;
	}

	@Transactional
	@Override
	public long saveDetail(ImtorDetail imtorDetail) {
		if(imtorDetail == null || StringUtils.isBlank(imtorDetail.getXtornum())) return 0;
		imtorDetail.setZid(sessionManager.getBusinessId());
		imtorDetail.setZauserid(getAuditUser());
		long count = imtorMapper.saveImtorDetail(imtorDetail);
		//if(count != 0) {count = updatePoordHeaderTotalAmt(imtorDetail);}
		return count;
	}

	@Override
	@Transactional
	public long saveDetail(List<ImtorDetail> imtorDetails) throws ServiceException {
		if(imtorDetails == null || imtorDetails.isEmpty()) return 0;
		long totalCount = 0;
		for(ImtorDetail id : imtorDetails) {
			id.setZid(sessionManager.getBusinessId());
			id.setZauserid(getAuditUser());
			long count = imtorMapper.saveImtorDetail(id);
			if(count == 0) {
				throw new ServiceException("All details not saved");
			}
			totalCount += count;
		}
		return totalCount;
	}

	@Transactional
	@Override
	public long updateDetail(ImtorDetail imtorDetail) {
		if(imtorDetail == null || StringUtils.isBlank(imtorDetail.getXtornum())) return 0;
		imtorDetail.setZid(sessionManager.getBusinessId());
		imtorDetail.setZuuserid(getAuditUser());
		long count = imtorMapper.updateImtorDetail(imtorDetail);
		//if(count != 0) {count = updatePoordHeaderTotalAmt(imtorDetail);}
		return count;
	}

	@Transactional
	@Override
	public long deleteDetail(ImtorDetail imtorDetail) {
		if(imtorDetail == null) return 0;
		long count = imtorMapper.deleteImtorDetail(imtorDetail);
		//if(count != 0) {count = updatePoordHeaderTotalAmt(imtorDetail);}
		return count;
	}

	@Override
	public ImtorHeader findImtorHeaderByXtornum(String xtornum) {
		if (StringUtils.isBlank(xtornum)) return null;
		return imtorMapper.findImtorHeaderByXtornum(xtornum, sessionManager.getBusinessId());
	}

	@Override
	public List<ImtorHeader> findImtorHeaderByXchalanref(String xchalanref) {
		if (StringUtils.isBlank(xchalanref)) return null;
		return imtorMapper.findImtorHeaderByXchalanref(xchalanref, sessionManager.getBusinessId());
	}

	@Override
	public List<ImtorHeader> findImtorHeaderByXchalanrefAndXfwhAndXtwh(String xchalanref, String xfwh, String xtwh) {
		if (StringUtils.isBlank(xchalanref) || StringUtils.isBlank(xfwh) || StringUtils.isBlank(xtwh)) return null;
		return imtorMapper.findImtorHeaderByXchalanrefAndXfwhAndXtwh(xchalanref, xfwh, xtwh, sessionManager.getBusinessId());
	}

	@Override
	public List<ImtorHeader> getAllImtorHeader() {
		return imtorMapper.getAllImtorHeader(sessionManager.getBusinessId());
	}

	@Override
	public List<ImtorDetail> findImtorDetailByXtornum(String xtornum) {
		if(StringUtils.isBlank(xtornum))
			return null;
		String centralZid = null;
		if(Boolean.TRUE.equals(sessionManager.getZbusiness().getCentral())) {
			centralZid = sessionManager.getBusinessId();
		} else {
			centralZid = sessionManager.getZbusiness().getCentralzid();
		}
		return imtorMapper.findImtorDetailByXtornum(xtornum, sessionManager.getBusinessId(), centralZid);
	}
	
	@Override
	public List<ImtorDetail> findImtorDetailByXtornumAndXchalanref(String xtornum, String xchalanref) {
		if(StringUtils.isBlank(xchalanref))
			return null;
		return imtorMapper.findImtorDetailByXtornumAndXchalanref(xtornum, xchalanref, sessionManager.getBusinessId());
	}
	
	

	@Override
	public ImtorDetail findImtorDetailByXtornumAndXrow(String xtornum, int xrow) {
		if (StringUtils.isBlank(xtornum) || xrow == 0)
			return null;
		return imtorMapper.findImtorDetailByXtornumAndXrow(xtornum, xrow, sessionManager.getBusinessId());
	}

	@Override
	public ImtorDetail findImtorDetailByXtornumAndXitem(String xtornum, String xitem) {
		if (StringUtils.isBlank(xtornum) || StringUtils.isBlank(xitem))
			return null;
		return imtorMapper.findImtorDetailByXtornumAndXitem(xtornum, xitem, sessionManager.getBusinessId());
	}

	@Transactional
	@Override
	public long updateImtorHeaderTotalAmt(ImtorDetail imtorDetail) {
		if(imtorDetail == null) return 0;
		return imtorMapper.updateImtorHeaderTotalAmt(imtorDetail);
	}

	@Override
	public void procConfirmTO(String xtornum, String p_action, String p_seq) {
		imtorMapper.procConfirmTO(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xtornum, p_action, p_seq);
	}

	@Transactional
	@Override
	public long deleteImtorDetailByXtornum(String xtornum) {
		if(StringUtils.isBlank(xtornum)) return 0;
		return imtorMapper.deleteImtorDetailByXtornum(xtornum, sessionManager.getBusinessId());
	}

	@Override
	public List<ImtorHeader> getAllImtorHeaderbyPrefix(String xtypetrn) {
		if(StringUtils.isBlank(xtypetrn)) return Collections.emptyList();
		return imtorMapper.getAllImtorHeaderbyPrefix(xtypetrn, sessionManager.getBusinessId());
	}

	@Transactional
	@Override
	public Map<String, Object> createPurchaseOrder(ResponseHelper responseHelper, String xtornum) throws ServiceException {
		ImtorHeader imtorHeader = findImtorHeaderByXtornum(xtornum);
		if(imtorHeader == null) {
			responseHelper.setErrorStatusAndMessage("Can't find store requisition!");
			return responseHelper.getResponse();
		}

		List<ImtorDetail> imtorDetailList = findImtorDetailByXtornum(xtornum);
		if(imtorDetailList== null || imtorDetailList.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Please add detail!");
			return responseHelper.getResponse();
		}

		PoordHeader poord = new PoordHeader();
		poord.setXtypetrn(TransactionCodeType.PURCHASE_ORDER.getCode());
		poord.setXtrn(TransactionCodeType.PURCHASE_ORDER.getdefaultCode());
		poord.setXstatuspor("Open");
		poord.setXtotamt(BigDecimal.ZERO);
		poord.setXtype("PO");
		poord.setXdate(new Date());
		poord.setXtotamt(BigDecimal.ZERO);
		poord.setXwh(StringUtils.isNotBlank(poord.getXwh()) ? poord.getXwh() : StringUtils.isNotBlank(sessionManager.getLoggedInUserDetails().getXwh()) ? sessionManager.getLoggedInUserDetails().getXwh() : "01");
		poord.setXcus(poord.getXcus());
		poord.setXnote(poord.getXnote());
 
		long count = poordMapper.savePoordHeader(poord);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't create purchase order");
			return responseHelper.getResponse();
		}
		
		// Create purchase details from imtor details
		for(int i = 0; i < imtorDetailList.size(); i++) {
			ImtorDetail imtordetail = imtorDetailList.get(i);
			if(imtordetail.getXqtyord() == null) imtordetail.setXqtyord(BigDecimal.ZERO);

			PoordDetail poorddetail = new PoordDetail();
			poorddetail.setXpornum(poord.getXporeqnum());
			poorddetail.setXitem(poorddetail.getXitem());
			poorddetail.setXunitpur(poorddetail.getXunitpur());
			poorddetail.setXrate(poorddetail.getXrate());
			poorddetail.setXlineamt(poorddetail.getXlineamt());
			poorddetail.setXqtyord(poorddetail.getXqtyord());

			// if item has no qty, then it don't need to save
			if(BigDecimal.ZERO.equals(poorddetail.getXqtyord())) continue;
	
			long dcount = poordMapper.savePoordDetail(poorddetail);
			if(dcount == 0)  throw new ServiceException("Can't save detail");
	
			imtordetail.setXqtyord(imtordetail.getXqtyord().add(imtordetail.getXqtyord()));
			// update poord total amt
			poord.setXtotamt(poord.getXtotamt().add(poorddetail.getXlineamt()));
		}
		

		for(int i = 0; i < imtorDetailList.size(); i++) {
			ImtorDetail imtordetail = imtorDetailList.get(i);
			long dcount = updateDetail(imtordetail);
			if(dcount == 0) throw new ServiceException("Can't update requisition detail");
		}

		// now update poordheader with total amount
		long countupdate = poordMapper.updatePoordHeader(poord);
		if(countupdate == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update Purchase total amount for requisition order : " + xtornum);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Purchase order created successfully");
		responseHelper.setRedirectUrl("/procurements/poord/");
		return responseHelper.getResponse();

	}



	
}
