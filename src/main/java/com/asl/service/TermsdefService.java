package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Termsdef;
@Component
public interface TermsdefService {

	public long saveTermsdef(Termsdef termsdef);

	public long updateTermsdef(Termsdef termsdef);

	public long deleteTermsdef(Termsdef termsdef);

	public List<Termsdef> getAllTermsdef();

	public Termsdef findTermsdefByXtypeAndXterm(String xtype, String xterm);

	public Termsdef findbyXtermid( int xtermid);

}
