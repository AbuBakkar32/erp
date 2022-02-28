package com.asl.mapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author Zubayer Ahamed
 * @since Feb 20, 2021
 */
@Mapper
public interface FormPagingMapper {
	public Long getExpectedRecord(String tableName, String columnName, String currentId, String direction);
	public Long getSeilingRecord(String tableName, String columnName, String direction);
}
