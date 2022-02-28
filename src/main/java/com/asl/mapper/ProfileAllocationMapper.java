package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.ProfileAllocation;

/**
 * @author Zubayer Ahamed
 * @since Feb 17, 2021
 */
@Mapper
public interface ProfileAllocationMapper {

	public long save(ProfileAllocation profileAllocation);
	public long update(ProfileAllocation profileAllocation);
	public ProfileAllocation findByPaid(String paid, String zid);
	public List<ProfileAllocation> getAllProfileAllocation(String zid);
	public ProfileAllocation findByZemail(String zemail, String zid);

}
