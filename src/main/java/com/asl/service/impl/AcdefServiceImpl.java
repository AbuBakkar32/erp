package com.asl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Acdef;
import com.asl.mapper.AcdefMapper;
import com.asl.service.AcdefService;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2021
 */
@Service
public class AcdefServiceImpl extends AbstractGenericService implements AcdefService {

	@Autowired private AcdefMapper acdefMapper;

	@Transactional
	@Override
	public long save(Acdef acdef) {
		if(acdef == null) return 0;
		acdef.setZid(sessionManager.getBusinessId());
		return acdefMapper.save(acdef);
	}

	@Transactional
	@Override
	public long update(Acdef acdef) {
		if(acdef == null) return 0;
		acdef.setZid(sessionManager.getBusinessId());
		return acdefMapper.update(acdef);
	}

	@Override
	public Acdef find() {
		return acdefMapper.find(sessionManager.getBusinessId());
	}

}
