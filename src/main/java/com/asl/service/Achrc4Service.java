package com.asl.service;

import java.util.List;

import com.asl.entity.Achrc4;

public interface Achrc4Service {
	
	public long save(Achrc4 achrc4);
	public long update(Achrc4 achrc4);
	
	public List<Achrc4> getAllAchrc4();

}
