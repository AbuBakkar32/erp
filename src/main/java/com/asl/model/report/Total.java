package com.asl.model.report;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Apr 17, 2021
 */
@Data
@XmlRootElement(name = "total")
@XmlAccessorType(XmlAccessType.FIELD)
public class Total {
	private String xitem;
	private BigDecimal xqtyord;
}
