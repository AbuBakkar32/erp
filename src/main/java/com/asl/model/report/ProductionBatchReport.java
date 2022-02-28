package com.asl.model.report;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.asl.entity.Moheader;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Mar 30, 2021
 */
@Data
@XmlRootElement(name = "productionbathcreport")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductionBatchReport extends AbstractReportModel {

	private static final long serialVersionUID = 37469231367485388L;

	@XmlElementWrapper(name = "batches")
	@XmlElement(name = "batch")
	private List<Moheader> batches = new ArrayList<>();

}
