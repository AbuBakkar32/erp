package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Moheader;
import com.asl.entity.DailyProductionBatchDetail;
import com.asl.entity.Modetail;

/**
 * @author Zubayer Ahamed
 * @since Mar 8, 2021
 */
@Mapper
public interface MoMapper {

	public long saveMoHeader(Moheader moheader);

	public long updateMoHeader(Moheader moheader);

	public long saveMoDetail(Modetail modetail);

	public long updateMoDetail(Modetail modetail);

	public Moheader findMoHeaderByXbatch(String xbatch, String zid);

	public List<Moheader> findMoHeaderByXchalan(String xchalan, String zid);

	public Modetail findModetailByXrowAndXbatch(int xrow, String xbatch, String zid);

	public List<Modetail> findModetailByXbatch(String xbatch, String zid);
	
	public List<Moheader> getAllMoheader(String zid);

	public List<Moheader> findMoheaderByXchalanAndXitem(String xchalan, String xitem, String zid);

	public Modetail findModetailByXbatchAndXitem(String xbatch, String xitem, String zid);

	public void processProduction(String batch, String action, String errseq, String zid, String user);

	public Modetail findModetailByXbatchAndXtype(String xbatch, String xtype, String zid);

	public long deleteModetail(Modetail modetail);
	
	//search field
	public List<Moheader> findModetailXbatch(String xbatch, String zid);
	
	public List<Modetail> findModetailByXtype(String xtype, String zid);

	public List<DailyProductionBatchDetail> dailyProductionReport(String xchalan, String zid);

	public long deleteModetailByXbatch(String xbatch, String zid);
}
