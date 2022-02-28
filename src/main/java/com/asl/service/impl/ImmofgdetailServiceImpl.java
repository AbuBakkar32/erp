package com.asl.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.Immofgdetail;
import com.asl.mapper.ImmofgdetailMapper;
import com.asl.service.ImmofgdetailService;

/**
 * @author Zubayer Ahamed
 * @since Mar 20, 2021
 */
@Service
public class ImmofgdetailServiceImpl extends AbstractGenericService implements ImmofgdetailService {

	@Autowired private ImmofgdetailMapper immofgdetailMapper;

	@Override
	public long save(Immofgdetail immofgdetail) {
		if(immofgdetail == null) return 0;
		immofgdetail.setZid(sessionManager.getBusinessId());
		immofgdetail.setZauserid(getAuditUser());
		return immofgdetailMapper.save(immofgdetail);
	}

	@Override
	public long update(Immofgdetail immofgdetail) {
		if(immofgdetail == null) return 0;
		immofgdetail.setZid(sessionManager.getBusinessId());
		immofgdetail.setZuuserid(getAuditUser());
		return immofgdetailMapper.update(immofgdetail);
	}

	@Override
	public Immofgdetail findImmofgDetailByXtornumAndXrow(String xtornum, int xrow) {
		if(StringUtils.isBlank(xtornum) || xrow == 0) return null;
		return immofgdetailMapper.findImmofgDetailByXtornumAndXrow(xtornum, xrow, sessionManager.getBusinessId());
	}

	@Override
	public List<Immofgdetail> getAllImmofgDetail() {
		return immofgdetailMapper.getAllImmofgDetail(sessionManager.getBusinessId());
	}

}
