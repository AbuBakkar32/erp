package com.asl.entity;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "poterms")
@IdClass(PotermsPK.class)
@EqualsAndHashCode(of = { "zid", "xpornum", "xrow" }, callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Poterms extends AbstractModel<String>{

	
	private static final long serialVersionUID = -6380924403965688180L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xpornum")
	private String xpornum;

	@Id
	@Column(name = "xrow")
	private int xrow;

	@Column(name = "xcode")
	private String xcode;

	@Column(name = "xnote")
	private String xnote;

}
