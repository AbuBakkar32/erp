package com.asl.service;

import java.util.List;

import com.asl.entity.Optoari;

public interface OptoariService {
	
	public long save(Optoari optoari);
	
	public long update(Optoari optoari);

	public long delete(Optoari optoari);
	
	public List<Optoari> getAllOptoari();
	
	public Optoari findByXtypeXgcusAndXgitem(String xtype, String xgcus,String xgitem);

}
