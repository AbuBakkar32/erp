package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Caproject;
import com.asl.entity.Cawhfact;
import com.asl.entity.Pdmst;


@Mapper
public interface CawhfactMapper {
	
	public long saveTask(Cawhfact cawhfact);
	
	public long updateTask(Cawhfact cawhfact);
	
	public long deleteTask(Cawhfact cawhfact);
	
	public List<Cawhfact> getAllTask(String zid);
	
	public List<Cawhfact> getAllTasksByProject(String xproject, String zid);
	
	public Cawhfact findByTaskId(String xtrnnum, String zid);
	
	//Search	
	public List<Cawhfact> searchDependsOn(String hint, String zid);

}
