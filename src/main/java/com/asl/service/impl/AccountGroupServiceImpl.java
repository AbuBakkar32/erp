package com.asl.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.AccountGroup;
import com.asl.mapper.AccountGroupMapper;
import com.asl.service.AccountGroupService;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2021
 */
@Service
public class AccountGroupServiceImpl extends AbstractGenericService implements AccountGroupService {

	@Autowired AccountGroupMapper accountGroupMapper;

	@Transactional
	@Override
	public long save(AccountGroup accountGroup) {
		if(accountGroup == null) return 0;
		accountGroup.setZid(sessionManager.getBusinessId());
		accountGroup.setZauserid(getAuditUser());
		return accountGroupMapper.save(accountGroup);
	}

	@Transactional
	@Override
	public long update(AccountGroup accountGroup) {
		if(accountGroup == null) return 0;
		accountGroup.setZid(sessionManager.getBusinessId());
		accountGroup.setZuuserid(getAuditUser());
		return accountGroupMapper.update(accountGroup);
	}

	@Transactional
	@Override
	public long delete(String xagcode) {
		if(StringUtils.isBlank(xagcode)) return 0;
		return accountGroupMapper.delete(xagcode, sessionManager.getBusinessId());
	}

	@Override
	public List<AccountGroup> getAllByLevelAndType(int xaglevel, String xagtype) {
		if(StringUtils.isBlank(xagtype)) return Collections.emptyList();
		return accountGroupMapper.getAllByLevelAndType(xaglevel, xagtype, sessionManager.getBusinessId());
	}

	@Override
	public AccountGroup findByCode(String xagcode) {
		if(StringUtils.isBlank(xagcode)) return null;
		return accountGroupMapper.findByCode(xagcode, sessionManager.getBusinessId());
	}

	@Override
	public List<AccountGroup> getAllByLevel(int xaglevel) {
		return accountGroupMapper.getAllByLevel(xaglevel, sessionManager.getBusinessId());
	}

	@Override
	public List<AccountGroup> getAllByXagparent(String xagparent) {
		if(StringUtils.isBlank(xagparent)) return Collections.emptyList();
		return accountGroupMapper.getAllByXagparent(xagparent, sessionManager.getBusinessId());
	}

	@Override
	public List<AccountGroup> searchByCodeOrName(String hint) {
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		return accountGroupMapper.searchByCodeOrName(hint.toUpperCase(), sessionManager.getBusinessId());
	}

	@Override
	public Map<String, AccountGroup> getAccountGroupHirerkey(String xgroup) {
		if(StringUtils.isBlank(xgroup)) return Collections.emptyMap();

		Map<String, AccountGroup> hirerkey = new HashMap<>();
		AccountGroup acgroup = findByCode(xgroup);
		if(acgroup != null) prepareAccountGroupHirerkey(hirerkey, acgroup);

		return hirerkey;
	}

	private Map<String, AccountGroup> prepareAccountGroupHirerkey(Map<String, AccountGroup> hirerkey, AccountGroup acgroup){
		hirerkey.put("LEVEL_" + acgroup.getXaglevel(), acgroup);

		if(acgroup.getXaglevel() == 1) return hirerkey;

		AccountGroup nextacgroup = findByCode(acgroup.getXagparent());
		if(nextacgroup != null) prepareAccountGroupHirerkey(hirerkey, nextacgroup);

		return hirerkey;
	}

}
