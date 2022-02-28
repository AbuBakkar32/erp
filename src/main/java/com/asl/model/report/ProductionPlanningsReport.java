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
 * @since Mar 24, 2021
 */
@Data
@XmlRootElement(name = "productionplanningsreport")
@XmlAccessorType(XmlAccessType.FIELD)
@EqualsAndHashCode(callSuper = true)
public class ProductionPlanningsReport extends AbstractReportModel {

	private static final long serialVersionUID = -2577217110271006869L;

	@XmlElementWrapper(name = "chalans")
	@XmlElement(name = "chalan")
	private List<SalesOrderChalan> chalans = new ArrayList<>();

	@XmlElementWrapper(name = "items")
	@XmlElement(name = "item")
	private List<ItemDetails> itemDetails = new ArrayList<>();
}
