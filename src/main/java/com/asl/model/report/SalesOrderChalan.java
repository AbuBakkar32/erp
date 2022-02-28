package com.asl.model.report;

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
 * @since Mar 24, 2021
 */
@Data
@XmlRootElement(name = "chalan")
@XmlAccessorType(XmlAccessType.FIELD)
public class SalesOrderChalan {

	private String chalanName;
	private String chalanDate;
	private String status;

	@XmlElementWrapper(name = "items")
	@XmlElement(name = "item")
	private List<ItemDetails> items = new ArrayList<>();

	@XmlElementWrapper(name = "suggestions")
	@XmlElement(name = "suggestion")
	private List<Suggestion> suggestions = new ArrayList<>();

	@XmlElementWrapper(name = "rawitems")
	@XmlElement(name = "rawitem")
	private List<RawMaterialItems> rawItems = new ArrayList<>();
}
