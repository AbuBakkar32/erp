package com.asl.service;

import java.util.List;

import com.asl.entity.Acdetail;

public interface AcgroupService {
	
	public long save(Acdetail acdetail);
	public long update(Acdetail acdetail);
	
	public List<Acdetail> getAllAcdetail();
}
