package com.asl.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Jan 12, 2021
 */
@Data
@Entity
@Table(name = "userAuditRecord")
public class UserAuditRecord implements Serializable {
	private static final long serialVersionUID = -7491245343434996061L;

	@Id
	@Column(name = "recordId", unique = true, nullable = false)
	private Long recordId;

	@Column(name = "userId")
	private String userId;

	@Column(name = "businessId")
	private String businessId;

	@Column(name = "unAuthorizedAccessMessage")
	private String unAuthorizedAccessMessage;

	@Column(name = "ipAddress")
	private String ipAddress;

	@Column(name = "loginDate")
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date loginDate;

	@Column(name = "logoutDate")
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date logoutDate;

	@Column(name = "recordDate")
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date recordDate;
}
