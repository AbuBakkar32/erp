package com.asl.model.report;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Apr 15, 2021
 */
@Data
@XmlRootElement(name = "column")
@XmlAccessorType(XmlAccessType.FIELD)
public class TableColumn{
	private String xitem;
	private String xdesc;
	private BigDecimal totalQty;
	private String xunitpur;
	private String xcatitem;
	private Long seqn;
}
