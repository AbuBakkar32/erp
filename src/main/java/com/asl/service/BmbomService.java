package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Bmbomdetail;
import com.asl.entity.Bmbomheader;

/**
 * @author Zubayer Ahamed
 * @since Mar 13, 2021
 */
@Component
public interface BmbomService {

	public long saveBmbomheader(Bmbomheader bmbomheader);

	public long updateBmbomheader(Bmbomheader bmbomheader);

	public long saveBmbomdetail(Bmbomdetail bmbomdetail);

	public long updateBmbomdetail(Bmbomdetail bmbomdetail);

	public Bmbomheader findBmbomheaderByXbomkey(String xbomkey);

	public Bmbomheader findBmBomHeaderByXitem(String xitem);

	public Bmbomdetail findBmbomdetailByXbomkeyAndXbomrow(String xbomkey, int xbomrow);

	public List<Bmbomdetail> findBmbomdetailByXbomkey(String xbomkey);

	public List<Bmbomheader> getAllBmbomheader();

	public long archiveBmbomdetailByXbomkey(String xbomkey);

	public long deleteBmbomdetailByXbomkeyAndXbomrow(Bmbomdetail bmbomdetail);

	public void explodeBom(String batch, String action, String errseq);
	public void explodeBom2(String batch, String xbomkey, String action, String errseq);
	
	//Search
	public List<Bmbomheader> searchXbom(String xbomkey);
}
