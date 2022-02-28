package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Bmbomheader;
import com.asl.entity.Bmbomdetail;

/**
 * @author Zubayer Ahamed
 * @since Mar 8, 2021
 */
@Mapper
public interface BmbomMapper {
	
	public long saveBmBomHeader(Bmbomheader bmbomheader);
	public long updateBmBomHeader(Bmbomheader bmbomheader);

	public long saveBmBomDetail(Bmbomdetail bmbomdetail);
	public long updateBmBomDetail(Bmbomdetail bmbomdetail);

	public Bmbomheader findBmBomHeaderByXbomkey(String xbomkey, String zid);
	public Bmbomheader findBmBomHeaderByXitem(String xitem, String zid);
	public Bmbomdetail findBmBomdetailByXbomkeyAndXbomrow(String xbomkey, int xbomrow, String zid);

	public List<Bmbomdetail> findBmBomdetailByXbomkey(String xbomkey, String zid);
	public List<Bmbomheader> getAllBmBomheader(String zid);

	public long archiveBmbomdetailByXbomkey(String xbomkey, String zid);
	public long deleteBmbomdetailByXbomkeyAndXbomrow(Bmbomdetail bmbomdetail);

	public void explodeBom(String zid, String user, String batch, String action, String errseq);
	public void explodeBom2(String zid, String user, String batch, String xbomkey, String action, String errseq);
	
	// Search
	public List<Bmbomheader> searchXbom(String xbomkey, String zid);
}
