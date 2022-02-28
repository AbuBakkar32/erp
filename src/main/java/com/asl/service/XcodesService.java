package com.asl.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.asl.entity.Xcodes;

/**
 * @author Zubayer Ahamed
 * @since Feb 24, 2021
 */
@Component
public interface XcodesService {

	public long save(Xcodes xcodes);
	
	public long save(Xcodes xcodes, String zid, String audituser);

	public long update(Xcodes xcodes);
	
	public long update(Xcodes xcodes, String zid, String audituser);

	public List<Xcodes> getAllXcodes();

	public List<Xcodes> getAllXcodesByCusGroup();

	public List<Xcodes> getAllXcodesBySupGroup();

	public List<Xcodes> getAllXcodes(Boolean zactive);

	public List<Xcodes> findByXtype(String xType);

	public List<Xcodes> findByXtype(String xType, Boolean zactive);

	public List<Xcodes> findByXtypeAndXtypeobj(String xtype, String xtypeobj, Boolean zactive);

	public List<Xcodes> findByXcode(String xCode);

	public List<Xcodes> findByXcode(String xCode, Boolean zactive);

	public Xcodes findByXtypesAndXcodes(String xType, String xCodes);
	
	public Xcodes findByXtypesAndXcodesAndZid(String xType, String xCodes, String zid);

	public Xcodes findByXtypesAndXcodes(String xType, String xCodes, Boolean zactive);
	
	public Xcodes findByXtypesAndXcodesForSup(String xType, String xCodes);
	
	public Xcodes findByXtypesAndXcodesForCus(String xType, String xCodes);

	public Xcodes getSeilingRecord(String direction);

	public long deleteXcodes(String xcodes, String xtype);

	public List<Xcodes> getAllPDTransactionTypes();

	public List<Xcodes> getAllPDTransactionTypes(Boolean zactive);

	public long deletePDCodes(String xcode);

	public Xcodes findByXcodes(String xcode);

	public List<Map<String, Object>> getExportDataByChunk(long limit, long offset, String zid);

	public List<Xcodes> getAllParents();

	public List<Xcodes> getAllCategoryWiseItem(String xparentcode);

	//search
	public List<Xcodes> searchSite(String xcode);
	public List<Xcodes> searchProject(String xcode);
	public List<Xcodes> searchAllProject(String xcode);
}
