package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.POSPromotion;
import com.asl.entity.Caitem;

@Mapper
public interface POSPromotionMapper {

	public long savePOSPromotion(POSPromotion pOSPromotionMapper);
	
	public long updatePOSPromotion(POSPromotion pOSPromotionMapper);

	public List<POSPromotion> getAllPOSPromotion(String zid);

	public POSPromotion findByPOSPromotion(String xpromono, String zid);

}
