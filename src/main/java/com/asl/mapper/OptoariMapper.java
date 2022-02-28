package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Optoari;
@Mapper
public interface OptoariMapper {
	
	public long saveOptoari(Optoari optoari);
	
	public long updateOptoari(Optoari optoari);

	public long deleteOptoari(Optoari optoari);
	
	public List<Optoari> getAllOptoari(String zid);
	
	public Optoari findByXtypeXgcusAndXgitem(String xtype, String xgcus,String xgitem, String zid);
	

}
