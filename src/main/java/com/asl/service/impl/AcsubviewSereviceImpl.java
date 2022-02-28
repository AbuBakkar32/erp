package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.Acsubview;
import com.asl.mapper.AcsubviewMapper;
import com.asl.service.AcsubviewSerevice;

/**
 * @author Zubayer Ahamed
 * @since Jul 25, 2021
 */
@Service
public class AcsubviewSereviceImpl extends AbstractGenericService implements AcsubviewSerevice {

	@Autowired private AcsubviewMapper acsubviewMapper;

	@Override
	public List<Acsubview> findAllSubAccount() {
		return acsubviewMapper.findAllSubAccount(sessionManager.getBusinessId());
	}

	@Override
	public List<Acsubview> findSubAccountByXacc(String xacc) {
		if(StringUtils.isBlank(xacc)) return Collections.emptyList();
		return acsubviewMapper.findSubAccountByXacc(xacc, sessionManager.getBusinessId());
	}

	@Override
	public List<Acsubview> findSubAccountByXbank(String xbank) {
		if(StringUtils.isBlank(xbank)) return Collections.emptyList();
		return acsubviewMapper.findSubAccountByXbank(xbank, sessionManager.getBusinessId());
	}
}
