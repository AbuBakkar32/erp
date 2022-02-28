package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Cawhbudget;
import com.asl.entity.Cawhbudgetdt;

@Component
public interface CawhbudgetService {

	public long saveCawhbudget(Cawhbudget cawhbudget);
	
	public long updateCawhbudget(Cawhbudget cawhbudget);

	public long deleteCawhbudget(Cawhbudget cawhbudget);

	public long updateCawhbudgetTotalAmt(Cawhbudgetdt cawhbudgetdt);

	public List<Cawhbudget> getAllCawhbudget();

	public Cawhbudget findByCawhbudget(String xbudget);

	public List<Cawhbudget> getAllCawhbudgetByXwh(String xwh);

	//for Budget Details
	public long saveCawhbudgetdt(Cawhbudgetdt cawhbudgetdt);
	
	public long updateCawhbudgetdt(Cawhbudgetdt cawhbudgetdt);
	
	public long deleteCawhbudgetdt(Cawhbudgetdt cawhbudgetdt);
	
	public List<Cawhbudgetdt> getAllCawhbudgetdt();
	
	public List<Cawhbudgetdt> findByCawhbudgetdt(String xbudget);
	
	public Cawhbudgetdt findCawhbudgetdtByXbudgetAndXrow(String xbudget, int xrow);
	
	// Procedure Calls
	public void procIM_confirmProjectBudget(String xbudget, String p_seq);
}
