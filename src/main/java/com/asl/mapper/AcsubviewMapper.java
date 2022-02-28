package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Acsubview;

/**
 * @author Zubayer Ahamed
 * @since Jul 25, 2021
 */
@Mapper
public interface AcsubviewMapper {

	public List<Acsubview> findAllSubAccount(String zid);

	public List<Acsubview> findSubAccountByXacc(String xacc, String zid);
	
	public List<Acsubview> findSubAccountByXbank(String xbank, String zid);
}
