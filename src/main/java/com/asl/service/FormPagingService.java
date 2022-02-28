package com.asl.service;

import org.springframework.stereotype.Component;

/**
 * @author Zubayer Ahamed
 * @since Feb 20, 2021
 */
@Component
public interface FormPagingService {

	public Long getExpectedRecord(String tableName, String columnName, String currentId, String direction);

	public Long getSeilingRecord(String tableName, String columnName, String direction);
}
