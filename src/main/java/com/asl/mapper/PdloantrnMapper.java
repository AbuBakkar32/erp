package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Pdeducation;
import com.asl.entity.Pdloantrn;
import com.asl.entity.Pdloanwriteoff;

@Mapper
public interface PdloantrnMapper {

	public long savePdloantrn(Pdloantrn pdloantrn);
	
	public long updatePdloantrn(Pdloantrn pdloantrn);

	public long deletePdloantrn(Pdloantrn pdloantrn);
	
	public List<Pdloantrn> getAllPdloantrn(String zid);

	public Pdloantrn findByPdloantrn(String xvoucher, String zid);
	
	//for LoanWriteOff
	public long savePdloanwriteoff(Pdloanwriteoff loanwo);
	
	public long updatePdloanwriteoff(Pdloanwriteoff loanwo);
	
	public long deletePdloanwriteoff(Pdloanwriteoff loanwo);
	
	public List<Pdloanwriteoff> getAllPdloanwriteoff(String zid);
	
	public List<Pdloanwriteoff> findByPdloanwriteoff(String xvoucher, String zid);
	
	public Pdloanwriteoff findPdloanwriteoffByXvoucherAndXrow(String xvoucher, int xrow, String zid);
}
