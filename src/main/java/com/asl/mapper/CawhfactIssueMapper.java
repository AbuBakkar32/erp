package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Cawhfact;

@Mapper
public interface CawhfactIssueMapper {
	
	public long saveIssue(Cawhfact cawhfact);
	
	public long updateIssue(Cawhfact cawhfact);
	
	public List<Cawhfact> getAllIssue(String zid);
	
	public Cawhfact findByIssueId(String xtrnnum, String zid);
	
	public long deleteIssue(Cawhfact cawhfact);
	
	
	//Search	
	public List<Cawhfact> searchDependsOn(String hint, String zid);

}
