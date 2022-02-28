package com.asl.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.asl.entity.Pocrndetail;
import com.asl.entity.Pocrnheader;
import com.asl.entity.PogrnHeader;
import com.asl.model.ResponseHelper;
import com.asl.model.ServiceException;

@Component
public interface PocrnService {

	public long save(Pocrnheader pocrnheader);

	public Map<String, Object> save(ResponseHelper responseHelper, Pocrnheader pocrnHeader, PogrnHeader pgh);

	public long update(Pocrnheader pocrnheader);

	public long deletePocrnHeader(String xcrnnum);

	public long saveDetail(Pocrndetail pocrndetail);

	public long saveDetails(List<Pocrndetail> pocrndetail) throws ServiceException;

	public long updateDetail(Pocrndetail pocrndetail);

	public long deleteDetail(Pocrndetail pocrndetail);

	public List<Pocrnheader> getAllPocrnheader();

	public List<Pocrndetail> findPocrnDetailByXcrnnum(String xcrnnum);

	public Pocrnheader findPocrnHeaderByXcrnnum(String xcrnnum);

	public Pocrnheader findPocrnHeaderByXgrnnum(String xgrnnum);

	public Pocrndetail findPocrnDetailByXcrnnumAndXrow(String xcrnnum, int xrow);

	public long updatePocrnHeaderWithDetail(String xcrnnum);

	// Procedure Calls
	public void procConfirmCRN(String xcrnnum, String p_seq);

	public void procIssuePricing(String xtrnnum, String xwh, String p_seq);

	public void procTransferPRtoAP(String xcrnnum, String p_seq);
	
	public List<Pocrnheader> findPocrnXstatuscrn(String xstatuscrn);

	public void confirmCRN(Pocrnheader pocrnHeader) throws ServiceException;

	public void SRPR_transferARtoGL(String xcrnnum, String p_seq);

}
