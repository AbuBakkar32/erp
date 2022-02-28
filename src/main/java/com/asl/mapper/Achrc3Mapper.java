package com.asl.mapper;

import java.util.List;

import com.asl.entity.Achrc3;

public interface Achrc3Mapper {
	public long saveAchrc3(Achrc3 Achrc3);
	public long updateAchrc3(Achrc3 Achrc3);
	
	public List<Achrc3> getAllAchrc3(String zid);
}
