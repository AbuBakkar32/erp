package com.asl.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "termstrn")
@IdClass(TermstrnPK.class)
@EqualsAndHashCode(of = { "zid", "xdocnum","xterm"}, callSuper = false)
@XmlRootElement(name = "dfterm")
@XmlAccessorType(XmlAccessType.FIELD)
public class Termstrn extends AbstractModel<String>{
	 
	private static final long serialVersionUID = 7354899071870813019L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xdocnum")
	private String xdocnum;

	@Id
	@Basic(optional = false)
	@Column(name = "xterm")
	private String xterm;

	@Column(name = "xtype")
	private String xtype;

	@Column(name = "xdesc")
	private String xdesc;
	
	@Column(name = "xnote")
	private String xnote;

	@Column(name = "xserial")
	private int xserial;


}
