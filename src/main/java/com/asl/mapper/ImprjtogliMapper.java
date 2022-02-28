package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Imprjtogli;

@Mapper
public interface ImprjtogliMapper {
	
	public long saveImprjtogli(Imprjtogli imprjtogli);
	
	public long updateImprjtogli(Imprjtogli imprjtogli);
	
	public long deleteImprjtogli(Imprjtogli imprjtogli);
	
	public List<Imprjtogli> getAllImprjtogli(String zid);
	
	public Imprjtogli findByXgitem(String zid,String xgitem);

}
 