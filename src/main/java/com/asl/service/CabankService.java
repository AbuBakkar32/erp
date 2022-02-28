package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Cabank;

/**
 * @author Zubayer Ahamed
 * @since Jul 12, 2021
 */
@Component
public interface CabankService {

	public long save(Cabank cabank);

	public long update(Cabank cabank);

	public long delete(String xbank);

	public Cabank findCaBankByXbank(String xbank);

	public List<Cabank> getAllCaBank();
	
	public List<Cabank> searchBank(String hint);
}
