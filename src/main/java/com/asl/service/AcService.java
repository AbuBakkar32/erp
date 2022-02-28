package com.asl.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.asl.entity.Acdef;
import com.asl.entity.Acdetail;
import com.asl.entity.Acheader;
import com.asl.entity.Xtrn;
import com.asl.model.ResponseHelper;
import com.asl.model.ServiceException;

@Component
public interface AcService {

	// For Header Entity
	public long saveAcheader(Acheader acheader);

	public long updateAcheader(Acheader acheader);

	public Map<String, Object> saveWithDetail(Acheader acheader, List<Acdetail> acdetails, ResponseHelper responseHelper) throws ServiceException;

	public long deleteAcheader(String xvoucher);

	public List<Acheader> getAllAcheader();

	public Acheader findAcheaderByXvoucher(String xvoucher);
	
	public Acheader findAcheaderByXvoucher(String xvoucher, String zid);

	// For Detail Entity
	public long saveAcdetail(Acdetail acdetail);

	public long updateAcdetail(Acdetail acdetail);

	public long deleteAcdetail(int xrow, String xvoucher);

	public List<Acdetail> getAllAcdetail();

	public Acdetail findAcdetailByXrowAndXvoucher(int xrow, String xvoucher);

	public List<Acdetail> findAcdetailsByXvoucher(String xvoucher);

	public long updateAcheaderXstatusjv(String xvoucher);

	public void procAcVoucherPost(Integer xyear, Integer xper, String xfvoucher, String xtvoucher);

	public void procAcVoucherUnPost(Integer xyear, Integer xper, String xfvoucher, String xtvoucher);

	public Acheader setXyearAndXper(Acheader acheader, Acdef acdef);
	
	// Report Voucher
	public List<Acheader> getAllVoucherNumber(String hint);
	
	public List<Acheader> getAllStatusNumber();
	
	public List<Acheader> getAllApproveStatusNumber();
	
	public Acheader findReportName(String xvoucher);
	
	public Xtrn getReportName(String xvoucher);
	
	public List<Map<String, Object>> getExportDataByChunk(long limit, long offset, String zid);

	public List<Acheader> searchVoucher(String hint);

}
