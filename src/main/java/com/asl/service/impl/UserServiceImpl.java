package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Xusers;
import com.asl.mapper.XusersMapper;
import com.asl.service.XusersService;

/**
 * @author Zubayer Ahamed
 * @since Dec 30, 2020
 */
@Service
public class UserServiceImpl extends AbstractGenericService implements XusersService {

	@Autowired
	private XusersMapper userMapper;

	@Transactional
	@Override
	public long save(Xusers xusers) {
		if (xusers == null) return 0;
		xusers.setZemail(modifyZemail(xusers.getZemail()));
		xusers.setZid(sessionManager.getBusinessId());
		xusers.setZauserid(getAuditUser());
		return userMapper.save(xusers);
	}

	@Transactional
	@Override
	public long update(Xusers user) {
		if (user == null) return 0;
		user.setZid(sessionManager.getBusinessId());
		user.setZuuserid(getAuditUser());
		return userMapper.update(user);
	}

	@Transactional
	@Override
	public long delete(Xusers user) {
		if (user == null) return 0;
		return userMapper.delete(user);
	}

	@Override
	public String modifyZemail(String username) {
		if (StringUtils.isBlank(username)) return "";
		username = username.trim();
		username = username.replace(" ", "-");
		return username;
	}

	@Override
	public Xusers findUserByZemail(String zemail) {
		if(StringUtils.isBlank(zemail)) return null;
		return userMapper.findUserByZemail(zemail, sessionManager.getBusinessId());
	}

	@Override
	public List<Xusers> findAllUserByZemail(String zemail) {
		if (StringUtils.isBlank(zemail)) return Collections.emptyList();

		List<Xusers> list = userMapper.findAllUserByZemail(zemail);
		if (list == null) return Collections.emptyList();
		return list;
	}

	@Override
	public List<Xusers> findByZemailAndXpassword(String zemail, String xpassword) {
		if (StringUtils.isBlank(zemail) || StringUtils.isBlank(xpassword)) return Collections.emptyList();

		List<Xusers> list = userMapper.findByZemailAndXpassword(zemail, xpassword);
		if (list == null) return Collections.emptyList();
		return list;
	}

	@Override
	public Xusers findByZemailAndZid(String zemail, String zid) {
		if (StringUtils.isBlank(zemail) || StringUtils.isBlank(zid)) return null;
		return userMapper.findByZemailAndZid(zemail, zid);
	}

	@Override
	public List<Xusers> getAllXusers() {
		List<Xusers> resultList = userMapper.getAllXusers(sessionManager.getBusinessId());
		if (resultList == null) return Collections.emptyList();
		return resultList;
	}

	@Override
	public Xusers findUserByXstaff(String xstaff) {
		if(StringUtils.isBlank(xstaff)) return null;
		return userMapper.findUserByXstaff(xstaff, sessionManager.getBusinessId());
	}

	@Override
	public Xusers findUserByXcus(String xcus) {
		if(StringUtils.isBlank(xcus)) return null;
		return userMapper.findUserByXcus(xcus, sessionManager.getBusinessId());
	}

}
