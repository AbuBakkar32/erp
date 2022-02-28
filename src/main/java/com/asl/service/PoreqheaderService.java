package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Poreqdetail;
import com.asl.entity.Poreqheader;

@Component
public interface PoreqheaderService {

	public long savePoreqheader(Poreqheader po);

	public long updatePoreqheader(Poreqheader po);

	public long deletePoreqheader(Poreqheader po);

	public Poreqheader topPoreqheader();

	public Poreqheader bottomPoreqheader();

	public Poreqheader nextPoreqheader(String xporeqnum);

	public Poreqheader previousPoreqheader(String xporeqnum);

	public List<Poreqheader> getALllPoreqheaderByXpreparer(String xpreparer);

	public List<Poreqheader> getALllPoreqheader();

	public Poreqheader findByPoreqheader(String xporeqnum); 

	public List<Poreqheader> getPoreqheadersByXtypetrn(String xtypetrn);

	//Details 
	public long savePoreqdetail(Poreqdetail poreqdetail);

	public long updatePoreqdetail(Poreqdetail poreqdetail);

	public long deletePoreqdetail(Poreqdetail poreqdetail);

	public Poreqdetail findPoreqdetailByXporeqnumAndXrow(String xporeqnum, int xrow);

	public Poreqdetail findPoreqdetailByXporeqnumAndXitem(String xporeqnum, String xitem);

	public List<Poreqdetail> findPoreqdetailByXporeqnum(String xporeqnum);

	public long updatePoreqheaderTotalAmt(String xporeqnum);

	// Procedure Calls
	public void procSP_CREATEPO_FROMPRQ(String xporeqnum, String p_screen,String p_seq);

}
