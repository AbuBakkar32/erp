package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.ProjectStoreView;

@Mapper
public interface ProjectStoreViewMapper {

	public List<ProjectStoreView> getProjectStores(String zid);

	public List<ProjectStoreView> getProjectStoresByXproject(String xproject, String zid);

	public List<ProjectStoreView> getProjectStoresByXtype(String xproject, String zid);
}
