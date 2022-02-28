package com.asl.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.mapper.FormPagingMapper;
import com.asl.service.FormPagingService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Feb 20, 2021
 */
@Slf4j
@Service
public class FormPagingServiceImpl extends AbstractGenericService implements FormPagingService {

	@Autowired private FormPagingMapper formPagingMapper;

	@Override
	public Long getExpectedRecord(String tableName, String columnName, String currentId, String direction) {
		if(StringUtils.isBlank(columnName) || StringUtils.isBlank(tableName) || StringUtils.isBlank(currentId) || StringUtils.isBlank(direction)) return null;

		String dir = "top";
		if("prev".equalsIgnoreCase(direction)) dir = "bottom";

		Long dataId = null;
		if("start".equalsIgnoreCase(currentId)) return getSeilingRecord(tableName, columnName, dir);

		try {
			dataId = formPagingMapper.getExpectedRecord(tableName, columnName, currentId, direction);
		} catch (Exception e) {
			log.error(ERROR, e.getMessage(), e);
		}

		if(dataId == null) dataId = getSeilingRecord(tableName, columnName, dir);
		return dataId;
	}

	@Override
	public Long getSeilingRecord(String tableName, String columnName, String direction) {
		if(StringUtils.isBlank(columnName) || StringUtils.isBlank(tableName) || StringUtils.isBlank(direction)) return null;
		String order = "ASC";
		if("bottom".equalsIgnoreCase(direction)) order = "DESC";
		return formPagingMapper.getSeilingRecord(tableName, columnName, order);
	}
}
