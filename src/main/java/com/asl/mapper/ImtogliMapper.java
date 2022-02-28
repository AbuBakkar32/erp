package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Imtogli;
import com.asl.entity.Xtrn;

@Mapper
public interface ImtogliMapper {

	public long saveImtogli(Imtogli imtogli);
	
	public long updateImtogli(Imtogli imtogli);

	public long deleteImtogli(Imtogli imtogli);
	
	public List<Imtogli> getAllImtogli(String xtrn, String zid);
	
	public Imtogli findByXtrnAndXgitem(String xtrn, String xgitem, String zid);
	
	public List<Xtrn> getxtrn(String zid);
}
