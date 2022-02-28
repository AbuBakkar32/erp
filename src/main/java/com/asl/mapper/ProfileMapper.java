package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Profile;
import com.asl.enums.ProfileType;

/**
 * @author Zubayer Ahamed
 * @since Feb 17, 2021
 */
@Mapper
public interface ProfileMapper {

	long save(Profile profile);

	long update(Profile profile);

	Profile findByProfilecode(String profilecode, String zid);

	Profile findByProfileCodeAndProfileType(String profilecode, ProfileType profiletype, String zid);

	List<Profile> getAllProfiles(ProfileType profiletype, String zid, Boolean zactive);
}
