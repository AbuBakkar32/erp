package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Xusers;

/**
 * @author Zubayer Ahamed
 * @since Feb 16, 2021
 */
@Mapper
public interface XusersMapper {

	public long save(Xusers xusers);

	public long update(Xusers xusers);
	
	public long delete(Xusers xusers);

	public List<Xusers> getAllXusers(String zid);

	public Xusers findUserByZemail(String zemail, String zid);

	public List<Xusers> findAllUserByZemail(String zemail);

	public Xusers findByZemailAndZid(String zemail, String zid);

	public List<Xusers> findByZemailAndXpassword(String zemail, String xpassword);

	public Xusers findUserByXstaff(String xstaff, String zid);

	public Xusers findUserByXcus(String xcus, String zid);
}
