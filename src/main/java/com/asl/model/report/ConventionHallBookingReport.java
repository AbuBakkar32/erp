package com.asl.model.report;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Jun 15, 2021
 */
@Data
@XmlRootElement(name = "hallbookingreport")
@XmlAccessorType(XmlAccessType.FIELD)
@EqualsAndHashCode(callSuper = true)
public class ConventionHallBookingReport extends AbstractReportModel {

	private static final long serialVersionUID = -8670924954749074288L;

	private HallBookingHeader header;

	@XmlElementWrapper(name = "fooddetails")
	@XmlElement(name = "fooddetail")
	private List<HallBookingFoodDetail> fooddetails = new ArrayList<>();

	@XmlElementWrapper(name = "facilitiesdetails")
	@XmlElement(name = "facilitiesdetail")
	private List<HallBookingFacilitiesDetail> facilitiesdetails = new ArrayList<>();
}
