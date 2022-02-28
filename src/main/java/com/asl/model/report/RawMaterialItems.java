package com.asl.model.report;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Apr 22, 2021
 */
@Data
@XmlRootElement(name = "rawitem")
@XmlAccessorType(XmlAccessType.FIELD)
public class RawMaterialItems implements Serializable {

	private static final long serialVersionUID = -3940833830062258233L;

	private String xitem;
	private BigDecimal xqtyl;
}
