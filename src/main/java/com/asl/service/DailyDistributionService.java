package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.DailyDistribution;

/**
 * @author Zubayer Ahamed
 * @since May 9, 2021
 */
@Component
public interface DailyDistributionService {

	public List<DailyDistribution> getDailyDistribution();
}
