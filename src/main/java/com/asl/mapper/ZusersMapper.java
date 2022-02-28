package com.asl.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Zusers;

@Mapper
public interface ZusersMapper {

	public long save(Zusers zusers);
	public long update(Zusers zusers);
	
	public List<Zusers> getAllZusers(String userid);
}
