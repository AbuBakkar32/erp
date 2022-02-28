package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Immofgdetail;

@Mapper
public interface ImmofgdetailMapper {
	public long save(Immofgdetail immofgdetail);
	public long update(Immofgdetail immofgdetail);

	public Immofgdetail findImmofgDetailByXtornumAndXrow(String xtornum, int xrow, String zid);
	public List<Immofgdetail> getAllImmofgDetail(String zid);
}
