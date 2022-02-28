package com.asl.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "cafile")
@IdClass(cafilePK.class)
@EqualsAndHashCode(of = { "zid", "xfile","xfono" }, callSuper = false)
public class cafile extends AbstractModel<String>{

	
	private static final long serialVersionUID = 4975493647367797826L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xfile")
	private String xfile;
	
	@Id
	@Basic(optional = false)
	@Column(name = "xfono")
	private String xfono;
	
	@Column(name = "foemail")
	private String foemail;
	
	@Column(name = "xemail")
	private String xemail;
	
	@Column(name = "zemail")
	private String zemail;

}
