package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Test;

@Mapper
public interface TestMapper {
	
	public long saveTest(Test test);

	public long updateTest(Test test);
	
	public long deleteTest(Test test);

	public Test findTestByXpornum(String xpornum,String zid);

	public List<Test> getAllTest(String zid);

	public List<Test> getTestByXtypetrn(String xtypetrn, String zid);


}
