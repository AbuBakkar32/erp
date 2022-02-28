package com.asl.service;

import java.util.List;

import com.asl.entity.Imprjtogli;

public interface ImprjtogliService {
	
	
	public long saveImprjtogli(Imprjtogli imprjtogli);
	
	public long updateImprjtogli(Imprjtogli imprjtogli);
	
	public long deleteImprjtogli(Imprjtogli imprjtogli);
	
	public List<Imprjtogli> getAllImprjtogli();
	
	public Imprjtogli findByXgitem(String xgitem);

}
