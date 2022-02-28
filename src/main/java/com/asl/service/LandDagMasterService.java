package com.asl.service;

import java.util.List;

import com.asl.entity.Cadag;
import com.asl.entity.LandDagDetails;

public interface LandDagMasterService {

	public long saveLandDagMaster(Cadag landDagMaster);
	
	public long updateLandDagMaster(Cadag landDagMaster);

	public long deleteLandDagMaster(Cadag landDagMaster);
	
	public List<Cadag> getAllLandDagMaster();

	public Cadag findbyXdagtypeAndxdagid(String xdagtype, int xdagid);
	
	public Cadag findByXdagnumAndXdagtype(String xdagtype, int xdagnum);
	
	public Cadag findbyXdagid( int xdagid);
	
	public List<Cadag> getDagNo(String xdagtype);
}
