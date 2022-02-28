package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;
import com.asl.entity.Acbl;
@Component
public interface AcblService {
	public long save(Acbl acbl);
	public long update(Acbl acbl);
	
	public List<Acbl> getAllAcbl();
}
