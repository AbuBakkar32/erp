package com.asl.entity;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "zlogs")

public class Zlogs {

	@Id
	@Basic(optional = false)
	@Column(name = "zerrorid")
	private String zerrorid;
	
	@Column(name = "zemail")
	private String zemail;
	
	@Column(name = "zerrornum")
	private String zerrornum;
	
	@Column(name = "zmessage")
	private String zmessage;
	
	@Column(name = "zstatement")
	private String zstatement;
}
