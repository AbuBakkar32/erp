package com.asl.model.report;

import java.io.Serializable;
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
 * @since May 8, 2021
 */
@Data
@XmlRootElement(name = "rawMaterial")
@XmlAccessorType(XmlAccessType.FIELD)
public class RawMaterial implements Serializable {

	private static final long serialVersionUID = -4060411750446565043L;

	private String code;
	private String name;
	private BigDecimal qty;
	private BigDecimal wastage;

	@XmlElementWrapper(name = "finishedGoods")
	@XmlElement(name = "finishedGood")
	private List<FinishedGood> finishedGoods = new ArrayList<>();

}
