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
 * @since Mar 30, 2021
 */
@Data
@XmlRootElement(name = "productionbathcreport")
@XmlAccessorType(XmlAccessType.FIELD)
@EqualsAndHashCode(callSuper = true)
public class DailyProductionBatchReport extends AbstractReportModel {

	private static final long serialVersionUID = 174857546195520525L;

	@XmlElementWrapper(name = "rawMaterials")
	@XmlElement(name = "rawMaterial")
	private List<RawMaterial> rawMaterials = new ArrayList<>();

}
