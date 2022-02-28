package com.asl.model.report;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.asl.entity.PogrnHeader;

import lombok.Data;

@Data
@XmlRootElement(name = "grnl")
@XmlAccessorType(XmlAccessType.FIELD)
public class GRNLReport {
	

	private String businessName;
	private String businessAddress;
	private String reportName;
	private String fromDate;
	private String toDate;
	private String printDate;
	private String copyrightText;

	@XmlElementWrapper(name = "grns")
	@XmlElement(name = "grn")
	private List<PogrnHeader> grns = new ArrayList<>();

}
