package com.asl.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.asl.entity.Caitem;
import com.asl.model.pagination.PaginationHelper;

/**
 * @author Zubayer Ahamed
 * @since Feb 23, 2021
 */
@Component
public interface PaginationService {

	public PaginationHelper getPagingTable();

	public List<Caitem> getAllCaitems(String query);

	public List<Map<String, Object>> getData(String query, int limit, int page);
	
	public long getCountOfData(String query);
}
