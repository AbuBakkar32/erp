package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Cawh;
import com.asl.entity.Cawhcostest;
@Component
public interface CawhService {
	
	public long saveCawh(Cawh cawh);
	
	public long updateCawh(Cawh cawh);

	public long deleteCawh(Cawh cawh);
	
	public List<Cawh> getAllCawh();

	public Cawh findByCawh(String xwh);

	public List<Cawh> getCawhBYProject(String xproject);

	public List<Cawh> searchSite(String xwh);

	//for Estimated Cost Details
	public long saveCawhcostest(Cawhcostest cawhcostest);

	public long updateCawhcostest(Cawhcostest cawhcostest);
	
	public long deleteCawhcostest(Cawhcostest cawhcostest);

	public List<Cawhcostest> getAllCawhcostest();
	
	public List<Cawhcostest> findByCawhcostest(String xwh);
	
	public Cawhcostest findCawhcostestByXwhAndXrow(String xwh,int xrow);
	
	public Cawhcostest findCawhcostestByXwhAndXitem(String xwh, String xitem);
	
	public long updateCawhTotalAmt(String xwh);
}
