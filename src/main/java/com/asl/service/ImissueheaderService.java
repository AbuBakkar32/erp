package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Imissuedetail;
import com.asl.entity.Imissueheader;
import com.asl.entity.Poreqheader;

@Component
public interface ImissueheaderService {

	public long saveImissueheader(Imissueheader imissueheader);

	public long updateImissueheader(Imissueheader imissueheader);

	public long updateImissueheaderTotalAmt(Imissuedetail imissuedetail);

	public long deleteImissueheader(Imissueheader imissueheader);

	public List<Imissueheader> getAllImissueheader();

	public Imissueheader findByImissueheader(String xissuenum);

	public List<Imissueheader> getALllImissueheaderByXwh(String xwh);

	//for Issue detail
	public long saveImissuedetail(Imissuedetail imissuedetail);

	public long updateImissuedetail(Imissuedetail imissuedetail);

	public long deleteImissuedetail(Imissuedetail imissuedetail);

	public List<Imissuedetail> getAllImissuedetail();

	public List<Imissuedetail> findByImissuedetail(String xissuenum);

	public Imissuedetail findImissuedetailByXissuenumAndXrow(String xissuenum, int xrow);

	public Imissuedetail findImissuedetailByXissuenumAndXitem(String xissuenum, String xitem);
	
	// Procedure Calls
	public void procIM_confirmProjectIssue(String xissuenum, String p_seq);
	public void procIM_confirmProjectIssueGL(String xissuenum, String p_seq);

}
