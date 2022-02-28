package com.asl.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Acdef;
import com.asl.entity.Acdetail;
import com.asl.entity.Acheader;
import com.asl.entity.Xtrn;
import com.asl.mapper.AcMapper;
import com.asl.model.ResponseHelper;
import com.asl.model.ServiceException;
import com.asl.service.AcService;
import com.asl.service.AcdefService;

/**
 * @author Zubayer Ahamed
 * @since Jul 10, 2021
 */
@Service
public class AcServiceImpl extends AbstractGenericService implements AcService {

	@Autowired private AcMapper acMapper;
	@Autowired private AcdefService acdefservice;

	@Transactional
	@Override
	public long saveAcheader(Acheader acheader) {
		if(acheader == null) return 0;
		acheader.setZid(sessionManager.getBusinessId());
		acheader.setZauserid(getAuditUser());
		return acMapper.saveAcheader(acheader);
	}

	@Transactional
	@Override
	public Map<String, Object> saveWithDetail(Acheader acheader, List<Acdetail> acdetails, ResponseHelper responseHelper)  throws ServiceException {
		if(acheader == null) throw new ServiceException("Data not found to save");

		// validate
		if(acheader.getXdate() == null) throw new ServiceException("Voucher date required");
		if(StringUtils.isBlank(acheader.getXwh())) throw new ServiceException("Project required");

		// set year and date based on xdate   
		Acdef acdef = acdefservice.find();
		if(acdef == null) throw new ServiceException("Account default is not set in this sytem");

		// voucher cal
		acheader = setXyearAndXper(acheader, acdef);

		// save or update acheader first
		// if existing
		if(StringUtils.isNotBlank(acheader.getXvoucher())) {
			Acheader exist = findAcheaderByXvoucher(acheader.getXvoucher());
			if(exist == null) throw new ServiceException("Can't find voucher in this system");

			BeanUtils.copyProperties(acheader, exist, "xvoucher","xstatusjv","xtrn", "xtypetrn");
			long count = updateAcheader(exist);
			if(count == 0) throw new ServiceException("Can't update voucher");
		} else {
			acheader.setXstatusjv("Balanced");
			long count = saveAcheader(acheader);
			if(count == 0) throw new ServiceException("Can't create voucher");
		}

		// now save details if exist
		if(acdetails == null || acdetails.isEmpty()) {
			responseHelper.setSuccessStatusAndMessage("Voucher created successfully");
			responseHelper.setRedirectUrl("/account/voucher/" + acheader.getXvoucher());
			return responseHelper.getResponse();
		}

		// delete existings
		List<Acdetail> existingDetails = findAcdetailsByXvoucher(acheader.getXvoucher());
		if(existingDetails != null && !existingDetails.isEmpty()) {
			for(Acdetail acd : existingDetails) {
				long count = deleteAcdetail(acd.getXrow(), acd.getXvoucher());
				if(count == 0) throw new ServiceException("Can't delete existing details");
			}
		}

		for(Acdetail acdetail : acdetails) {
			// set voucher header ref
			acdetail.setXvoucher(acheader.getXvoucher());

			if(StringUtils.isBlank(acdetail.getXacc())) throw new ServiceException("Account required");
			if(acdetail.getXdebit() == null) acdetail.setXdebit(BigDecimal.ZERO);
			if(acdetail.getXcredit() == null) acdetail.setXcredit(BigDecimal.ZERO);
			if(acdetail.getXdebit().compareTo(BigDecimal.ZERO) == -1 || acdetail.getXcredit().compareTo(BigDecimal.ZERO) == -1) {
				throw new ServiceException("Invalid David or Credit amount");
			}
			if(acdetail.getXdebit().equals(BigDecimal.ZERO) && acdetail.getXcredit().equals(BigDecimal.ZERO)) {
				throw new ServiceException("Invalid David and Credit amount");
			}
			if(acdetail.getXdebit().compareTo(BigDecimal.ZERO) == 1 && acdetail.getXcredit().compareTo(BigDecimal.ZERO) == 1) {
				throw new ServiceException("You should enter only David or Credit amount");
			}
			if(acdetail.getXdebit().compareTo(BigDecimal.ZERO) == 1) {
				acdetail.setXprime(acdetail.getXdebit());
			} else {
				acdetail.setXprime(acdetail.getXcredit().multiply(BigDecimal.valueOf(-1)));
			}

			// if existing
			if(acdetail.getXrow() > 0) {
				Acdetail exist = findAcdetailByXrowAndXvoucher(acdetail.getXrow(), acdetail.getXvoucher());
				if(exist == null) throw new ServiceException("Can't find detail to do update");

				BeanUtils.copyProperties(acdetail, exist, "xvoucher", "xrow");
				long count = updateAcdetail(exist);
				if(count == 0) throw new ServiceException("Can't update voucher detail");
			} else {
				long count = saveAcdetail(acdetail);
				if(count == 0) throw new ServiceException("Can't save voucher detail");
			}

		}

		responseHelper.setSuccessStatusAndMessage("Voucher created successfully");
		responseHelper.setRedirectUrl("/account/voucher/" + acheader.getXvoucher());
		return responseHelper.getResponse();
	}



	@Transactional
	@Override
	public long updateAcheader(Acheader acheader) {
		if(acheader == null) return 0;
		acheader.setZid(sessionManager.getBusinessId());
		acheader.setZuuserid(getAuditUser());
		return acMapper.updateAcheader(acheader);
	}

	@Override
	public List<Acheader> getAllAcheader() {
		return acMapper.getAllAcheader(sessionManager.getBusinessId());
	}

	@Override
	public Acheader findAcheaderByXvoucher(String xvoucher) {
		return findAcheaderByXvoucher(xvoucher, sessionManager.getBusinessId());
	}

	@Override
	public Acheader findAcheaderByXvoucher(String xvoucher, String zid) {
		if(StringUtils.isBlank(xvoucher)) return null;
		return acMapper.findAcheaderByXvoucher(xvoucher, zid);
	}

	@Transactional
	@Override
	public long saveAcdetail(Acdetail acdetail) {
		if(acdetail == null) return 0;
		acdetail.setZid(sessionManager.getBusinessId());
		acdetail.setZauserid(getAuditUser());
		long count = acMapper.saveAcdetail(acdetail);
		if(count != 0) {
			count = updateAcheaderXstatusjv(acdetail.getXvoucher());
		}
		return count;
	}

	@Transactional
	@Override
	public long updateAcdetail(Acdetail acdetail) {
		if(acdetail == null) return 0;
		acdetail.setZid(sessionManager.getBusinessId());
		acdetail.setZuuserid(getAuditUser());
		long count = acMapper.updateAcdetail(acdetail);
		if(count != 0) {
			count = updateAcheaderXstatusjv(acdetail.getXvoucher());
		}
		return count;
	}

	@Transactional
	@Override
	public long updateAcheaderXstatusjv(String xvoucher) {
		if(StringUtils.isBlank(xvoucher)) return 0;
		return acMapper.updateAcheaderXstatusjv(xvoucher, sessionManager.getBusinessId());
	}

	@Override
	public List<Acdetail> getAllAcdetail() {
		return acMapper.getAllAcdetail(sessionManager.getBusinessId());
	}

	@Override
	public Acdetail findAcdetailByXrowAndXvoucher(int xrow, String xvoucher) {
		if(xrow == 0 || StringUtils.isBlank(xvoucher)) return null;
		return acMapper.findAcdetailByXrowAndXvoucher(xrow, xvoucher, sessionManager.getBusinessId());
	}

	@Override
	public List<Acdetail> findAcdetailsByXvoucher(String xvoucher) {
		if(StringUtils.isBlank(xvoucher)) return Collections.emptyList();
		return acMapper.findAcdetailsByXvoucher(xvoucher, sessionManager.getBusinessId());
	}

	@Override
	public void procAcVoucherPost(Integer xyear, Integer xper, String xfvoucher, String xtvoucher) {
		acMapper.procAcVoucherPost(sessionManager.getBusinessId(), getAuditUser(), xyear, xper, xfvoucher, xtvoucher);
	}

	@Override
	public void procAcVoucherUnPost(Integer xyear, Integer xper, String xfvoucher, String xtvoucher) {
		acMapper.procAcVoucherUnPost(sessionManager.getBusinessId(), getAuditUser(), xyear, xper, xfvoucher, xtvoucher);
	}

	@Transactional
	@Override
	public long deleteAcheader(String xvoucher) {
		if(StringUtils.isBlank(xvoucher)) return 0;
		return acMapper.deleteAcheader(xvoucher, sessionManager.getBusinessId());
	}

	@Transactional
	@Override
	public long deleteAcdetail(int xrow, String xvoucher) {
		if(xrow == 0 || StringUtils.isBlank(xvoucher)) return 0;
		long count = acMapper.deleteAcdetail(xrow, xvoucher, sessionManager.getBusinessId());
		if(count != 0) {
			count = updateAcheaderXstatusjv(xvoucher);
		}
		return count;
	}

	@Override
	public Acheader setXyearAndXper(Acheader acheader, Acdef acdef) {
		if(acheader == null) return acheader;
		if(acdef == null) return acheader;

		Calendar cal = Calendar.getInstance();
		cal.setTime(acheader.getXdate());

		int year = cal.get(Calendar.YEAR);
		int per = 12 + (cal.get(Calendar.MONTH) + 1) - acdef.getXoffset();
		if(per <= 12) {
			year = year - 1;
		} else {
			per = per - 12;
		}

		acheader.setXyear(year);
		acheader.setXper(per);
		return acheader;
	}

	@Override
	public List<Acheader> getAllVoucherNumber(String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		return acMapper.getAllVoucherNumber(hint.toUpperCase(),sessionManager.getBusinessId());	
	}
	
	@Override
	public List<Acheader> getAllStatusNumber(){
		return acMapper.getAllStatusNumber(sessionManager.getBusinessId());
	}
	
	@Override
	public List<Acheader> getAllApproveStatusNumber(){
		return acMapper.getAllApproveStatusNumber(sessionManager.getBusinessId());
	}

	@Override
	public Acheader findReportName(String xvoucher) {
		if( StringUtils.isBlank(xvoucher)) return null;
		return acMapper.findReportName(xvoucher, sessionManager.getBusinessId());
	}
	
	@Override
	public Xtrn getReportName(String xvoucher) {
		if( StringUtils.isBlank(xvoucher)) return null;
		return acMapper.getReportName(xvoucher, sessionManager.getBusinessId());
	}

	@Override
	public List<Map<String, Object>> getExportDataByChunk(long limit, long offset, String zid) {
		return acMapper.getExportDataByChunk(limit, offset, zid);
	}

	@Override
	public List<Acheader> searchVoucher(String hint) {
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		return acMapper.searchVoucher(hint.toUpperCase(), sessionManager.getBusinessId());
	}

	
}
