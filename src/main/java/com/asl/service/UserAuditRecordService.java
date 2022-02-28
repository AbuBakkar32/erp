package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.UserAuditRecord;

/**
 * @author Zubayer Ahamed
 * @since Jan 12, 2021
 */
@Component
public interface UserAuditRecordService {

	public long save(UserAuditRecord userAuditRecord);

	public List<UserAuditRecord> findByUserId(String userId);

	public List<UserAuditRecord> getAll();
}
