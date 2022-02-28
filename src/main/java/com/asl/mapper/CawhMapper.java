package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Caproject;
import com.asl.entity.Cawh;
import com.asl.entity.Cawhcostest;
import com.asl.entity.LandCommitteeMembers;
import com.asl.entity.Poreqdetail;
@Mapper
public interface CawhMapper {
	
	public long saveCawh(Cawh cawh);
	
	public long updateCawh(Cawh cawh);

	public long deleteCawh(Cawh cawh);
	
	public List<Cawh> getAllCawh(String zid);

	public Cawh findByCawh(String xwh, String zid);

	public List<Cawh> getCawhBYProject(String xproject, String zid);

	public List<Cawh> searchSite(String xwh, String zid);

	//for Estimated Cost Details
	public long saveCawhcostest(Cawhcostest cawhcostest);

	public long updateCawhcostest(Cawhcostest cawhcostest);
	
	public long deleteCawhcostest(Cawhcostest cawhcostest);

	public List<Cawhcostest> getAllCawhcostest(String zid);
	
	public List<Cawhcostest> findByCawhcostest(String xwh, String zid);
	
	public Cawhcostest findCawhcostestByXwhAndXrow(String xwh,int xrow, String zid);	
	
	public Cawhcostest findCawhcostestByXwhAndXitem(String xwh, String xitem, String zid);
	
	public long updateCawhTotalAmt(String xwh, String zid);

}
