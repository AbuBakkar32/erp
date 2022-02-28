package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.Imstock;
import com.asl.mapper.ImstockMapper;
import com.asl.service.ImstockService;

/**
 * @author Zubayer Ahamed
 * @since Mar 1, 2021
 */
@Service
public class ImstockServiceImpl extends AbstractGenericService implements ImstockService {

	@Autowired private ImstockMapper imstockMapper;

	@Override
	public List<Imstock> findByXitem(String xitem) {
		if(StringUtils.isBlank(xitem)) return Collections.emptyList();
		return imstockMapper.findByXitem(xitem, sessionManager.getBusinessId());
	}

	@Override
	public Imstock findByXitemAndXwh(String xitem, String xwh) {
		if(StringUtils.isBlank(xitem) || StringUtils.isBlank(xwh)) return null;
		return imstockMapper.findByXitemAndXwh(xitem, xwh, sessionManager.getBusinessId());
	}

	@Override
	public List<Imstock> searchXitem(String xitem) {
		return imstockMapper.searchXitem(xitem.toUpperCase(), sessionManager.getBusinessId());
	}

	@Override
	public List<Imstock> search(String xwh, String xitem) {
		return imstockMapper.search(xwh, StringUtils.isBlank(xitem) ? null : xitem.toUpperCase(), sessionManager.getBusinessId());
	}

}
