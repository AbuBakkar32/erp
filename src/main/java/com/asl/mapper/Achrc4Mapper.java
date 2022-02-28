package com.asl.mapper;

import java.util.List;

import com.asl.entity.Achrc4;

public interface Achrc4Mapper {
	public long saveAchrc4(Achrc4 achrc4);
	public long updateAchrc4(Achrc4 achrc4);
	
	public List<Achrc4> getAllAchrc4(String zid);
}
