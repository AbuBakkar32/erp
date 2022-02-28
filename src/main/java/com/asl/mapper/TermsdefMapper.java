package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Termsdef;

@Mapper
public interface TermsdefMapper {

	public long saveTermsdef(Termsdef termsdef);

	public long updateTermsdef(Termsdef termsdef);

	public long deleteTermsdef(Termsdef termsdef);

	public List<Termsdef> getAllTermsdef(String zid);

	public Termsdef findTermsdefByXtypeAndXterm(String xtype, String xterm, String zid);

	public Termsdef findbyXtermid( int xtermid, String zid);
}
