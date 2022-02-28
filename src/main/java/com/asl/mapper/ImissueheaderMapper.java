package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Imissueheader;
import com.asl.entity.Poreqheader;
import com.asl.entity.Imissuedetail;
@Mapper
public interface ImissueheaderMapper {

	public long saveImissueheader(Imissueheader imissueheader);

	public long updateImissueheader(Imissueheader imissueheader);

	public long updateImissueheaderTotalAmt(Imissuedetail imissuedetail);

	public long deleteImissueheader(Imissueheader imissueheader);

	public List<Imissueheader> getAllImissueheader(String zid);

	public Imissueheader findByImissueheader(String xissuenum, String zid);

	public List<Imissueheader> getALllImissueheaderByXwh(String xwh,String zid);

	//for Issue detail
	public long saveImissuedetail(Imissuedetail imissuedetail);

	public long updateImissuedetail(Imissuedetail imissuedetail);

	public long deleteImissuedetail(Imissuedetail imissuedetail);

	public List<Imissuedetail> getAllImissuedetail(String zid);

	public List<Imissuedetail> findByImissuedetail(String xissuenum, String zid);

	public Imissuedetail findImissuedetailByXissuenumAndXrow(String xissuenum, int xrow, String zid);

	public Imissuedetail findImissuedetailByXissuenumAndXitem(String xissuenum, String xitem, String zid);
	
	// Procedure Calls
	public void procIM_confirmProjectIssue(String zid, String user, String xissuenum, String p_seq);
	public void procIM_confirmProjectIssueGL(String zid, String user, String xissuenum, String p_seq);

}
