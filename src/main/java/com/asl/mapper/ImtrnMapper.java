package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Imtrn;

@Mapper
public interface ImtrnMapper {

	public long saveImtrn(Imtrn imtrn);

	public long updateImtrn(Imtrn imtrn);

	public Imtrn findImtrnByXimtrnnum(String ximtrnnum, String zid);

	public List<Imtrn> getAllImtrn(String zid);
	
	public List<Imtrn> getAllImtrnlist(String xtype,String zid);

	public long deleteByXimtrnnum(String ximtrnnum, String zid);
}
