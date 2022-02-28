package com.asl.model.report;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.asl.entity.Imstock;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Mar 24, 2021
 */
@Data
@XmlRootElement(name = "stockl")
@XmlAccessorType(XmlAccessType.FIELD)
public class STOCKLReport extends AbstractReportModel {

	private static final long serialVersionUID = -6294587903964088393L;

	@XmlElementWrapper(name = "stocks")
	@XmlElement(name = "stock")
	private List<Imstock> stocks = new ArrayList<>();
}
