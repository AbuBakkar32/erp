package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.UserAuditRecord;
import com.asl.mapper.UserAuditRecordMapper;
import com.asl.service.UserAuditRecordService;

/**
 * @author Zubayer Ahamed
 * @since Jan 12, 2021
 */
@Service
public class UserAuditRecordServiceImpl extends AbstractGenericService implements UserAuditRecordService {

	@Autowired private UserAuditRecordMapper userAuditRecordMapper;

	@Override
	public long save(UserAuditRecord userAuditRecord) {
		if(userAuditRecord == null) return 0;
		userAuditRecord.setBusinessId(sessionManager.getBusinessId());
		return userAuditRecordMapper.save(userAuditRecord);
	}

	@Override
	public List<UserAuditRecord> findByUserId(String userId) {
		if(StringUtils.isBlank(userId)) return Collections.emptyList();

		List<UserAuditRecord> list = userAuditRecordMapper.findByUserId(userId, sessionManager.getBusinessId());
		if(list == null) return Collections.emptyList();
		return list;
	}

	@Override
	public List<UserAuditRecord> getAll() {
		List<UserAuditRecord> list = userAuditRecordMapper.getAll(sessionManager.getBusinessId());
		if(list == null) return Collections.emptyList();
		return list;
	}

}
