package com.asl.service;

import java.util.List;

import com.asl.entity.Imtogli;
import com.asl.entity.Xtrn;

public interface ImtogliService {
	
	public long saveImtogli(Imtogli imtogli);
	
	public long updateImtogli(Imtogli imtogli);

	public long deleteImtogli(Imtogli imtogli);
	
	public List<Imtogli> getAllImtogli(String xtrn);
	
	public Imtogli findByXtrnAndXgitem(String xtrn, String xgitem);
	
	public List<Xtrn> getxtrn();

}
