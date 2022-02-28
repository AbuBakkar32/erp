package com.asl.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Mar 20, 2021
 */
@Data
@Entity
@IdClass(ProcErrorLogPK.class)
@Table(name = "ASL_ERROR_LOG_TABLE")
public class ProcErrorLog {

	@Id
	@Column(name = "zid")
	private String zid;

	@Id
	@Column(name = "osqlcode")
	private String osqlCode;

	@Id
	@Column(name = "action")
	private String action;

	@Id
	@Column(name = "v_source")
	private String source;


	@Column(name = "zauserid")
	private String zauserid;

	@Column(name = "zutime")
	private String zutime;

	@Column(name = "v_error_message")
	private String errorMessage;

}
