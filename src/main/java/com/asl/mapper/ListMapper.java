package com.asl.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.DataList;
import com.asl.entity.ListHead;

/**
 * @author Zubayer Ahamed
 * @since Feb 16, 2021
 */
@Mapper
public interface ListMapper {

	public long saveListHead(ListHead listHead);

	public long saveDataList(DataList dataList);

	public long updateListHead(ListHead listHead);

	public long updateDataList(DataList dataList);

	public long deleteListHead(String listcode, String zid);

	public long deleteDataList(int listid, String listcode, String zid);

	public long deleteDataListByListCode(String listcode, String zid);

	public ListHead findListHeadByListcode(String listcode, String zid);

	public DataList findDataListByListcodeAndListid(int listid, String listcode, String zid);

	public List<DataList> findDataListByListCode(String listcode, String zid);

	public List<DataList> getList(String listcode, String zid, Map<String, String> params);

	public List<ListHead> getAllListHead(String zid);

//	public List<ListHead> getExportDataByChunk(long limit, long offset, String zid);
	
	public List<Map<String, Object>> getExportDataByChunk(long limit, long page, List<String> listcodes, String zid);
}
