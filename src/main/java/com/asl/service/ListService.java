package com.asl.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.asl.entity.DataList;
import com.asl.entity.ListHead;

/**
 * @author Zubayer Ahamed
 * @since Dec 04, 2020
 */
@Component
public interface ListService {

	public String modifiedListcode(String listcode);

	public long saveListHead(ListHead listHead);

	public long saveListHead(ListHead listHead, String zid, String auditUser);

	public long updateListHead(ListHead listHead);

	public long updateListHead(ListHead listHead, String zid, String auditUser);

	public long deleteListHead(String listcode);

	public long deleteListHead(String listcode, String zid);

	public long saveDataList(DataList dataList);

	public long saveDataList(DataList dataList, String zid, String auditUser);

	public long updateDataList(DataList dataList);

	public long updateDataList(DataList dataList, String zid);

	public long deleteDataList(String listcode, String zid);

	public long deleteDataList(int listid, String listcode);

	public long deleteDataList(int listid, String listcode, String zid);

	public ListHead findListHeadByListcode(String listcode);

	public ListHead findListHeadByListcode(String listcode, String zid);

	public DataList findDataListByListcodeAndListid(int listid, String listcode);

	public DataList findDataListByListcodeAndListid(int listid, String listcode, String zid);

	public List<DataList> findDataListByListcode(String listcode);

	public List<DataList> findDataListByListcode(String listcode, String zid);

	public List<DataList> getList(String listcode, String... values);

	public List<ListHead> getAllListHead();

	public List<ListHead> getAllListHead(String zid);

	public List<Map<String, Object>> getExportDataByChunk(long limit, long offset, List<String> listcodes, String zid);
}
