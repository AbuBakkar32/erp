package com.asl.service;

import java.util.List;

import com.asl.entity.Aralc;

public interface AralcService {
	
	public long save(Aralc aralc);
	public long update(Aralc aralc);
	
	public List<Aralc> getAllAralc();

}
