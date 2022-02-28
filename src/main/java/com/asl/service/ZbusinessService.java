package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Cacus;
import com.asl.entity.Zbusiness;

/**
 * @author Zubayer Ahamed
 * @since Dec 30, 2020
 */
@Component
public interface ZbusinessService {

	public long save(Zbusiness zbusiness);

	public long update(Zbusiness zbusiness);

	public Zbusiness findBById(String zid);
	
	public List<Cacus> getBusinessName(String hint);
	
	public Zbusiness findfromZid();

	public List<Zbusiness> getAllBranchBusiness();
	
	public List<Zbusiness> getBranchBusiness();

	public Zbusiness getCentralBusiness();
}
