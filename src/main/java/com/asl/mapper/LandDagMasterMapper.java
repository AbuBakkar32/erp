package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.LandDagDetails;
import com.asl.entity.LandOwner;
import com.asl.entity.Cadag;;

@Mapper
public interface LandDagMasterMapper {

	public long saveLandDagMaster(Cadag landDagMaster);
	
	public long updateLandDagMaster(Cadag landDagMaster);

	public long deleteLandDagMaster(Cadag landDagMaster);
	
	public List<Cadag> getAllLandDagMaster(String zid);
	
	public Cadag findbyXdagtypeAndxdagid(String xdagtype, int xdagid, String zid);
	 
	public Cadag findByXdagnumAndXdagtype(String xdagtype, int xdagnum, String zid);
	
	public Cadag findbyXdagid( int xdagid, String zid);
	
	public List<Cadag> getDagNo(String xdagtype, String zid);
}
