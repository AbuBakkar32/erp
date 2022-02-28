package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Potoapi;

@Mapper
public interface PotoapiMapper {

	public long savePotoapi(Potoapi potoapi);
	
	public long updatePotoapi(Potoapi potoapi);

	public long deletePotoapi(Potoapi potoapi);
	
	public List<Potoapi> getAllPotoapi(String zid);
	
	public Potoapi findByXtypeXgsupAndXgitem(String xtype, String xgsup,String xgitem, String zid);
}
