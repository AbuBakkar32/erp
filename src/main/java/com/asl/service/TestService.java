package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Test;
@Component
public interface TestService {
	public long saveTest(Test test);

	public long updateTest(Test test);
	
	public long deleteTest(Test test);

	public Test findTestByXpornum(String xpornum);

	public List<Test> getAllTest();

	public List<Test> getTestByXtypetrn(String xtypetrn);
}
