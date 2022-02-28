package com.asl.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Imtag;
import com.asl.entity.Imtdet;

@Mapper
public interface ImtagMapper {
	public long saveImtag(Imtag imtag);
	public long updateImtag(Imtag imtag);
	public long deleteImtag(Imtag imtag);
	
	public long saveImtdet(Imtdet imtdet);
	public long updateImtdet(Imtdet imtdet);
	public long deleteImtdet(Imtdet imtdet);
	
	public Imtag findImtagByXtagnum(String xtagnum, String zid);
	public List<Imtag> getAllImTag(String zid);
	
	public Imtdet findImtdetByXtagnumAndXrow(String xtagnum, int xrow, String zid);
	public Imtdet findImtdetByXtagnumAndXitem(String xtagnum, String xitem, String zid);
	public List<Imtdet> findImtdetByXtagnum(String xtagnum, String zid);
	
	//procedure
	public void procStockTake(String zid, String user, Date xdate, String xtagnum, String p_seq);
}
