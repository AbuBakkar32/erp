package com.asl.service.impl;

import java.math.BigDecimal;
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
import com.asl.entity.Mobomdetail;
import com.asl.entity.Mobomheader;
import com.asl.mapper.MobomheaderMapper;
import com.asl.model.ResponseHelper;
import com.asl.model.ServiceException;
import com.asl.service.MobomheaderService;
@Service
public class MobomheaderServiceImpl  extends AbstractGenericService implements MobomheaderService{

	@Autowired private MobomheaderMapper  mobomheaderMapper;

	@Transactional
	@Override
	public long saveMobomheader(Mobomheader mobomheader) {
		if(mobomheader == null) return 0;
		mobomheader.setZid(sessionManager.getBusinessId());
		mobomheader.setZauserid(getAuditUser());
		return mobomheaderMapper.saveMobomheader(mobomheader);
	}

	@Override
	public Map<String, Object> saveWithDetail(Mobomheader mobomheader, List<Mobomdetail> mobomdetails,
			ResponseHelper responseHelper) throws ServiceException {
		if(mobomheader == null) throw new ServiceException("Data not found to save");

		// validate
		//mobomheader.setXtrn("BOM-");
//		if(mobomheader.getXitem() == null) throw new ServiceException("Item required");
//		if(mobomheader.getXqty().compareTo(BigDecimal.ZERO) == 0) throw new ServiceException("Qty must be greater than 0");
//		if(mobomheader.getXdesc() == null) throw new ServiceException("Desciption required");

		// save or update Mobomheader first
		// if existing
		if(StringUtils.isNotBlank(mobomheader.getXbomkey())) {
			Mobomheader exist = findMobomheaderByXbomkey(mobomheader.getXbomkey());
			if(exist == null) throw new ServiceException("Can't find bom in this system");

			BeanUtils.copyProperties(mobomheader, exist, "xbomkey","xtrn");
			long count = updateMobomheader(exist);
			if(count == 0) throw new ServiceException("Can't update BOM");
			else responseHelper.setSuccessStatusAndMessage("BOM updated successfully");
			
		} else {
			long count = saveMobomheader(mobomheader);
			if(count == 0) throw new ServiceException("Can't create BOM");
			else responseHelper.setSuccessStatusAndMessage("BOM created successfully");
		}

		// now save details if exist
		if(mobomdetails == null || mobomdetails.isEmpty()) {
//			responseHelper.setSuccessStatusAndMessage("BOM created successfully");
			responseHelper.setRedirectUrl("/production/mobomheader/" + mobomheader.getXbomkey());
			return responseHelper.getResponse();
		}

		// delete existings
		List<Mobomdetail> existingDetails = findMobomdetailsByXbomkey(mobomheader.getXbomkey());
		if(existingDetails != null && !existingDetails.isEmpty()) {
			for(Mobomdetail acd : existingDetails) {
				long count = deleteMobomdetail(acd.getXrow(), acd.getXbomkey());
				if(count == 0) throw new ServiceException("Can't delete existing details");
			}
		}

		for(Mobomdetail mobomdetail : mobomdetails) {
			// set BOM header ref
			mobomdetail.setXbomkey(mobomheader.getXbomkey());
//			mobomdetail.setXsign(-1);
//			mobomdetail.setXtype("BOM");

			if(StringUtils.isBlank(mobomdetail.getXitem())) throw new ServiceException("Item required");
			if(mobomdetail.getXqty().compareTo(BigDecimal.ZERO) == 0) {
				throw new ServiceException("Qty must be greater than 0");
			}

			// if existing
			if(mobomdetail.getXrow() > 0) {
				Mobomdetail exist = findMobomdetailByXrowAndXbomkey(mobomdetail.getXrow(), mobomdetail.getXbomkey());
				if(exist == null) throw new ServiceException("Can't find detail to do update");

				BeanUtils.copyProperties(mobomdetail, exist, "xbomkey", "xrow");
				long count = updateMobomdetail(exist);
				if(count == 0) throw new ServiceException("Can't update BOM detail");
			} else {
				long count = saveMobomdetail(mobomdetail);
				if(count == 0) throw new ServiceException("Can't save BOM detail");
			}

		}

//		responseHelper.setSuccessStatusAndMessage("BOM created successfully");
		responseHelper.setRedirectUrl("/production/mobomheader/" + mobomheader.getXbomkey());
		return responseHelper.getResponse();
	}

	@Transactional
	@Override
	public long updateMobomheader(Mobomheader mobomheader) { 
		if(mobomheader == null) return 0;
		mobomheader.setZid(sessionManager.getBusinessId());
		mobomheader.setZauserid(getAuditUser());
		return mobomheaderMapper.updateMobomheader(mobomheader);
	}

	@Transactional
	@Override
	public long deleteMobomheader(String xbomkey) {
		if(StringUtils.isBlank(xbomkey)) return 0;
		return mobomheaderMapper.deleteMobomheader(xbomkey, sessionManager.getBusinessId());
	}

	@Override
	public List<Mobomheader> getAllMobomheader() {
		return mobomheaderMapper.getAllMobomheader(sessionManager.getBusinessId());
	}

	@Override
	public Mobomheader findMobomheaderByXbomkey(String xbomkey) {
		return mobomheaderMapper.findMobomheaderByXbomkey(xbomkey,sessionManager.getBusinessId());
	}

	//Detail
	
	@Transactional
	@Override
	public long saveMobomdetail(Mobomdetail mobomdetail) {
		if(mobomdetail == null) return 0;
		mobomdetail.setZid(sessionManager.getBusinessId());
		mobomdetail.setZauserid(getAuditUser());
		return mobomheaderMapper.saveMobomdetail(mobomdetail);
	}

	@Transactional
	@Override
	public long updateMobomdetail(Mobomdetail mobomdetail) {
		if(mobomdetail == null) return 0;
		mobomdetail.setZid(sessionManager.getBusinessId());
		mobomdetail.setZauserid(getAuditUser());
		return mobomheaderMapper.updateMobomdetail(mobomdetail);
	}

	@Transactional
	@Override
	public long deleteMobomdetail(int xrow, String xbomkey) {
		if(xrow == 0 || StringUtils.isBlank(xbomkey)) return 0;
		return mobomheaderMapper.deleteMobomdetail(xrow, xbomkey, sessionManager.getBusinessId());
	}

	@Override
	public List<Mobomdetail> getAllMobomdetail() {
		return mobomheaderMapper.getAllMobomdetail(sessionManager.getBusinessId());
	}

	@Override
	public Mobomdetail findMobomdetailByXrowAndXbomkey(int xrow, String xbomkey) {
		if(xrow == 0 || StringUtils.isBlank(xbomkey)) return null;
		return mobomheaderMapper.findMobomdetailByXrowAndXbomkey(xrow, xbomkey, sessionManager.getBusinessId());
	}

	@Override
	public List<Mobomdetail> findMobomdetailsByXbomkey(String xbomkey) {
		if(StringUtils.isBlank(xbomkey)) return null;
		return mobomheaderMapper.findMobomdetailsByXbomkey(xbomkey, sessionManager.getBusinessId());
	}


}
