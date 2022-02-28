package com.asl.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Acdef;

@Mapper
public interface AcdefMapper {

	public long save(Acdef acdef);

	public long update(Acdef acdef);

	public Acdef find(String zid);

}
