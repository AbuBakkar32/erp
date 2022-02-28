package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Lcamend;
import com.asl.entity.Lcinfo;

@Component
public interface LcinfoService {

	public long saveLcinfo(Lcinfo lcinfo);

	public long updateLcinfo(Lcinfo lcinfo);

	public long deleteLcinfo(Lcinfo lcinfo);

	public List<Lcinfo> getAllLcinfo();

	public Lcinfo findByLcinfo(String xlcno);

	// for LC Ammendment Detail

	public long saveLcamend(Lcamend lcamend);

	public long updateLcamend(Lcamend lcamend);

	public long deleteLcamend(Lcamend lcamend);

	public List<Lcamend> getAllLcamend();

	public List<Lcamend> findByLcamend(String xlcno);

	public Lcamend findLcamendByXlcnoAndXrow(String xlcno, int xrow);

	public List<Lcinfo> searchLcNo(String hint);

}
