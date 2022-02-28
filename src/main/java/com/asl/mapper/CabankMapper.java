package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Cabank;
import com.asl.entity.LandPerson;
import com.asl.entity.Pdmst;

@Mapper
public interface CabankMapper {

	public long save(Cabank cabank);

	public long update(Cabank cabank);

	public long delete(String xbank, String zid);

	public Cabank findCaBankByXbank(String xbank, String zid);

	public List<Cabank> getAllCaBank(String zid);
	
	public List<Cabank> searchBank(String xbank, String zid);
	
	
}
