package com.asl.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.ASLProcError;
import com.asl.mapper.ASLProcErrorMapper;
import com.asl.service.ASLProcErrorService;

/**
 * @author Zubayer Ahamed
 * @since Apr 26, 2021
 */
@Service
public class ASLProcErrorServiceImpl extends AbstractGenericService implements ASLProcErrorService {

	@Autowired ASLProcErrorMapper errorMapper;

	@Override
	public List<ASLProcError> getAllProcErrors() {
		return errorMapper.getAllProcErrors(sessionManager.getBusinessId());
	}

}
