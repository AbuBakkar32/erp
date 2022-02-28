package com.asl.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Cacus;
import com.asl.entity.Xcodes;
import com.asl.mapper.XcodesMapper;
import com.asl.service.XcodesService;

/**
 * @author Zubayer Ahamed
 * @since Feb 24, 2021
 */
@Service
public class XcodesServiceImpl extends AbstractGenericService implements XcodesService {

	@Autowired
	private XcodesMapper xcodesMapper;

	@Transactional
	@Override
	public long save(Xcodes xcodes) {
		return save(xcodes, sessionManager.getBusinessId(), getAuditUser());
	}

	@Transactional
	@Override
	public long save(Xcodes xcodes, String zid, String audituser) {
		if (xcodes == null || StringUtils.isBlank(xcodes.getXtype()) || StringUtils.isBlank(xcodes.getXcode())) return 0;
		xcodes.setZid(zid);
		xcodes.setZauserid(audituser);
		return xcodesMapper.saveXcodes(xcodes);
	}


	@Transactional
	@Override
	public long update(Xcodes xcodes) {
		return update(xcodes, sessionManager.getBusinessId(), getAuditUser());
	}

	@Transactional
	@Override
	public long update(Xcodes xcodes, String zid, String audituser) {
		if (xcodes == null || StringUtils.isBlank(xcodes.getXtype()) || StringUtils.isBlank(xcodes.getXcode())) return 0;
		xcodes.setZid(zid);
		xcodes.setZuuserid(audituser);
		return xcodesMapper.updateXcodes(xcodes);
	}

	@Override
	public List<Xcodes> getAllXcodes() {
		return getAllXcodes(null);
	}
	
	@Override
	public List<Xcodes> getAllXcodesByCusGroup(){
		return xcodesMapper.getAllXcodesByCusGroup(sessionManager.getBusinessId());
	}
	
	@Override
	public List<Xcodes> getAllXcodesBySupGroup(){
		return xcodesMapper.getAllXcodesBySupGroup(sessionManager.getBusinessId());
	}

	@Override
	public List<Xcodes> getAllXcodes(Boolean zactive) {
		return xcodesMapper.getAllXcodes(sessionManager.getBusinessId(), zactive);
	}

	@Override
	public List<Xcodes> findByXtype(String xType) {
		return findByXtype(xType, null);
	}

	@Override
	public List<Xcodes> findByXtype(String xType, Boolean zactive) {
		if (StringUtils.isBlank(xType)) return Collections.emptyList();
		return xcodesMapper.findByXtype(xType, sessionManager.getBusinessId(), zactive);
	}

	@Override
	public List<Xcodes> findByXtypeAndXtypeobj(String xtype, String xtypeobj, Boolean zactive){
		if (StringUtils.isBlank(xtype) || StringUtils.isBlank(xtypeobj)) return Collections.emptyList();
		return xcodesMapper.findByXtypeAndXtypeobj(xtype, xtypeobj, sessionManager.getBusinessId(), zactive );
	}

	@Override
	public List<Xcodes> findByXcode(String xCode) {
		return findByXcode(xCode, null);
	}

	@Override
	public List<Xcodes> findByXcode(String xCode, Boolean zactive) {
		if (StringUtils.isBlank(xCode)) return Collections.emptyList();
		return xcodesMapper.findByXcode(xCode, sessionManager.getBusinessId(), zactive);
	}

	@Override
	public Xcodes findByXtypesAndXcodes(String xType, String xCodes) {
		return findByXtypesAndXcodes(xType, xCodes, null);
	}

	

	

	@Override
	public Xcodes findByXtypesAndXcodesAndZid(String xType, String xCodes, String zid) {
		if (StringUtils.isBlank(xType) || StringUtils.isBlank(xCodes)) return null;
		return xcodesMapper.findByXtypesAndXcodes(xType, xCodes, zid, null);
	}

	@Override
	public Xcodes findByXtypesAndXcodes(String xType, String xCodes, Boolean zactive) {
		if (StringUtils.isBlank(xType) || StringUtils.isBlank(xCodes)) return null;
		return xcodesMapper.findByXtypesAndXcodes(xType, xCodes, sessionManager.getBusinessId(), zactive);
	}
	
	@Override
	public Xcodes findByXtypesAndXcodesForSup(String xType, String xCodes) {
		if (StringUtils.isBlank(xType) || StringUtils.isBlank(xCodes)) return null;
		return xcodesMapper.findByXtypesAndXcodesForSup(xType, xCodes, sessionManager.getBusinessId());
	}
	
	@Override
	public Xcodes findByXtypesAndXcodesForCus(String xType, String xCodes) {
		if (StringUtils.isBlank(xType) || StringUtils.isBlank(xCodes)) return null;
		return xcodesMapper.findByXtypesAndXcodesForCus(xType, xCodes, sessionManager.getBusinessId());
	}

	@Override
	public Xcodes getSeilingRecord(String direction) {
		return xcodesMapper.getSeilingRecord(direction, sessionManager.getBusinessId());
	}

	@Transactional
	@Override
	public long deleteXcodes(String xcodes, String xtype) {
		if(StringUtils.isBlank(xcodes) || StringUtils.isBlank(xtype)) return 0;
		return xcodesMapper.deleteXcodes(xcodes, xtype, sessionManager.getBusinessId());
	}

	@Override
	public List<Xcodes> getAllPDTransactionTypes() {
		return getAllPDTransactionTypes(null);
		
	}

	@Override
	public List<Xcodes> getAllPDTransactionTypes(Boolean zactive) {
		return xcodesMapper.getAllPDTransactionTypes(sessionManager.getBusinessId(),zactive);
	}

	@Transactional
	@Override
	public long deletePDCodes(String xcode) {
		if(StringUtils.isBlank(xcode)) return 0;
		return xcodesMapper.deletePDCodes(xcode, sessionManager.getBusinessId());
	}

	@Override
	public Xcodes findByXcodes(String xcode) {
		if (StringUtils.isBlank(xcode) ) return null;
		return xcodesMapper.findByXcodes(xcode, sessionManager.getBusinessId());
	}

	@Override
	public List<Map<String, Object>> getExportDataByChunk(long limit, long offset, String zid) {
		return xcodesMapper.getExportDataByChunk(limit, offset, zid);
	}

	@Override
	public List<Xcodes> getAllParents() {
		return xcodesMapper.getAllParents(sessionManager.getBusinessId());
	}

	@Override
	public List<Xcodes> getAllCategoryWiseItem(String xparentcode) {
		if (StringUtils.isBlank(xparentcode) ) return null;
		return xcodesMapper.getAllCategoryWiseItem(xparentcode, sessionManager.getBusinessId());
	}

	@Override
	public List<Xcodes> searchSite(String xcode) {
		if(StringUtils.isBlank(xcode)) return Collections.emptyList();
		return xcodesMapper.searchSite(xcode, sessionManager.getBusinessId());
	}

	@Override
	public List<Xcodes> searchProject(String xcode) {
		if(StringUtils.isBlank(xcode)) return Collections.emptyList();
		return xcodesMapper.searchProject(xcode.toUpperCase(), sessionManager.getBusinessId());
	}

	@Override
	public List<Xcodes> searchAllProject(String xcode) {
		if(StringUtils.isBlank(xcode)) return Collections.emptyList();
		return xcodesMapper.searchAllProject(xcode.toUpperCase(), sessionManager.getBusinessId());
	}
	
}
