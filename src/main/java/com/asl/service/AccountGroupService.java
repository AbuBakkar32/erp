package com.asl.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.asl.entity.AccountGroup;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2021
 */
@Component
public interface AccountGroupService {

	public long save(AccountGroup accountGroup);

	public long update(AccountGroup accountGroup);

	public long delete(String xagcode);

	public List<AccountGroup> getAllByLevelAndType(int xaglevel, String xagtype);

	public List<AccountGroup> getAllByLevel(int xaglevel);

	public AccountGroup findByCode(String xagcode);

	public List<AccountGroup> getAllByXagparent(String xagparent);

	public List<AccountGroup> searchByCodeOrName(String hint);

	public Map<String, AccountGroup> getAccountGroupHirerkey(String xgroup);
}
