package com.asl.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ConsumptionMapper {

	public void IM_2GL_Transfer(String zid, String xuser, Date xstartdate, Date xdatexenddate, Date xdate, String xwh, String xtrn, String p_seq);
}
