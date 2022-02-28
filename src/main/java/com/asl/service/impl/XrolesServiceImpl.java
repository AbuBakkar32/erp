package com.asl.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.Xroles;
import com.asl.mapper.XRolesMapper;
import com.asl.service.XRolesService;

@Service
public class XrolesServiceImpl extends AbstractGenericService implements XRolesService {

	@Autowired
	private XRolesMapper xRolesMapper;

	@Override
	public long save(Xroles xroles) {
		if (xroles == null) return 0;
		xroles.setZid(sessionManager.getBusinessId());
		xroles.setZauserid(getAuditUser());
		return xRolesMapper.saveXroles(xroles);
	}

	@Override
	public long update(Xroles xroles) {
		if (xroles == null) return 0;
		xroles.setZid(sessionManager.getBusinessId());
		xroles.setZuuserid(getAuditUser());
		return xRolesMapper.updateXroles(xroles);
	}



	@Override
	public List<Xroles> getAllXroles() {
		return xRolesMapper.getAllXroles(sessionManager.getBusinessId());
	}

	@Override
	public Xroles findByXrole(String xrole) {
		if(StringUtils.isBlank(xrole)) return null;
		return xRolesMapper.findByXrole(xrole, sessionManager.getBusinessId());
	}

}
