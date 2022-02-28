package com.asl.model.report;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zubayer Ahamed
 * @since Apr 15, 2021
 */
@Data
@XmlRootElement(name = "item")
@AllArgsConstructor
@NoArgsConstructor
public class BranchItem{
	private String zorg;
	private String xitem;
	private BigDecimal xqtyord;
	private Long seqn;

	public BranchItem(String zorg, String xitem, BigDecimal xqtyord) {
		this.zorg = zorg;
		this.xitem = xitem;
		this.xqtyord = xqtyord;
	}
}
