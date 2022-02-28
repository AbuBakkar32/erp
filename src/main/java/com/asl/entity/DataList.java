package com.asl.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Dec 04, 2020
 */
@Data
@Entity
@Table(name = "DL")
@IdClass(DataListPK.class)
@EqualsAndHashCode(of = { "zid","listid","listcode" }, callSuper = false)
public class DataList extends AbstractModel<String> {

	private static final long serialVersionUID = 2402818361445233745L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "listid")
	private int listid;

	@Id
	@Basic(optional = false)
	@Column(name = "listcode", length = 100)
	private String listcode;

	@Column(name = "description", length = 100)
	private String description;

	@Column(name = "listvalue1")
	private String listvalue1;

	@Column(name = "listvalue2")
	private String listvalue2;

	@Column(name = "listvalue3")
	private String listvalue3;

	@Column(name = "listvalue4")
	private String listvalue4;

	@Column(name = "listvalue5")
	private String listvalue5;

	@Column(name = "listvalue6")
	private String listvalue6;

	@Column(name = "listvalue7")
	private String listvalue7;

	@Column(name = "listvalue8")
	private String listvalue8;

	@Column(name = "listvalue9")
	private String listvalue9;

	@Column(name = "listvalue10")
	private String listvalue10;

	@Column(name = "listvalue11")
	private String listvalue11;

	@Column(name = "listvalue12")
	private String listvalue12;

	@Column(name = "listvalue13")
	private String listvalue13;

	@Column(name = "listvalue14")
	private String listvalue14;

	@Column(name = "listvalue15")
	private String listvalue15;

	@Column(name = "listvalue16")
	private String listvalue16;

	@Column(name = "extravalue1")
	private String extravalue1;

	@Column(name = "extravalue2")
	private String extravalue2;

	@Column(name = "extravalue3")
	private String extravalue3;

	@Column(name = "extravalue4")
	private String extravalue4;

	@Column(name = "extravalue5")
	private String extravalue5;
}
