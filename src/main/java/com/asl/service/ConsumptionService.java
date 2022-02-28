package com.asl.service;

import java.util.Date;

import org.springframework.stereotype.Component;
@Component
public interface ConsumptionService {
	
	public void IM_2GL_Transfer(Date xstartdate, Date xdatexenddate, Date xdate, String xwh, String xtrn, String p_seq);

}
