package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.ProcErrorLog;
import com.asl.mapper.ProcErrorLogMapper;
import com.asl.service.ProcErrorLogService;

/**
 * @author Zubayer Ahamed
 * @since Mar 20, 2021
 */
@Service
public class ProcErrorLogServiceImpl extends AbstractGenericService implements ProcErrorLogService {

	@Autowired
	private ProcErrorLogMapper procErrorLogMapper;

	@Override
	public List<ProcErrorLog> findByAction(String action) {
		if (StringUtils.isBlank(action))
			return Collections.emptyList();
		return procErrorLogMapper.findByAction(action, sessionManager.getBusinessId());
	}

}
