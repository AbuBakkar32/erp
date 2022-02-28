package com.asl.service;

import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public interface AcyearendService {

	public void acYearEnd(int xyear, Date xdate, String p_seq);
}
