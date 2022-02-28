package com.asl.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.Vatait;
import com.asl.mapper.VataitMapper;
import com.asl.service.VataitService;

@Service
public class VataitServiceImpl extends AbstractGenericService implements VataitService {
	
	@Autowired
	private VataitMapper vataitMapper;

	@Override
	public long save(Vatait vatait) {
		if (vatait == null)
			return 0;
		vatait.setZid(sessionManager.getBusinessId());
		vatait.setZauserid(getAuditUser());
		return vataitMapper.saveVatait(vatait);
	}

	@Override
	public long update(Vatait vatait) {
		if (vatait == null)
			return 0;
		vatait.setZid(sessionManager.getBusinessId());
		vatait.setZuuserid(getAuditUser());
		return vataitMapper.updateVatait(vatait);
	}

	@Override
	public long delete(Vatait vatait) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Vatait findVataitByXvatait(String xvatait) {
		if (StringUtils.isBlank(xvatait)) return null;
		return vataitMapper.findVataitByXvatait(xvatait, sessionManager.getBusinessId());
	}

	@Override
	public List<Vatait> getAllVatait() {
		return vataitMapper.getAllVatait(sessionManager.getBusinessId());
	}

}
