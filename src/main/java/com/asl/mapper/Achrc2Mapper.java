package com.asl.mapper;

import java.util.List;

import com.asl.entity.Achrc2;

public interface Achrc2Mapper {
	public long saveAchrc2(Achrc2 Achrc2);
	public long updateAchrc2(Achrc2 Achrc2);
	
	public List<Achrc2> getAllAchrc2(String zid);
}
