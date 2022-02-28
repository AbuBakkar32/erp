package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Lcamend;
import com.asl.entity.Lcinfo;

@Mapper
public interface LcinfoMapper {

	public long saveLcinfo(Lcinfo lcinfo);

	public long updateLcinfo(Lcinfo lcinfo);

	public long deleteLcinfo(Lcinfo lcinfo);

	public List<Lcinfo> getAllLcinfo(String zid);

	public Lcinfo findByLcinfo(String xlcno, String zid);

	// for LC Ammendment Detail

	public long saveLcamend(Lcamend lcamend);

	public long updateLcamend(Lcamend lcamend);

	public long deleteLcamend(Lcamend lcamend);

	public List<Lcamend> getAllLcamend(String zid);

	public List<Lcamend> findByLcamend(String xlcno, String zid);

	public Lcamend findLcamendByXlcnoAndXrow(String xlcno, int xrow, String zid);

	public List<Lcinfo> searchLcNo(String hint, String zid);

}
