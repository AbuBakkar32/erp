package com.asl.model.report;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Mar 24, 2021
 */
@Data
@XmlRootElement(name = "suggestion")
@XmlAccessorType(XmlAccessType.FIELD)
public class Suggestion {

	private String xitem;
	private String productionItem;
	private String productionItemQty;
	private String productionItemUnit;
	
	private String xrawmaterial;
	private String rawMaterial;
	private String rawMaterialQty;
	private String rawMaterialUnit;
}
