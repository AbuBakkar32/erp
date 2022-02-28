package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Cawhbudget;
import com.asl.entity.Cawhbudgetdt;
import com.asl.entity.PoordDetail;
@Mapper
public interface CawhbudgetMapper {

	public long saveCawhbudget(Cawhbudget cawhbudget);
	
	public long updateCawhbudget(Cawhbudget cawhbudget);

	public long updateCawhbudgetTotalAmt(Cawhbudgetdt cawhbudgetdt);

	public long deleteCawhbudget(Cawhbudget cawhbudget);
	
	public List<Cawhbudget> getAllCawhbudget(String zid);

	public Cawhbudget findByCawhbudget(String xbudget, String zid);

	public List<Cawhbudget> getAllCawhbudgetByXwh(String xwh, String zid);

	//for Budget Details
	public long saveCawhbudgetdt(Cawhbudgetdt cawhbudgetdt);
	
	public long updateCawhbudgetdt(Cawhbudgetdt cawhbudgetdt);
	
	public long deleteCawhbudgetdt(Cawhbudgetdt cawhbudgetdt);
	
	public List<Cawhbudgetdt> getAllCawhbudgetdt(String zid);
	
	public List<Cawhbudgetdt> findByCawhbudgetdt(String xbudget, String zid);
	
	public Cawhbudgetdt findCawhbudgetdtByXbudgetAndXrow(String xbudget, int xrow, String zid);

	// Procedure Calls
	public void procIM_confirmProjectBudget(String zid, String user, String xbudget, String p_seq);
}
