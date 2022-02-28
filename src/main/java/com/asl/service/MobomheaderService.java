package com.asl.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.asl.entity.Acdetail;
import com.asl.entity.Acheader;
import com.asl.entity.Mobomdetail;
import com.asl.entity.Mobomheader;
import com.asl.model.ResponseHelper;
import com.asl.model.ServiceException;

@Component
public interface MobomheaderService {

	// For Header
	public long saveMobomheader(Mobomheader mobomheader);

	public long updateMobomheader(Mobomheader mobomheader);

	public Map<String, Object> saveWithDetail(Mobomheader mobomheader, List<Mobomdetail> mobomdetail, ResponseHelper responseHelper) throws ServiceException;


	public long deleteMobomheader(String xbomkey);

	public List<Mobomheader> getAllMobomheader();

	public Mobomheader findMobomheaderByXbomkey(String xbomkey);

	// For Detail
	public long saveMobomdetail(Mobomdetail mobomdetail);

	public long updateMobomdetail(Mobomdetail mobomdetail);

	public long deleteMobomdetail(int xrow, String xbomkey);

	public List<Mobomdetail> getAllMobomdetail();

	public Mobomdetail findMobomdetailByXrowAndXbomkey(int xrow, String xbomkey);

	public List<Mobomdetail> findMobomdetailsByXbomkey(String xbomkey);

}
