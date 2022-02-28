package com.asl.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.DataList;
import com.asl.entity.ListHead;
import com.asl.mapper.ListMapper;
import com.asl.service.ListService;

/**
 * @author Zubayer Ahamed
 * @since Dec 04, 2020
 */
@Service
public class ListServiceImpl extends AbstractGenericService implements ListService {

	@Autowired private ListMapper listMapper;

	@Override
	public String modifiedListcode(String listcode) {
		if(StringUtils.isBlank(listcode)) return "";
		listcode = listcode.trim();
		listcode = listcode.toUpperCase();
		listcode = listcode.replace(" ", "_");
		return listcode;
	}

	@Transactional
	@Override
	public long saveListHead(ListHead listHead) {
		return saveListHead(listHead, sessionManager.getBusinessId(), getAuditUser());
	}

	@Transactional
	@Override
	public long saveListHead(ListHead listHead, String zid, String auditUser) {
		if(listHead == null) return 0;
		listHead.setListcode(modifiedListcode(listHead.getListcode()));
		listHead.setZid(zid);
		listHead.setZauserid(auditUser);
		return listMapper.saveListHead(listHead);
	}

	@Transactional
	@Override
	public long updateListHead(ListHead listHead) {
		return updateListHead(listHead, sessionManager.getBusinessId(), getAuditUser());
	}

	@Transactional
	@Override
	public long updateListHead(ListHead listHead, String zid, String audituser) {
		if(listHead == null) return 0;
		listHead.setListcode(modifiedListcode(listHead.getListcode()));
		listHead.setZid(zid);
		listHead.setZuuserid(audituser);
		return listMapper.updateListHead(listHead);
	}
	
	@Transactional
	@Override
	public long saveDataList(DataList dataList) {
		return saveDataList(dataList, sessionManager.getBusinessId(), getAuditUser());
	}

	@Transactional
	@Override
	public long saveDataList(DataList dataList, String zid, String auditUser) {
		if(dataList == null) return 0;
		dataList.setZid(zid);
		dataList.setZauserid(auditUser);
		return listMapper.saveDataList(dataList);
	}

	@Transactional
	@Override
	public long updateDataList(DataList dataList) {
		return updateDataList(dataList, sessionManager.getBusinessId());
	}

	@Override
	public long updateDataList(DataList dataList, String zid) {
		if(dataList == null) return 0;
		dataList.setZid(zid);
		dataList.setZuuserid(getAuditUser());
		return listMapper.updateDataList(dataList);
	}

	@Override
	public ListHead findListHeadByListcode(String listcode) {
		return findListHeadByListcode(listcode, sessionManager.getBusinessId());
	}

	@Override
	public ListHead findListHeadByListcode(String listcode, String zid) {
		if(StringUtils.isBlank(listcode) || StringUtils.isBlank(zid)) return null;
		return listMapper.findListHeadByListcode(listcode, zid);
	}

	@Override
	public DataList findDataListByListcodeAndListid(int listid, String listcode) {
		return listMapper.findDataListByListcodeAndListid(listid, listcode, sessionManager.getBusinessId());
	}

	@Override
	public DataList findDataListByListcodeAndListid(int listid, String listcode, String zid) {
		if(StringUtils.isBlank(listcode) || StringUtils.isBlank(zid)) return null;
		return listMapper.findDataListByListcodeAndListid(listid, listcode, zid);
	}


	@Override
	public List<DataList> findDataListByListcode(String listcode) {
		return findDataListByListcode(listcode, sessionManager.getBusinessId());
	}

	@Override
	public List<DataList> findDataListByListcode(String listcode, String zid) {
		if(StringUtils.isBlank(listcode) || StringUtils.isBlank(zid)) return Collections.emptyList();
		List<DataList> list = listMapper.findDataListByListCode(listcode, zid);
		if(list == null) return Collections.emptyList();
		return list;
	}

	@Override
	public List<DataList> getList(String listcode, String... values){
		if(StringUtils.isBlank(listcode)) return Collections.emptyList();

		Map<String, String> paramMap = new HashMap<>();
		if(values != null) {
			int index = 0;
			for(String value : values) {
				index++;
				if(StringUtils.isBlank(value)) continue;
				paramMap.put("listvalue" + index, value);
			}
		}

		List<DataList> resultList = listMapper.getList(listcode, sessionManager.getBusinessId(), paramMap);
		return resultList != null ? resultList : Collections.emptyList();
	}


	@Override
	public List<ListHead> getAllListHead() {
		return getAllListHead(sessionManager.getBusinessId());
	}

	@Override
	public List<ListHead> getAllListHead(String zid) {
		if(StringUtils.isBlank(zid)) return Collections.emptyList();
		List<ListHead> list = listMapper.getAllListHead(zid);
		if(list == null) return Collections.emptyList();
		return list;
	}

	@Transactional
	@Override
	public long deleteListHead(String listcode) {
		return deleteListHead(listcode, sessionManager.getBusinessId());
	}

	@Transactional
	@Override
	public long deleteListHead(String listcode, String zid) {
		if(StringUtils.isBlank(listcode) || StringUtils.isBlank(zid)) return 0;
		return listMapper.deleteListHead(listcode, zid);
	}

	@Transactional
	@Override
	public long deleteDataList(int listid, String listcode) {
		return deleteDataList(listid, listcode, sessionManager.getBusinessId());
	}

	@Transactional
	@Override
	public long deleteDataList(int listid, String listcode, String zid) {
		if(StringUtils.isBlank(listcode) || StringUtils.isBlank(zid)) return 0;
		return listMapper.deleteDataList(listid, listcode, zid);
	}

	@Override
	public List<Map<String, Object>> getExportDataByChunk(long limit, long offset, List<String> listcodes, String zid) {
		return listMapper.getExportDataByChunk(limit, offset, listcodes, zid);
	}

	@Override
	public long deleteDataList(String listcode, String zid) {
		if(StringUtils.isBlank(listcode) || StringUtils.isBlank(zid)) return 0;
		return listMapper.deleteDataListByListCode(listcode, zid);
	}

	

}