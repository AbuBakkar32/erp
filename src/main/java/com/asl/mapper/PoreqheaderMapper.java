package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.ImtorDetail;
import com.asl.entity.PoordHeader;
import com.asl.entity.Poreqdetail;
import com.asl.entity.Poreqheader;

@Mapper
public interface PoreqheaderMapper {

	public long savePoreqheader(Poreqheader po);

	public long updatePoreqheader(Poreqheader po);

	public long deletePoreqheader(Poreqheader po);

	public Poreqheader topPoreqheader(String zid);

	public Poreqheader bottomPoreqheader(String zid);

	public Poreqheader nextPoreqheader(String zid, String xporeqnum);

	public Poreqheader previousPoreqheader(String zid, String xporeqnum);

	public List<Poreqheader> getALllPoreqheaderByXpreparer(String zid, String xpreparer);

	public List<Poreqheader> getALllPoreqheader(String zid);

	public Poreqheader findByPoreqheader(String zid, String xporeqnum); 

	public List<Poreqheader> getPoreqheadersByXtypetrn(String xtypetrn, String zid);

	//Details 
	public long savePoreqdetail(Poreqdetail poreqdetail);

	public long updatePoreqdetail(Poreqdetail poreqdetail);

	public long deletePoreqdetail(Poreqdetail poreqdetail);

	public Poreqdetail findPoreqdetailByXporeqnumAndXrow(String xporeqnum, int xrow, String zid);

	public Poreqdetail findPoreqdetailByXporeqnumAndXitem(String xporeqnum, String xitem, String zid);

	public List<Poreqdetail> findPoreqdetailByXporeqnum(String xporeqnum, String zid);

	public long updatePoreqheaderTotalAmt(String xporeqnum, String zid);

	// Procedure Calls
	public void procSP_CREATEPO_FROMPRQ(String zid, String user, String xporeqnum, String p_screen, String p_seq);

}
