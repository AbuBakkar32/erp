package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Zaudittrail;

@Mapper
public interface ZaudittrailMapper {

	public long save(Zaudittrail zaudittrail);
	public long update(Zaudittrail zaudittrail);
	
	public List<Zaudittrail> getAllZaudittrail(String zauditid);
}
