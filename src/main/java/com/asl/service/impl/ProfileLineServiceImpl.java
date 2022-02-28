package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.ProfileLine;
import com.asl.enums.ProfileType;
import com.asl.mapper.ProfileLineMapper;
import com.asl.service.ProfileLineService;

/**
 * @author Zubayer Ahamed
 * @since Dec 3, 2020
 */
@Service
public class ProfileLineServiceImpl extends AbstractGenericService implements ProfileLineService {

	@Autowired private ProfileLineMapper profileLineMapper;

	@Override
	public long save(ProfileLine profileLine) {
		if(profileLine == null) return 0;

		profileLine.setZid(sessionManager.getBusinessId());
		profileLine.setZauserid(getAuditUser());
		return profileLineMapper.save(profileLine);
	}

	@Override
	public long update(ProfileLine profileLine) {
		if(profileLine == null) return 0;

		profileLine.setZid(sessionManager.getBusinessId());
		profileLine.setZuuserid(getAuditUser());
		return profileLineMapper.update(profileLine);
	}

	@Override
	public long updateAllProfileLines(String profilecode, ProfileType profiletype, boolean display) {
		if(StringUtils.isBlank(profilecode) || profiletype == null) return 0;
		return profileLineMapper.updateAllProfileLines(profilecode, profiletype, display, sessionManager.getBusinessId());
	}

	@Override
	public ProfileLine findByProfilelineid(String profilelineid) {
		if(profilelineid == null) return null;
		return profileLineMapper.findByProfilelineid(profilelineid, sessionManager.getBusinessId());
	}

	@Override
	public List<ProfileLine> getAllByProfilecodeAndProfiletype(String profilecode, ProfileType profiletype) {
		if(profilecode == null || profiletype == null) return null;

		List<ProfileLine> list = profileLineMapper.getAllByProfilecodeAndProfiletype(profilecode, profiletype, sessionManager.getBusinessId());
		return list == null ? Collections.emptyList() : list;
	}

	@Override
	public ProfileLine findByProfilelinecodeAndProfilecodeAndProfiletype(String profilelinecode, String profilecode, ProfileType profiletype) {
		if(StringUtils.isBlank(profilelinecode) || StringUtils.isBlank(profilecode) || profiletype == null) return null;
		return profileLineMapper.findByProfilelinecodeAndProfilecodeAndProfiletype(profilelinecode, profilecode, profiletype, sessionManager.getBusinessId());
	}

	
}
