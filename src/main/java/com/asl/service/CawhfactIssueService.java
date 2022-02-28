package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Cawhfact;

@Component
public interface CawhfactIssueService {
	
	public long save(Cawhfact cawhfact);

	public long update(Cawhfact cawhfact);

	public List<Cawhfact> getAllIssue();

	public Cawhfact findByIssueId(String xtrnnum);

	public long delete(Cawhfact cawhfact);

	// Search
	public List<Cawhfact> searchDependsOn(String hint);


}
