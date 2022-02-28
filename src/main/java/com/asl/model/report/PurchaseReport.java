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
@XmlRootElement(name = "purchasereport")
@XmlAccessorType(XmlAccessType.FIELD)
@EqualsAndHashCode(callSuper = true)
public class PurchaseReport extends AbstractReportModel {

	private static final long serialVersionUID = 329415349462370955L;

	private String status;

	@XmlElementWrapper(name = "purchaseorders")
	@XmlElement(name = "purchaseorder")
	private List<PurchaseOrder> purchaseorders = new ArrayList<>();
}
