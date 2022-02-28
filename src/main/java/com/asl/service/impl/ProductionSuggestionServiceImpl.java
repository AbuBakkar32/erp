package com.asl.service.impl;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.asl.mapper.ProductionSuggestionMapper;
import com.asl.model.ProductionSuggestion;
import com.asl.service.ProductionSuggestionService;

/**
 * @author Zubayer Ahamed
 * @since Mar 10, 2021
 */
@Service
public class ProductionSuggestionServiceImpl extends AbstractGenericService implements ProductionSuggestionService{

	@Autowired private ProductionSuggestionMapper productionSuggestionMapper;

	@Override
	public List<ProductionSuggestion> getProductionSuggestionByChalan(String chalan) {
		if(StringUtils.isBlank(chalan)) return Collections.emptyList();
		return productionSuggestionMapper.getProductionSuggestionByChalan(chalan, sessionManager.getBusinessId());
	}

	@Override
	public List<ProductionSuggestion> getProductionSuggestion(String chalan, Date xdate) {
		if(StringUtils.isBlank(chalan)) return Collections.emptyList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = null;
		if(xdate != null) {
			date = sdf.format(xdate);
		}
		return productionSuggestionMapper.getProductionSuggestion(chalan, date, sessionManager.getBusinessId());
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void createSuggestion(String xordernum) {
		productionSuggestionMapper.createSuggestion(xordernum, sessionManager.getBusinessId());
	}

	@Override
	public long deleteSuggestion(String chalan, Date xdate) {
		if(StringUtils.isBlank(chalan) || xdate == null) return 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return productionSuggestionMapper.deleteSuggestion(chalan, sdf.format(xdate), sessionManager.getBusinessId());
	}

	@Override
	public List<String> searchClananNumbers(String chalan) {
		if(StringUtils.isBlank(chalan)) return Collections.emptyList();
		return productionSuggestionMapper.searchClananNumbers(chalan.toUpperCase(), sessionManager.getBusinessId());
	}

	@Override
	public List<ProductionSuggestion> getProductionSuggestionByXitemAndChalan(String chalan, String xitem) {
		if(StringUtils.isBlank(chalan) || StringUtils.isBlank(xitem)) return null;
		return productionSuggestionMapper.getProductionSuggestionByXitemAndChalan(chalan, xitem, sessionManager.getBusinessId());
	}

	
}
