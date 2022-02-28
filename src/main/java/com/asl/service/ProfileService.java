package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Profile;
import com.asl.enums.ProfileType;
import com.asl.model.MenuProfile;
import com.asl.model.ReportProfile;

/**
 * @author Zubayer Ahamed
 * @since Dec 1, 2020
 */
@Component
public interface ProfileService {

	long save(Profile profile);

	long update(Profile profile);

	Profile findByProfilecode(String profilecode);

	Profile findByProfilecodeAndProfiletype(String profilecode, ProfileType profiletype);

	public List<Profile> getAllLiveProfiles();

	List<Profile> getAllProfiles();

	List<Profile> getAllProfilesByProfiletype(ProfileType profiletype);

	MenuProfile getDefaultMenuProfile();

	ReportProfile getDefaultReportProfile();

	MenuProfile getLoggedInUserMenuProfile();

	ReportProfile getLoggedInUserReportProfile();

	MenuProfile getMenuProfileByProfilecode(String profilecode);

	ReportProfile getReportProfileByProfilecode(String profilecode);

	String modifiedProfileCode(String pc);
}
