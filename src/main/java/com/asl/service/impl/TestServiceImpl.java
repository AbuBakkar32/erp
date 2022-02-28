package com.asl.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Test;
import com.asl.mapper.TestMapper;
import com.asl.service.TestService;

@Service
public class TestServiceImpl extends AbstractGenericService implements TestService{

	@Autowired TestMapper  testMapper;
	
	@Transactional
	@Override
	public long saveTest(Test test) {
		if (test == null) return 0;
		test.setZid(sessionManager.getBusinessId());
		test.setZauserid(getAuditUser());
		return testMapper.saveTest(test);
	}

	@Transactional
	@Override
	public long updateTest(Test test) {
		if(test == null) return 0;
		test.setZid(sessionManager.getBusinessId());
		test.setZauserid(getAuditUser());
		return testMapper.updateTest(test);
	}

	@Transactional
	@Override
	public long deleteTest(Test test) {
		if(test == null) return 0;
		long count = testMapper.deleteTest(test);
		return count;
	}

	@Override
	public Test findTestByXpornum(String xpornum) {
		if (StringUtils.isBlank(xpornum))
			return null;
		return testMapper.findTestByXpornum(xpornum, sessionManager.getBusinessId());
	}

	@Override
	public List<Test> getAllTest() {
		return testMapper.getAllTest(sessionManager.getBusinessId());
	}

	@Override
	public List<Test> getTestByXtypetrn(String xtypetrn) {
		if (StringUtils.isBlank(xtypetrn))
			return null;
		return testMapper.getTestByXtypetrn(xtypetrn, sessionManager.getBusinessId());
	}

}
