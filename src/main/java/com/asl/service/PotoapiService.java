package com.asl.service;

import java.util.List;

import com.asl.entity.Potoapi;

public interface PotoapiService {
	
	public long savePotoapi(Potoapi potoapi);
	
	public long updatePotoapi(Potoapi potoapi);

	public long deletePotoapi(Potoapi potoapi);
	
	public List<Potoapi> getAllPotoapi();
	
	public Potoapi findByXtypeXgsupAndXgitem(String xtype, String xgsup,String xgitem);

}
