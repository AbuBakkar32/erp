package com.asl.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.Caitem;
import com.asl.mapper.PaginationMapper;
import com.asl.model.pagination.PaginationHelper;
import com.asl.service.PaginationService;

/**
 * @author Zubayer Ahamed
 * @since Feb 23, 2021
 */
@Service
public class PaginationServiceImpl extends AbstractGenericService implements PaginationService {

	@Autowired private PaginationMapper paginationMapper;

	@Override
	public PaginationHelper getPagingTable() {
//		PagingTable pt = new PagingTable();
//
//		String tableName = "ListHead";
//		int limit = 10;
//		int offset = 1;
//		String hint = "";
//		List<String> columns = new ArrayList<>(Arrays.asList("listHeadId","listCode","description","status"));
//
//		paginationMapper.getResultMap(columns, sessionManager.getBusinessId()).stream().forEach(r -> {
//			r.entrySet().stream().forEach(m -> {
//				System.out.println(m.getKey() + " --- " + m.getValue());
//			});
//			System.out.println("==== Divider ===");
//		});
//		
		return null;
	}

	@Override
	public List<Caitem> getAllCaitems(String query) {
		return paginationMapper.getAllCaitems(query);
	}

	@Override
	public List<Map<String, Object>> getData(String query, int limit, int page) {
		return paginationMapper.getData(query, limit, page);
	}

	@Override
	public long getCountOfData(String query) {
		return paginationMapper.getCountOfData(query);
	}

}
