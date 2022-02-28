package com.asl.service;

import java.util.List;

import com.asl.entity.Caitem;
import com.asl.entity.POSPromotion;

public interface POSPromotionService {

	public long save(POSPromotion pOSPromotionMapper);

	public long update(POSPromotion pOSPromotionMapper);

	public List<POSPromotion> getAllPOSPromotion();

	public POSPromotion findByPOSPromotion(String xpromono);

}
