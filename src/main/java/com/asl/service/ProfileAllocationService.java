package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.ProfileAllocation;

/**
 * @author Zubayer Ahamed
 * @since Jan 5, 2021
 */
@Component
public interface ProfileAllocationService {

	public long save(ProfileAllocation profileAllocation);

	public long update(ProfileAllocation profileAllocation);

	public ProfileAllocation findByPaid(String paid);

	public List<ProfileAllocation> getAllProfileAllocation();

	public ProfileAllocation findByZemail(String zemail);
}
