package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Caproject;
import com.asl.entity.Caprojectplan;
import com.asl.entity.Cawh;
import com.asl.entity.Cawhcostest;
import com.asl.entity.Xcodes;
@Mapper
public interface CaprojectMapper {

	public long saveCaproject(Caproject caproject);

	public long updateCaproject(Caproject caproject);

	public long deleteCaproject(Caproject caproject);

	public List<Caproject> getAllCaproject(String zid);

	public Caproject findByCaproject(String xproject, String zid);

	public List<Caproject> searchProjectFromCaproject(String xproject, String zid);

	//for Project Planning Details
	public long saveCaprojectplan(Caprojectplan caprojectplan);

	public long updateCaprojectplan(Caprojectplan caprojectplan);

	public long deleteCaprojectplan(Caprojectplan caprojectplan);

	public List<Caprojectplan> getAllCaprojectplan(String zid);

	public List<Caprojectplan> findByCaprojectplan(String xproject, String zid);

	public Caprojectplan findCaprojectplanByXprojectAndXrow(String xproject,int xrow, String zid);	
}
