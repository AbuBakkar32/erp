package com.asl.model.report;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.asl.entity.DailyDistribution;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since May 9, 2021
 */

@Data
@XmlRootElement(name = "dailydistributionreport")
@XmlAccessorType(XmlAccessType.FIELD)
@EqualsAndHashCode(callSuper = true)
public final class DailyDistributionReport extends AbstractReportModel {

	private static final long serialVersionUID = -4161065873513986535L;

	@XmlElementWrapper(name = "dailydistributions")
	@XmlElement(name = "dailydistribution")
	private List<DailyDistribution> distributions = new ArrayList<>();
}
