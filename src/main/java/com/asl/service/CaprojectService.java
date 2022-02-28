package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Caproject;
import com.asl.entity.Caprojectplan;
@Component
public interface CaprojectService {
	
	public long saveCaproject(Caproject caproject);
	
	public long updateCaproject(Caproject caproject);

	public long deleteCaproject(Caproject caproject);
	
	public List<Caproject> getAllCaproject();

	public Caproject findByCaproject(String xproject);

	public List<Caproject> searchProjectFromCaproject(String xproject);

	//for Project Planning Details
	public long saveCaprojectplan(Caprojectplan caprojectplan);

	public long updateCaprojectplan(Caprojectplan caprojectplan);

	public long deleteCaprojectplan(Caprojectplan caprojectplan);

	public List<Caprojectplan> getAllCaprojectplan();

	public List<Caprojectplan> findByCaprojectplan(String xproject);

	public Caprojectplan findCaprojectplanByXprojectAndXrow(String xproject,int xrow);

}
