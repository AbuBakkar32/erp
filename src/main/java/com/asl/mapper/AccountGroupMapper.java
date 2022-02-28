package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.AccountGroup;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2021
 */
@Mapper
public interface AccountGroupMapper {

	public long save(AccountGroup accountGroup);

	public long update(AccountGroup accountGroup);

	public long delete(String xagcode, String zid);

	public List<AccountGroup> getAllByLevelAndType(int xaglevel, String xagtype, String zid);

	public List<AccountGroup> getAllByLevel(int xaglevel, String zid);

	public AccountGroup findByCode(String xagcode, String zid);

	public List<AccountGroup> getAllByXagparent(String xagparent, String zid);

	public List<AccountGroup> searchByCodeOrName(String hint, String zid);
}
