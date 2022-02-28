package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Opordheader;
import com.asl.entity.Opreqdetail;
import com.asl.entity.Opreqheader;
import com.asl.model.ServiceException;

@Component
public interface OpreqService {

	public long saveOpreqheader(Opreqheader opreqheader);

	public long updateOpreqheader(Opreqheader opreqheader);

	public long deleteOpreqheader(String xdoreqnum);

	public Opreqheader findOpreqHeaderByXdoreqnum(String xdoreqnum);

	public List<Opreqheader> getAllOpreqheader();

	public List<Opreqheader> getAllStatusOpenOpreqheader();
	
	public List<Opreqheader> getAllStatusConfirmedOpreqheader();

	public List<Opordheader> getAllSOCreated(String xreqnum);

	public long saveOpreqdetail(Opreqdetail opreqdetail);

	public long updateOpreqdetail(Opreqdetail opreqdetail);

	public long deleteOpreqdetail(Opreqdetail opreqdetail);

	public long deleteOpreqdetailData(String xdoreqnum);

	public long saveDetail(List<Opreqdetail> opreqdetail) throws ServiceException;

	public List<Opreqdetail> findOpreqDetailByXdoreqnum(String xdoreqnum);

	public Opreqdetail findOpreqdetailByXordernumAndXrow(String xdoreqnum, int xrow);

	public Opreqdetail findOpreqdetailByXdoreqnumAndXitem(String xdoreqnum, String xitem);

	public long updateOpreqHeaderTotalAmtAndGrandTotalAmt(String xdoreqnum);

	// For Individual Customer Requisition
	public Opreqheader findOpreqCusHeaderByXdoreqnum();

	public List<Opreqheader> getAllOpreqCusheader();

	public List<Opreqheader> getAllStatusOpenOpreqCusheader();

	// Procedure Calls
	public void procCreateSOFromSRQ(String xdoreqnum,String p_seq);
	
	public void procCreateSOFromSRQ_khanas(String xdoreqnum);

}
