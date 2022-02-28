package com.asl.mapper;

import java.util.List;

import com.asl.entity.Aralc;

public interface AralcMapper {
	public long saveAralc(Aralc aralc);
	public long updateAralc(Aralc aralc);
	
	public List<Aralc> getAllAralc(String zid);

}
