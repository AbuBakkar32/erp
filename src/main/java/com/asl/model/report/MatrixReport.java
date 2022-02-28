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
 * @since Apr 15, 2021
 */
@Data
@XmlRootElement(name = "matrixreport")
@XmlAccessorType(XmlAccessType.FIELD)
public class MatrixReport extends AbstractReportModel {

	@XmlElementWrapper(name = "datas")
	@XmlElement(name = "data")
	List<MatrixReportData> datas = new ArrayList<>();
}
