package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Xtrnp;

@Component
public interface XtrnpService {
	public long save(Xtrnp xtrnp);
	public long update(Xtrnp xtrnp);
	public long delete(Xtrnp xtrnp);
	
	public Xtrnp findXtrnpByXvoucher(String xtypetrn, String xtrn, String xtyperel);
	public List<Xtrnp> getAllXtrnp();
}
