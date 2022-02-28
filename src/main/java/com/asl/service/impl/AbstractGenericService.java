package com.asl.service.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.asl.config.AppConfig;
import com.asl.entity.Arhed;
import com.asl.entity.DataList;
import com.asl.entity.ProcErrorLog;
import com.asl.entity.Zbusiness;
import com.asl.enums.Modules;
import com.asl.service.ASLSessionManager;
import com.asl.service.ListService;
import com.asl.service.ProcErrorLogService;

/**
 * @author Zubayer Ahamed
 * @since Dec 04, 2020
 */
@Component
public abstract class AbstractGenericService  {

	protected static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	protected static final String ERROR = "Error is {}, {}";
	@Autowired protected ASLSessionManager sessionManager;
	@Autowired protected AppConfig appConfig;
	@Autowired protected ProcErrorLogService errorService;
	@Autowired protected ListService listService;

	/**
	 * Generate slug name
	 * @param name
	 * @return {@link String}
	 */
	protected String generateSlug(String name) {
		if(StringUtils.isBlank(name)) return "NOSULG";
		name = name.trim().toLowerCase();
		name = name.replace(" ", "-");
		return name;
	}

	protected String getBusinessId() {
		Zbusiness z = sessionManager.getZbusiness();
		if(Boolean.TRUE.equals(z.getCentral())) return z.getZid();
		return z.getCentralzid();
	}

	protected String getAuditUser() {
		return sessionManager.getLoggedInUserDetails().getUsername();
	}
	
	public List<Arhed> getAllAdars() {
		// TODO Auto-generated method stub
		return null;
	}

	protected String getProcedureErrorMessages(String errorCode) {
		List<ProcErrorLog> errors = errorService.findByAction(errorCode);

		if(errors == null || errors.isEmpty()) return null;

		StringBuilder message = new StringBuilder();
		errors.stream().forEach(e -> {
			message.append(e.getOsqlCode() + " - " + e.getErrorMessage());
		});
		return message.toString();
	}

	protected boolean isModuleActive(Modules module) {
		List<DataList> dl = listService.getList("SYSTEM", "MODULE");
		if(dl == null || dl.isEmpty()) return true;
		for(DataList d : dl) {
			if(module.name().equalsIgnoreCase(d.getListvalue2()) && "Y".equalsIgnoreCase(d.getListvalue4())) return true;
		}
		return false;
	}
}
