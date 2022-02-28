package com.asl.mapper;

import java.util.List;

import com.asl.entity.Achrc5;

public interface Achrc5Mapper {
	public long saveAchrc5(Achrc5 achrc5);
	public long updateAchrc5(Achrc5 achrc5);
	
	public List<Achrc5> getAllAchrc5(String zid);
}
