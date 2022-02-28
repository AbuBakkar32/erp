package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Mobomdetail;
import com.asl.entity.Mobomheader;

@Mapper
public interface MobomheaderMapper {

	// For Header
	public long saveMobomheader(Mobomheader mobomheader);

	public long updateMobomheader(Mobomheader mobomheader);

	public long deleteMobomheader(String xbomkey, String zid);

	public List<Mobomheader> getAllMobomheader(String zid);

	public Mobomheader findMobomheaderByXbomkey(String xbomkey, String zid);

	// For Detail
	public long saveMobomdetail(Mobomdetail mobomdetail);

	public long updateMobomdetail(Mobomdetail mobomdetail);

	public long deleteMobomdetail(int xrow, String xbomkey, String zid);

	public List<Mobomdetail> getAllMobomdetail(String zid);

	public Mobomdetail findMobomdetailByXrowAndXbomkey(int xrow, String xbomkey, String zid);

	public List<Mobomdetail> findMobomdetailsByXbomkey(String xbomkey, String zid);

}
