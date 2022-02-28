package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Cacus;
import com.asl.entity.Zbusiness;

/**
 * @author Zubayer Ahamed
 * @since Feb 17, 2021
 */
@Mapper
public interface ZbusinessMapper {

	public long save(Zbusiness zbusiness);
	public long update(Zbusiness zbusiness);

	Zbusiness findByZid(String zid);
	
	Zbusiness findfromZid(String zid);

	public List<Zbusiness> getAllBranchBusiness(String zid);
	
	public List<Zbusiness> getBranchBusiness(String zid);
	
	public List<Cacus> getBusinessName(String hint, String zid);
	
	public Zbusiness getCentralBusiness(String centralzid);
}
