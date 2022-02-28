package com.asl.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface AcyearendMapper {

	public void acYearEnd(String zid, String xuser, int xyear, Date xdate, String p_seq);
}
