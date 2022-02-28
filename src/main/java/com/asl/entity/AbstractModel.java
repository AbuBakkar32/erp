package com.asl.entity;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Nov 28, 2020
 */
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractModel<U> implements Serializable {

	private static final long serialVersionUID = -3736149934368733226L;

	@CreatedBy
	@Column(name = "zauserid")
	private U zauserid;

	@LastModifiedBy
	@Column(name = "zuuserid")
	private U zuuserid;

	@Column(name = "zaip")
	private String zaip;

	@Column(name = "zuip")
	private String zuip;

	@Column(name = "zactive", length = 1)
	private Boolean zactive = Boolean.TRUE;

	public Boolean getZactive() {
		if(this.zactive == null) return Boolean.FALSE;
		return zactive;
	}

	@CreationTimestamp
	@Column(name = "ztime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date ztime = new Date();

	@UpdateTimestamp
	@Column(name = "zutime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date zutime = new Date();

	@Transient
	private String copyid;

	@Transient
	private String centralzid;

	@PreUpdate
	public void onUpdate() {
		try {
			setZuip(InetAddress.getLocalHost().toString());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	@PrePersist
	public void onPersist() {
		try {
			setZaip(InetAddress.getLocalHost().toString());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

}
