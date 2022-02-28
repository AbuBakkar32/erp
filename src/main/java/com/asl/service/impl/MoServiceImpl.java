package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.DailyProductionBatchDetail;
import com.asl.entity.Modetail;
import com.asl.entity.Moheader;
import com.asl.entity.Opordheader;
import com.asl.mapper.MoMapper;
import com.asl.mapper.OpordMapper;
import com.asl.model.ServiceException;
import com.asl.service.MoService;
import com.asl.service.OpordService;

/**
 * @author Zubayer Ahamed
 * @since Mar 18, 2021
 */
@Service
public class MoServiceImpl extends AbstractGenericService implements MoService {

	@Autowired private MoMapper moMapper;
	@Autowired private OpordMapper opordMapper;
	@Autowired private OpordService opordService;

	@Override
	public long saveMoHeader(Moheader moheader) {
		if(moheader == null || StringUtils.isBlank(moheader.getXtypetrn())) return 0;
		moheader.setZid(sessionManager.getBusinessId());
		moheader.setZauserid(getAuditUser());
		return moMapper.saveMoHeader(moheader);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public long saveBatchMoHeader(List<Moheader> moheaders) {
		if(moheaders == null || moheaders.isEmpty()) return 0;
		long count = 0;
		for(Moheader moh : moheaders) {
			count += saveMoHeader(moh);
		}
		return count;
	}

	@Override
	@Transactional
	public long updateMoHeader(Moheader moheader) {
		if(moheader == null || StringUtils.isBlank(moheader.getXbatch())) return 0;
		moheader.setZid(sessionManager.getBusinessId());
		moheader.setZuuserid(getAuditUser());
		return moMapper.updateMoHeader(moheader);
	}

	@Override
	@Transactional
	public long updateMoHeader(List<Moheader> moheaders) {
		if(moheaders == null || moheaders.isEmpty()) return 0;
		long f_count = 0;
		for(Moheader moh : moheaders) {
			f_count += updateMoHeader(moh);
		}
		return f_count;
	}

	@Override
	@Transactional
	public long saveMoDetail(Modetail modetail) {
		if(modetail == null || StringUtils.isBlank(modetail.getXbatch())) return 0;
		modetail.setZid(sessionManager.getBusinessId());
		modetail.setZauserid(getAuditUser());
		return moMapper.saveMoDetail(modetail);
	}

	@Override
	@Transactional
	public long saveBatchMoDetail(List<Modetail> modetails) {
		if(modetails == null || modetails.isEmpty()) return 0;
		long finalCount = 0;
		for(Modetail md : modetails) {
			finalCount += saveMoDetail(md);
		}
		return finalCount;
	}

	@Override
	public long updateMoDetail(Modetail modetail) {
		if(modetail == null || StringUtils.isBlank(modetail.getXbatch()) || modetail.getXrow() == 0) return 0;
		modetail.setZid(sessionManager.getBusinessId());
		modetail.setZuuserid(getAuditUser());
		return moMapper.updateMoDetail(modetail);
	}

	@Override
	public long deleteModetail(Modetail modetail) {
		if(modetail == null || StringUtils.isBlank(modetail.getXbatch()) || modetail.getXrow() == 0) return 0;
		modetail.setZid(sessionManager.getBusinessId());
		return moMapper.deleteModetail(modetail);
	}

	@Override
	public Moheader findMoHeaderByXbatch(String xbatch) {
		if(StringUtils.isBlank(xbatch)) return null;
		return moMapper.findMoHeaderByXbatch(xbatch, sessionManager.getBusinessId());
	}

	@Override
	public Modetail findModetailByXrowAndXbatch(int xrow, String xbatch) {
		if(StringUtils.isBlank(xbatch) || xrow == 0) return null;
		return moMapper.findModetailByXrowAndXbatch(xrow, xbatch, sessionManager.getBusinessId());
	}

	@Override
	public List<Modetail> findModetailByXbatch(String xbatch) {
		if(StringUtils.isBlank(xbatch)) return Collections.emptyList();
		return moMapper.findModetailByXbatch(xbatch, sessionManager.getBusinessId());
	}

	@Override
	public List<Moheader> getAllMoheader() {
		return moMapper.getAllMoheader(sessionManager.getBusinessId());
	}

	@Override
	public List<Moheader> findMoheaderByXchalanAndXitem(String xchalan, String xitem) {
		if(StringUtils.isBlank(xchalan) || StringUtils.isBlank(xitem)) return null;
		return moMapper.findMoheaderByXchalanAndXitem(xchalan, xitem, sessionManager.getBusinessId());
	}

	@Override
	public Modetail findModetailByXbatchAndXitem(String xbatch, String xitem) {
		if(StringUtils.isBlank(xbatch) || StringUtils.isBlank(xitem)) return null;
		return moMapper.findModetailByXbatchAndXitem(xbatch, xitem, sessionManager.getBusinessId());
	}

	@Override
	public void processProduction(String batch, String action, String errseq) {
		moMapper.processProduction(batch, action, errseq, sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void bulkProcessProduction(List<String> batchList, String action, String errseq) {
		if(batchList == null || batchList.isEmpty()) return;
		for(String batch : batchList) {
			moMapper.processProduction(batch, action, errseq, sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername());
		}
	}

	@Override
	public Modetail findDefaultModetailByXbatch(String xbatch) {
		if(StringUtils.isBlank(xbatch)) return null;
		return moMapper.findModetailByXbatchAndXtype(xbatch, "Default", sessionManager.getBusinessId());
	}

	@Override
	public boolean isProductionProcessCompleted(String xchalan) {
		if(StringUtils.isBlank(xchalan)) return false;

		Opordheader chalan = opordMapper.findOpordHeaderByXordernum(xchalan, sessionManager.getBusinessId());
		if(chalan == null) return false;
		if(chalan.isProductioncompleted()) return true;

		List<Moheader> batches = moMapper.findMoHeaderByXchalan(xchalan, sessionManager.getBusinessId());
		if(batches == null || batches.isEmpty()) return false;

		boolean productionCompleted = true;
		for(Moheader batch : batches) {
			if(!"Completed".equalsIgnoreCase(batch.getXstatusmor())) {
				productionCompleted = false;
				break;
			}
		}

		if(!productionCompleted) return productionCompleted;

		// update chalan with flag
		chalan.setProductioncompleted(productionCompleted);
		long count = opordMapper.updateOpordHeader(chalan);
		if(count == 0) return false;

		return productionCompleted;
	}

	@Override
	public List<Moheader> findMoheaderByXchalan(String xchalan) {
		if(StringUtils.isBlank(xchalan)) return Collections.emptyList();
		return moMapper.findMoHeaderByXchalan(xchalan, sessionManager.getBusinessId());
	}

	@Override
	public List<Moheader> findModetailXbatch(String xbatch){
		if(StringUtils.isBlank(xbatch)) return Collections.emptyList();
		return moMapper.findModetailXbatch(xbatch, sessionManager.getBusinessId());
	}

	@Override
	public List<Modetail> findModetailByXtype(String xtype){
		if(StringUtils.isBlank(xtype)) return Collections.emptyList();
		return moMapper.findModetailByXtype(xtype, sessionManager.getBusinessId());
	}

	@Override
	public List<DailyProductionBatchDetail> dailyProductionReport(String xchalan) {
		if(StringUtils.isBlank(xchalan)) return Collections.emptyList();
		return moMapper.dailyProductionReport(xchalan, sessionManager.getBusinessId());
	}

	@Override
	@Transactional
	public long deleteModetailByXbatch(String xbatch) {
		if(StringUtils.isBlank(xbatch)) return 0;
		return moMapper.deleteModetailByXbatch(xbatch, sessionManager.getBusinessId());
	}

	@Override
	@Transactional
	public String createBulkBatchFromProductionChallan(String challan) throws ServiceException {
		Opordheader chalan = opordService.findOpordHeaderByXordernum(challan);
		if(chalan == null) return "redirect:/production/batch";
		
		
		
		return null;
	}

	

}
