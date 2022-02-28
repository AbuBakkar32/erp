package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.ImtorDetail;
import com.asl.entity.ImtorHeader;

@Mapper
public interface ImtorMapper {

	public long saveImtorHeader(ImtorHeader imtorHeader);

	public long saveImtorHeaderWithChalan(ImtorHeader imtorHeader);

	public long updateImtorHeader(ImtorHeader imtorHeader);

	public long deleteImtorHeader(ImtorHeader imtorHeader);
	
	public long saveImtorDetail(ImtorDetail imtorDetail);

	public long updateImtorDetail(ImtorDetail imtorDetail);

	public long deleteImtorDetail(ImtorDetail imtorDetail);

	public ImtorHeader findImtorHeaderByXtornum(String xtornum, String zid);

	public List<ImtorHeader> findImtorHeaderByXchalanref(String xchalanref, String zid);
	
	public List<ImtorHeader> findImtorHeaderByXchalanrefAndXfwhAndXtwh(String xchalanref, String xfwh, String xtwh, String zid);

	public List<ImtorHeader> getAllImtorHeader(String zid);

	public ImtorDetail findImtorDetailByXtornumAndXrow(String xtornum, int xrow, String zid);

	public ImtorDetail findImtorDetailByXtornumAndXitem(String xtornum, String xitem, String zid);

	public List<ImtorDetail> findImtorDetailByXtornum(String xtornum, String zid, String centralzid);

	public List<ImtorDetail> findImtorDetailByXtornumAndXchalanref(String xtornum, String xchalanref, String zid);

	public long updateImtorHeaderTotalAmt(ImtorDetail imtorDetail);

	// Procedure Calls
	public void procConfirmTO(String zid, String user, String xtornum, String p_action, String p_seq);

	public long deleteImtorDetailByXtornum(String xtornum, String zid);
	
	public List<ImtorHeader> getAllImtorHeaderbyPrefix(String xtypetrn, String zid);

}
