package com.asl.service;

import java.util.List;

import com.asl.entity.ProjectStoreView;

/**
 * @author Zubayer Ahamed
 * @since Jan 25, 2022
 */
public interface ProjectStoreViewService {

	public List<ProjectStoreView> getProjectStores();

	public List<ProjectStoreView> getProjectStoresByXproject(String xproject);

	public List<ProjectStoreView> getProjectStoresByXtype(String xproject);
}
