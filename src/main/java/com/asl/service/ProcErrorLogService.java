package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.ProcErrorLog;

/**
 * @author Zubayer Ahamed
 * @since Mar 20, 2021
 */
@Component
public interface ProcErrorLogService {

	public List<ProcErrorLog> findByAction(String action);
}
