package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.UserAuditRecord;

/**
 * @author Zubayer Ahamed
 * @since Feb 17, 2021
 */
@Mapper
public interface UserAuditRecordMapper {

	long save(UserAuditRecord userAuditRecord);
	List<UserAuditRecord> findByUserId(String userId, String zid);
	public List<UserAuditRecord> getAll(String zid);
}
