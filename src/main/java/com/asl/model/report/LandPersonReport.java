package com.asl.model.report;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.asl.entity.LandDocument;
import com.asl.entity.LandEducation;
import com.asl.entity.LandExperience;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@XmlRootElement(name = "landpersonreport")
@XmlAccessorType(XmlAccessType.FIELD)
@EqualsAndHashCode(callSuper = true)
public class LandPersonReport extends AbstractReportModel {

	
	private static final long serialVersionUID = -3790352281292466368L;
	
	private String xperson;
	private String xname;
	private String xfname;
	private String xmname;
	private String xdob;
	private String xoccupation;
	private String xcontact;
	private String xemail;
	private String xphone;
	private String xmadd;
	private String xpadd;
	
	@XmlElementWrapper(name = "educations")
	@XmlElement(name = "education")
	private List<LandEducation> educations = new ArrayList<>();

	@XmlElementWrapper(name = "experiences")
	@XmlElement(name = "experience")
	private List<LandExperience> experiences = new ArrayList<>();

	@XmlElementWrapper(name = "documents")
	@XmlElement(name = "document") 
	private List<LandDocument> documents = new ArrayList<>();
	
}
