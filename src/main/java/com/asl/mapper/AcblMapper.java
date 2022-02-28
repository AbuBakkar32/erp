package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.asl.entity.Acbl;

@Mapper
public interface AcblMapper {
	
	public long saveAcbl(Acbl acbl);
	public long updateAcbl(Acbl acbl);
	
	public List<Acbl> getAllAcbl(String zid);
}

