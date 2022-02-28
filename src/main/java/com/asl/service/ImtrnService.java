package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Imtrn;

@Component
public interface ImtrnService {

	public long save(Imtrn imtrn);

	public long save(List<Imtrn> imtrn);

	public long update(Imtrn imtrn);

	public Imtrn findImtrnByXimtrnnum(String ximtrnnum);

	public List<Imtrn> getAllImtrn();
	
	public List<Imtrn> getAllImtrnlist(String xtype);

	public long deleteByXimtrnnum(String ximtrnnum);
}
