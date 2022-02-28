package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.ProcErrorLog;

/**
 * @author Zubayer Ahamed
 * @since Mar 20, 2021
 */
@Mapper
public interface ProcErrorLogMapper {

	public List<ProcErrorLog> findByAction(String action, String zid);
}
