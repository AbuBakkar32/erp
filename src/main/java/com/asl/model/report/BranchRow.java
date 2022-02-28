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
 * @since Apr 15, 2021
 */
@Data
@XmlRootElement(name = "row")
@XmlAccessorType(XmlAccessType.FIELD)
public class BranchRow{
	private String zorg;
	@XmlElementWrapper(name = "items")
	@XmlElement(name = "item")
	private List<BranchItem> items = new ArrayList<>();
	private BigDecimal totalItemOrdered;
}
