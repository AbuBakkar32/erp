package com.asl.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Caitem;

/**
 * @author Zubayer Ahamed
 * @since Feb 23, 2021
 */
@Mapper
public interface PaginationMapper {

	public List<Map<String, Object>> getResultMap(List<String> columns, String zid);

	public List<Caitem> getAllCaitems(String query);

	public List<Map<String, Object>> getData(String query, int limit, int page);

	public long getCountOfData(String query);
}
