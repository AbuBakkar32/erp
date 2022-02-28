package com.asl.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.mapper.AcyearendMapper;
import com.asl.service.AcyearendService;

@Service
public class AcyearendServiceImpl extends AbstractGenericService implements AcyearendService {

	@Autowired
	private AcyearendMapper acyearendMapper;

	@Override
	@Transactional
	public void acYearEnd(int xyear, Date xdate, String p_seq) {
		acyearendMapper.acYearEnd(sessionManager.getBusinessId(),sessionManager.getLoggedInUserDetails().getUsername(), xyear, xdate, p_seq);

	}

}
