package com.asl.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.Caitem;
import com.asl.entity.DataList;
import com.asl.enums.ResponseStatus;
import com.asl.model.pagination.PaginationQueryHelper;
import com.asl.model.pagination.QueryColumn;
import com.asl.model.pagination.TableBody;
import com.asl.service.ListService;
import com.asl.service.PaginationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Feb 22, 2021
 */
@Slf4j
@Controller
@RequestMapping("/pagination")
public class PaginationController extends ASLAbstractController {

	@Autowired private PaginationService paginationService;
	@Autowired private ListService listService;

	@GetMapping
	public String loadPagingTaleWithData(Model model) {

//		PaginationHelper pt = paginationService.getPagingTable();
//		System.out.println(pt.toString());
//
//		PaginationHelper pagingTable = new PaginationHelper();

//		pagingTable.getHeaders().add(Collections.singletonList(new HeaderColumn(1, "SL")).get(0));
//		pagingTable.getHeaders().add(Collections.singletonList(new HeaderColumn(2, "COL1")).get(0));
//		pagingTable.getHeaders().add(Collections.singletonList(new HeaderColumn(3, "COL2")).get(0));

//		List<List<BodyRecord>> records = new ArrayList<>();
//		for(int j = 1; j <= 10; j++) {
//			List<BodyRecord> record = new ArrayList<>();
//			for(int i = 1; i <= 3; i++) {
//				record.add(new BodyRecord(i, "rec" + j + " - val" + i));
//			}
//			records.add(record);
//		}
//		pagingTable.setRecords(records);
//
//		model.addAttribute("pagingTable", pagingTable);
		
		
		List<DataList> lists = listService.getList("SERVER-SIDE-PAGINATION", "ITEM_MASTER");
		DataList dl = lists.get(0);
		StringBuilder sql = new StringBuilder("select ");
		sql.append(dl.getListvalue2());
		sql.append(" from ");
		sql.append(dl.getListvalue3());
		sql.append(" where ");
		sql.append(dl.getListvalue4());
		sql.append(" order by ");
		sql.append(dl.getListvalue5());
		
		
		String query = sql.toString();
		List<Caitem> caitem = paginationService.getAllCaitems(query);
		caitem.stream().forEach(c -> {
			System.out.println(c.toString());
		});
		
		
		return "pages/pagination/pagination";
	}

	@PostMapping(value = "/fetchdata", headers="Accept=application/json")
	public @ResponseBody Map<String, Object> fetchData(@RequestBody String json, Model model){

		PaginationQueryHelper paginationHelper = new PaginationQueryHelper();
		System.out.println(json);

		ObjectMapper obm = new ObjectMapper();
		obm.setDateFormat(SDF2);
		try {
			paginationHelper = obm.readValue(json, PaginationQueryHelper.class);
			System.out.println(paginationHelper.toString());
		} catch (JsonProcessingException e) {
			log.error(ERROR, e.getMessage(), e);
			responseHelper.setErrorStatusAndMessage(e.getMessage());
			return responseHelper.getResponse();
		}

		StringBuilder sqlQuery = new StringBuilder();
		sqlQuery.append("SELECT ");
		// SELECT Query
		prepareSelectColumns(sqlQuery, paginationHelper);
		prepareFromCondition(sqlQuery, paginationHelper);
		prepareWhereCondition(sqlQuery, paginationHelper);
		prepareOrderByClause(sqlQuery, paginationHelper);

		StringBuilder sqlCountQuery = new StringBuilder("SELECT COUNT(*) ");
		prepareFromCondition(sqlCountQuery, paginationHelper);
		prepareWhereCondition(sqlCountQuery, paginationHelper);
		prepareOrderByClause(sqlCountQuery, paginationHelper);

		System.out.println(sqlQuery.toString());

		List<Map<String, Object>> lst = paginationService.getData(sqlQuery.toString(), paginationHelper.getDataLimit(), paginationHelper.getDataPage());
		long count = paginationService.getCountOfData(sqlCountQuery.toString());

		paginationHelper.getQueryColumns().sort(Comparator.comparing(QueryColumn::getIndex));
		List<Map<String, Object>> lst2 = new ArrayList<>();
		for(Map<String, Object> map : lst) {
			Map<String, Object> dataMap = new TreeMap<>();

			for(QueryColumn qc : paginationHelper.getQueryColumns()) {
				dataMap.put(String.valueOf(qc.getIndex()), map.get(qc.getColumn().toUpperCase()));
			}

			lst2.add(dataMap);
		}

		responseHelper.addDataToResponse("totalData", count);
		responseHelper.addDataToResponse("tableBodyData", lst2);
		responseHelper.setReloadSectionIdWithUrl("paginationbody", "/pagination/loadtablebody");
		responseHelper.setStatus(ResponseStatus.SUCCESS);
		responseHelper.setDisplayMessage(false);
		return responseHelper.getResponse();
	}
	
	
	@PostMapping(value = "/loadtablebody")
	public String loadTableBody(TableBody tableBody, Model model) {

		
		tableBody.getLst().stream().forEach(map -> {
			map.entrySet().stream().forEach(m -> {
				System.out.println(m.getKey() + " - " + m.getValue());
			});
		});
		
		
		
		
		model.addAttribute("bodydata", tableBody.getLst());

		return "pages/pagination/pagination::paginationbody";
	}
	
	
	private void prepareSelectColumns(StringBuilder sqlQuery, PaginationQueryHelper paginationHelper) {
		StringBuilder selectedColumns = new StringBuilder(); 
		paginationHelper.getQueryColumns().stream().forEach(qc -> {
			if(StringUtils.isNotBlank(qc.getPrefix())) selectedColumns.append(qc.getPrefix() + ".");
			selectedColumns.append(qc.getColumn() + ", ");
		});
		sqlQuery.append(selectedColumns.toString().substring(0, selectedColumns.toString().lastIndexOf(",")));
	}

	private void prepareFromCondition(StringBuilder sqlQuery, PaginationQueryHelper paginationHelper) {
		sqlQuery.append(" FROM " + paginationHelper.getBaseTable() + " " + paginationHelper.getBaseTableAlias());
		sqlQuery.append(paginationHelper.getJoinCondition());
	}

	private void prepareWhereCondition(StringBuilder sqlQuery, PaginationQueryHelper paginationHelper) {
		if(StringUtils.isNotBlank(paginationHelper.getBaseTableAlias())) {
			sqlQuery.append(" WHERE " + paginationHelper.getBaseTableAlias() + ".zid=" + sessionManager.getBusinessId());
		} else {
			sqlQuery.append(" WHERE zid=" + sessionManager.getBusinessId());
		}
		sqlQuery.append(" " + paginationHelper.getWhereCondition());
	}
	
	private void prepareOrderByClause(StringBuilder sqlQuery, PaginationQueryHelper paginationHelper) {
		StringBuilder orderByColumns = new StringBuilder(); 
		Arrays.asList(paginationHelper.getOrderByColumns()).stream().forEach(c -> {
			orderByColumns.append(c + ", ");
		});
		sqlQuery.append(" ORDER BY " + orderByColumns.toString().substring(0, orderByColumns.toString().lastIndexOf(",")));
	}
	
}








