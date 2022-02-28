package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.ConventionBookedDetails;

/**
 * @author Zubayer Ahamed
 * @since Mar 8, 2021
 */
@Component
public interface HallBookingService {

	public List<String> allBookedHallsInDateRange(String xstartdate, String xenddate, String xordernum);

	public List<ConventionBookedDetails> allBookedHallsInDateRange2(String xstartdate, String xenddate);
}
