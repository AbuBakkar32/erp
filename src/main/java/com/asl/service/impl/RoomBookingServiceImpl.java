package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.ConventionBookedDetails;
import com.asl.mapper.OpordMapper;
import com.asl.service.RoomBookingService;

/**
 * @author Zubayer Ahamed
 * @since May 30, 2021
 */
@Service
public class RoomBookingServiceImpl extends AbstractGenericService implements RoomBookingService {

	@Autowired private OpordMapper opordMapper;

	@Override
	public List<String> allBookedRoomsInDateRange(String xcatitem, String xstartdate, String xenddate, String xordernum) {
		if(StringUtils.isBlank(xcatitem) || StringUtils.isBlank(xstartdate) || StringUtils.isBlank(xenddate)) return Collections.emptyList();
		return opordMapper.allBookedHallsInDateRange(xstartdate, xenddate, xordernum, sessionManager.getBusinessId());
	}

	@Override
	public List<ConventionBookedDetails> allBookedRoomsInDateRange2(String xcatitem, String xstartdate, String xenddate) {
		if(StringUtils.isBlank(xcatitem) || StringUtils.isBlank(xstartdate) || StringUtils.isBlank(xenddate)) return Collections.emptyList();
		return opordMapper.allBookedHallsInDateRange2(xstartdate, xenddate, sessionManager.getBusinessId());
	}

}
