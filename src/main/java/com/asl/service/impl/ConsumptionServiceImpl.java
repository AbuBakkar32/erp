package com.asl.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.mapper.ConsumptionMapper;
import com.asl.service.ConsumptionService;

@Service
public class ConsumptionServiceImpl extends AbstractGenericService implements ConsumptionService{

	@Autowired
	private ConsumptionMapper consumptionMapper;
	
	@Transactional
	@Override
	public void IM_2GL_Transfer(Date xstartdate, Date xdatexenddate, Date xdate, String xwh, String xtrn,String p_seq) {
		
		consumptionMapper.IM_2GL_Transfer(sessionManager.getBusinessId(),sessionManager.getLoggedInUserDetails().getUsername(), xstartdate, xdatexenddate, xdate, xwh, xtrn, p_seq);
	}

}
