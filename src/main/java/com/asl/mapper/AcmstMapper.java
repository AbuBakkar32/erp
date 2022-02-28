package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Acmst;

@Mapper
public interface AcmstMapper {

	public long save(Acmst acmst);

	public long update(Acmst acmst);

	public long delete(String xacc, String zid);

	public List<Acmst> getAllAcmst(String zid);

	public Acmst findByXacc(String xacc, String zid);
	
	//For report Search 
	public List<Acmst> getAllAcc(String hint, String zid);
	
	public List<Acmst> searchByXaccORXdesc(String hint, String zid);
	
	public List<Acmst> getAllSub(String hint, String zid);
}
