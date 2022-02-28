package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Opreqheader;
import com.asl.entity.PoordDetail;
import com.asl.entity.Opreqdetail;
import com.asl.entity.Opordheader;

/**
 * @author Zubayer Ahamed
 * @since Jun 20, 2021
 */
@Mapper
public interface OpreqMapper {
	//opreqheader
	public long saveOpreqheader(Opreqheader opreqheader);

	public long updateOpreqheader(Opreqheader opreqheader);

	public long deleteOpreqheader(String xdoreqnum, String zid);
	
	public Opreqheader findOpreqHeaderByXdoreqnum(String xdoreqnum, String zid);
	
	public List<Opreqheader> getAllOpreqheader(String zid);
	
	public List<Opreqheader> getAllStatusOpenOpreqheader(String zid);
	
	public List<Opreqheader> getAllStatusConfirmedOpreqheader(String zid);
	
	public List<Opordheader> getAllSOCreated(String zid, String xreqnum);
	
	//opreqdetail
	public long saveOpreqdetail(Opreqdetail opreqdetail);

	public long updateOpreqdetail(Opreqdetail opreqdetail);

	public long deleteOpreqdetail(Opreqdetail opreqdetail);
	
	public long deleteOpreqdetailData(String xdoreqnum, String zid);
	
	public long savePoordDetailWithRow(Opreqdetail opreqdetail);
	
	public List<Opreqdetail> findOpreqDetailByXdoreqnum(String xdoreqnum, String zid);
	
	public Opreqdetail findOpreqdetailByXordernumAndXrow(String xdoreqnum, int xrow, String zid);
	
	public Opreqdetail findOpreqdetailByXdoreqnumAndXitem(String xdoreqnum, String xitem, String zid);
	
	public long updateOpreqHeaderTotalAmtAndGrandTotalAmt(String xdoreqnum, String zid);
	
	//For Individual Customer Requisition 
	public Opreqheader findOpreqCusHeaderByXdoreqnum(String zid, String xcus);
	
	public List<Opreqheader> getAllOpreqCusheader(String zid,String xcus);
	
	public List<Opreqheader> getAllStatusOpenOpreqCusheader(String zid, String xcus);
	
	//Procedure Calls
	public void procCreateSOFromSRQ(String zid, String user, String xdoreqnum, String p_seq);
	
	public void procCreateSOFromSRQ_Khanas(String zid, String user, String xdoreqnum);
}
