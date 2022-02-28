package com.asl.model.report;

import java.math.BigDecimal;
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
@XmlRootElement(name = "chalanreport")
@XmlAccessorType(XmlAccessType.FIELD)
public class SalesOrderChalanReport extends AbstractReportModel {

	private static final long serialVersionUID = -6282166705159721870L;

	private String chalanNumber;
	private String chalanDate;
	private String chalanStatus;
	private String spellword2;
	private String xprimeword2;

	@XmlElementWrapper(name = "salesorders")
	@XmlElement(name = "salesorder")
	private List<SalesOrder> salesorders = new ArrayList<>();

	
}
