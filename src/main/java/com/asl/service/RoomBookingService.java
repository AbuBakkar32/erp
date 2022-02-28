package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.ConventionBookedDetails;

/**
 * @author Zubayer Ahamed
 * @since May 30, 2021
 */
@Component
public interface RoomBookingService {

	public List<String> allBookedRoomsInDateRange(String xcatitem, String xstartdate, String xenddate, String xordernum);

	public List<ConventionBookedDetails> allBookedRoomsInDateRange2(String xcatitem, String xstartdate, String xenddate);
}
