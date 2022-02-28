package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.ProjectStoreView;
import com.asl.mapper.ProjectStoreViewMapper;
import com.asl.service.ProjectStoreViewService;

/**
 * @author Zubayer Ahamed
 * @since Jan 25, 2022
 */
@Service
public class ProjectStoreViewServiceImpl extends AbstractGenericService implements ProjectStoreViewService{

	@Autowired private ProjectStoreViewMapper projectStoreViewMapper;

	@Override
	public List<ProjectStoreView> getProjectStores() {
		return projectStoreViewMapper.getProjectStores(sessionManager.getBusinessId());
	}

	@Override
	public List<ProjectStoreView> getProjectStoresByXproject(String xproject) {
		if(StringUtils.isBlank(xproject)) return Collections.emptyList();
		return projectStoreViewMapper.getProjectStoresByXproject(xproject, sessionManager.getBusinessId());
	}

	@Override
	public List<ProjectStoreView> getProjectStoresByXtype(String xproject) {
		if(StringUtils.isBlank(xproject)) return Collections.emptyList();
		return projectStoreViewMapper.getProjectStoresByXtype(xproject, sessionManager.getBusinessId());
	}

}
