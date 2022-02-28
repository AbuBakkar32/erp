package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Cabank;
import com.asl.mapper.CabankMapper;
import com.asl.service.CabankService;

/**
 * @author Zubayer Ahamed
 * @since Jul 12, 2021
 */
@Service
public class CabankServiceImpl extends AbstractGenericService implements CabankService{

	@Autowired private CabankMapper cabankMapper;

	@Transactional
	@Override
	public long save(Cabank cabank) {
		if(cabank == null) return 0;
		cabank.setZid(sessionManager.getBusinessId());
		cabank.setZauserid(getAuditUser());
		return cabankMapper.save(cabank);
	}

	@Transactional
	@Override
	public long update(Cabank cabank) {
		if(cabank == null) return 0;
		cabank.setZid(sessionManager.getBusinessId());
		cabank.setZuuserid(getAuditUser());
		return cabankMapper.update(cabank);
	}

	@Transactional
	@Override
	public long delete(String xbank) {
		if(StringUtils.isBlank(xbank)) return 0;
		return cabankMapper.delete(xbank, sessionManager.getBusinessId());
	}

	@Override
	public Cabank findCaBankByXbank(String xbank) {
		if(StringUtils.isBlank(xbank)) return null;
		return cabankMapper.findCaBankByXbank(xbank, sessionManager.getBusinessId());
	}

	@Override
	public List<Cabank> getAllCaBank() {
		return cabankMapper.getAllCaBank(sessionManager.getBusinessId());
	}

	@Override
	public List<Cabank> searchBank(String hint) {
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		return cabankMapper.searchBank(hint.toUpperCase(), sessionManager.getBusinessId());
	}
	

}
