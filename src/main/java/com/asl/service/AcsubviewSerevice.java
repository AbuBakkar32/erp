package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Acsubview;

/**
 * @author Zubayer Ahamed
 * @since Jul 25, 2021
 */
@Component
public interface AcsubviewSerevice {

	public List<Acsubview> findAllSubAccount();
	
	public List<Acsubview> findSubAccountByXacc(String xacc);
	
	public List<Acsubview> findSubAccountByXbank(String xbank);
}
