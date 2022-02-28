package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Cawhfact;

@Component
public interface CawhfactService {

	public long save(Cawhfact cawhfact);

	public long update(Cawhfact cawhfact);
	
	public long delete(Cawhfact cawhfact);

	public List<Cawhfact> getAllTask();
	
	public List<Cawhfact> getAllTasksByProject(String xproject);

	public Cawhfact findByTaskId(String xtrnnum);

	// Search
	public List<Cawhfact> searchDependsOn(String hint);

}
