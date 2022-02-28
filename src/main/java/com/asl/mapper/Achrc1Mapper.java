package com.asl.mapper;

import java.util.List;

import com.asl.entity.Achrc1;

public interface Achrc1Mapper {
	public long saveAchrc1(Achrc1 Achrc1);
	public long updateAchrc1(Achrc1 Achrc1);
	
	public List<Achrc1> getAllAchrc1(String zid);
}
