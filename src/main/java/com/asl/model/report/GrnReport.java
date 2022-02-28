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

@Data
@XmlRootElement(name = "grnreport")
@XmlAccessorType(XmlAccessType.FIELD)
@EqualsAndHashCode(callSuper = true)
public class GrnReport extends AbstractReportModel{

	private static final long serialVersionUID = -8875986826852915473L;

	private String status;

	@XmlElementWrapper(name = "grnorders")
	@XmlElement(name = "grnorder")
	private List<GRNOrder> grnorders = new ArrayList<>();
}
