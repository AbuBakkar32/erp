package com.asl.model.report;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Mar 24, 2021
 */
@Data
@XmlRootElement(name = "allsalesorderchalanreport")
@XmlAccessorType(XmlAccessType.FIELD)
public class AllSalesOrderChalanReport extends AbstractReportModel {

	private static final long serialVersionUID = -5645378863381389958L;

	@XmlElementWrapper(name = "chalans")
	@XmlElement(name = "chalan")
	private List<SalesOrderChalan> chalans = new ArrayList<>();
}
