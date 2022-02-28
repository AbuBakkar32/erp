package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.DailyProductionBatchDetail;
import com.asl.entity.Modetail;
import com.asl.entity.Moheader;
import com.asl.model.ServiceException;

/**
 * @author Zubayer Ahamed
 * @since Mar 18, 2021
 */
@Component
public interface MoService {

	public long saveBatchMoHeader(List<Moheader> moheaders);

	public long saveMoHeader(Moheader moheader);

	public long updateMoHeader(Moheader moheader);

	public long updateMoHeader(List<Moheader> moheader);

	public long saveMoDetail(Modetail modetail);

	public long saveBatchMoDetail(List<Modetail> modetails);

	public long updateMoDetail(Modetail modetail);

	public Moheader findMoHeaderByXbatch(String xbatch);

	public Modetail findModetailByXrowAndXbatch(int xrow, String xbatch);

	public List<Modetail> findModetailByXbatch(String xbatch);

	public List<Moheader> getAllMoheader();

	public List<Moheader> findMoheaderByXchalanAndXitem(String xchalan, String xitem);

	public Modetail findModetailByXbatchAndXitem(String xbatch, String xitem);

	public void processProduction(String batch, String action, String errseq);

	public void bulkProcessProduction(List<String> batchList, String action, String errseq);

	public Modetail findDefaultModetailByXbatch(String xbatch);

	public long deleteModetail(Modetail modetail);

	public boolean isProductionProcessCompleted(String xchalan);

	public List<Moheader> findMoheaderByXchalan(String xchalan);

	// search field

	public List<Moheader> findModetailXbatch(String xbatch);

	public List<Modetail> findModetailByXtype(String xtype);

	public List<DailyProductionBatchDetail> dailyProductionReport(String xchalan);

	public long deleteModetailByXbatch(String xbatch);

	public String createBulkBatchFromProductionChallan(String challan) throws ServiceException;
}
