package com.asl.service;

import java.util.List;

import com.asl.entity.ProfileLine;
import com.asl.enums.ProfileType;

/**
 * @author Zubayer Ahamed
 * @since Dec 3, 2020
 */
public interface ProfileLineService {

	long save(ProfileLine profileLine);

	long update(ProfileLine profileLine);

	long updateAllProfileLines(String profilecode, ProfileType profiletype, boolean display);

	ProfileLine findByProfilelineid(String profilelineid);

	ProfileLine findByProfilelinecodeAndProfilecodeAndProfiletype(String profilelinecode, String profilecode, ProfileType profiletype);

	List<ProfileLine> getAllByProfilecodeAndProfiletype(String profilecode, ProfileType profiletype);
}
