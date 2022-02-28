package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.DailyDistribution;

/**
 * @author Zubayer Ahamed
 * @since May 9, 2021
 */
@Mapper
public interface DailyDistributionMapper {

	public List<DailyDistribution> getDailyDistribution(String zid, String currentDate, String nextDate);
}
