package com.asl.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Imtag;
import com.asl.entity.Imtdet;

@Component
public interface ImtagService {

	public long saveImtag(Imtag imtag);

	public long updateImtag(Imtag imtag);
	
	public long deleteImtag(Imtag imtag);

	public long saveImtdet(Imtdet imtdet);

	public long updateImtdet(Imtdet imtdet);

	public long deleteImtdet(Imtdet imtdet);

	public Imtag findImtagByXtagnum(String xtagnum);

	public List<Imtag> getAllImTag();

	public Imtdet findImtdetByXtagnumAndXrow(String xtagnum, int xrow);

	public Imtdet findImtdetByXtagnumAndXitem(String xtagnum, String xitem);

	public List<Imtdet> findImtdetByXtagnum(String xtagnum);
	
	public void procStockTake(Date xdate, String xtagnum, String p_seq);

}
