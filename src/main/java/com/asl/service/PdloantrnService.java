package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Pdloantrn;
import com.asl.entity.Pdloanwriteoff;
@Component
public interface PdloantrnService {
	
	public long savePdloantrn(Pdloantrn pdloantrn);
	
	public long updatePdloantrn(Pdloantrn pdloantrn);

	public long deletePdloantrn(Pdloantrn pdloantrn);
	
	public List<Pdloantrn> getAllPdloantrn();

	public Pdloantrn findByPdloantrn(String xvoucher);
	
	//for LoanWriteOff
	public long savePdloanwriteoff(Pdloanwriteoff loanwo);
	
	public long updatePdloanwriteoff(Pdloanwriteoff loanwo);
	
	public long deletePdloanwriteoff(Pdloanwriteoff loanwo);
	
	public List<Pdloanwriteoff> getAllPdloanwriteoff();
	
	public List<Pdloanwriteoff> findByPdloanwriteoff(String xvoucher);
	
	public Pdloanwriteoff findPdloanwriteoffByXvoucherAndXrow(String xvoucher, int xrow);

}
