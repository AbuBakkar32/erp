package com.asl.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.DailyDistribution;
import com.asl.mapper.DailyDistributionMapper;
import com.asl.service.DailyDistributionService;

/**
 * @author Zubayer Ahamed
 * @since May 9, 2021
 */
@Service
public class DailyDistributionServiceImpl extends AbstractGenericService implements DailyDistributionService {

	@Autowired private DailyDistributionMapper mapper;

	@Override
	public List<DailyDistribution> getDailyDistribution() {
		Calendar currentDate = Calendar.getInstance();
		currentDate.set(Calendar.HOUR_OF_DAY, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);

		Calendar nextDate = Calendar.getInstance();
		nextDate.add(Calendar.DAY_OF_MONTH, 1);
		nextDate.set(Calendar.HOUR_OF_DAY, 0);
		nextDate.set(Calendar.MINUTE, 0);
		nextDate.set(Calendar.SECOND, 0);

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
		return mapper.getDailyDistribution(sessionManager.getBusinessId(), sdf.format(currentDate.getTime()).toUpperCase(), sdf.format(nextDate.getTime()).toUpperCase());
	}

	
}
