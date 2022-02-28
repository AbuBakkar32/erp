package com.asl.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.model.ProductionSuggestion;

/**
 * @author Zubayer Ahamed
 * @since Mar 10, 2021
 */
@Component
public interface ProductionSuggestionService {

	public void createSuggestion(String xordernum);

	public List<ProductionSuggestion> getProductionSuggestionByChalan(String chalan);

	public List<ProductionSuggestion> getProductionSuggestion(String chalan, Date xdate);

	public long deleteSuggestion(String chalan, Date xdate);

	public List<String> searchClananNumbers(String chalan);

	public List<ProductionSuggestion> getProductionSuggestionByXitemAndChalan(String chalan, String xitem);
}
