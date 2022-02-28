package com.asl.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Caitem;
import com.asl.entity.POSPromotion;
import com.asl.mapper.POSPromotionMapper;
import com.asl.service.POSPromotionService;;

@Service
public class POSPromotionServiceImpl extends AbstractGenericService implements POSPromotionService {

	@Autowired
	private POSPromotionMapper pOSPromotionMapper;

	@Transactional
	@Override
	public long save(POSPromotion pOSPromotion) {
		if (pOSPromotion == null)
			return 0;
		pOSPromotion.setZid(sessionManager.getBusinessId());
		pOSPromotion.setZauserid(getAuditUser());
		return pOSPromotionMapper.savePOSPromotion(pOSPromotion);
	}

	@Transactional
	@Override
	public long update(POSPromotion pOSPromotion) {
		if (pOSPromotion == null)
			return 0;
		pOSPromotion.setZid(sessionManager.getBusinessId());
		pOSPromotion.setZuuserid(getAuditUser());
		return pOSPromotionMapper.updatePOSPromotion(pOSPromotion);
	}

	@Override
	public List<POSPromotion> getAllPOSPromotion() {
		return pOSPromotionMapper.getAllPOSPromotion(sessionManager.getBusinessId());
	}

	@Override
	public POSPromotion findByPOSPromotion(String xpromono) {
		if (StringUtils.isBlank(xpromono))
			return null;
		return pOSPromotionMapper.findByPOSPromotion(xpromono, sessionManager.getBusinessId());
	}

}
