package com.asl.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.asl.entity.Opcrndetail;
import com.asl.entity.Opcrnheader;
import com.asl.entity.Opdoheader;
import com.asl.model.ResponseHelper;
import com.asl.model.ServiceException;

@Component
public interface OpcrnService {

	public long save(Opcrnheader opcrnheader);

	public Map<String, Object> save(ResponseHelper responseHelper, Opcrnheader opcrnHeader, Opdoheader odh);

	public long update(Opcrnheader opcrnheader);

	public long deleteOpcrnHeader(Opcrnheader opcrnheader);

	public long saveDetail(Opcrndetail opcrndetail);

	public long saveDetail(List<Opcrndetail> opcrndetail) throws ServiceException;

	public long updateDetail(Opcrndetail opcrndetail);

	public long deleteDetail(Opcrndetail opcrndetail);

	public List<Opcrnheader> getAllOpcrnheader();

	public List<Opcrndetail> findOpcrnDetailByXcrnnum(String xcrnnum);

	public Opcrnheader findOpcrnHeaderByXcrnnum(String xcrnnum);

	public Opcrnheader findOpcrnHeaderByXdornum(String xdornum);

	public Opcrndetail findOpcrnDetailByXcrnnumAndXrow(String xcrnnum, int xrow);

	public long updateOpcrnHeaderWithDetail(String xcrnnum);

	// Procedure Calls
	public void procConfirmCRN(String xcrnnum, String p_seq);

	public void procTransferOPtoAR(String xcrnnum, String p_screen, String p_seq);

	public void confirmCRN(Opcrnheader opcrnHeader) throws ServiceException;
	
	public void procTransferOPtoGL(String xcrnnum, String p_screen, String p_seq);
}
