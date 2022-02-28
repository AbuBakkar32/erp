package com.asl.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Cacus;
import com.asl.entity.Caproject;
import com.asl.entity.Xcodes;

/**
 * @author Zubayer Ahamed
 * @since Feb 24, 2021
 */
@Mapper
public interface XcodesMapper {

	public long saveXcodes(Xcodes xcodes);

	public long updateXcodes(Xcodes xcodes);

	public List<Xcodes> getAllXcodes(String zid, Boolean zactive);

	public List<Xcodes> getAllXcodesByCusGroup(String zid);

	public List<Xcodes> getAllXcodesBySupGroup(String zid);

	public List<Xcodes> findByXtype(String xtype, String zid, Boolean zactive);

	public List<Xcodes> findByXcode(String xcode, String zid, Boolean zactive);

	public List<Xcodes> findByXtypeAndXtypeobj(String xtype, String xtypeobj, String zid, Boolean zactive);

	public Xcodes findByXtypesAndXcodes(String xtype, String xcode, String zid, Boolean zactive);
	
	public Xcodes findByXtypesAndXcodesForSup(String xtype, String xcode, String zid);
	
	public Xcodes findByXtypesAndXcodesForCus(String xtype, String xcode, String zid);

	public Xcodes getSeilingRecord(String direction, String zid);

	public long deleteXcodes(String xcode, String xtype, String zid);

	public List<Xcodes> getAllPDTransactionTypes(String zid, Boolean zactive);

	public long deletePDCodes(String xcode, String zid);

	public Xcodes findByXcodes(String xcode, String zid);

	public List<Map<String, Object>> getExportDataByChunk(long limit, long page, String zid);

	public List<Xcodes> getAllParents(String zid);

	public List<Xcodes> getAllCategoryWiseItem(String xparentcode, String zid);

	//search
	public List<Xcodes> searchSite(String xcode, String zid);
	public List<Xcodes> searchProject(String xcode, String zid);
	public List<Xcodes> searchAllProject(String xcode, String zid);

}
