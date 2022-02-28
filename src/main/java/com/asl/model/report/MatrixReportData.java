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
 * @since Apr 17, 2021
 */
@Data
@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.FIELD)
public class MatrixReportData {

	@XmlElementWrapper(name = "columns")
	@XmlElement(name = "column")
	List<TableColumn> columns = new ArrayList<>();

	@XmlElementWrapper(name = "rows")
	@XmlElement(name = "row")
	List<BranchRow> rows = new ArrayList<>();

	@XmlElementWrapper(name = "totals")
	@XmlElement(name = "total")
	List<Total> totals = new ArrayList<>();
}
