package com.asl.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Dec 04, 2020
 */
@Data
@Entity
@Table(name = "LH")
@IdClass(ListHeadPK.class)
@EqualsAndHashCode(of = { "zid","listcode" }, callSuper = false)
public class ListHead extends AbstractModel<String> {

	private static final long serialVersionUID = 3380300077484005051L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "listcode", length = 100)
	private String listcode;

	@Column(name = "description", length = 100)
	@Size(max = 100, message = "Description maximum 100 character allowed")
	private String description;

	@Column(name = "prompt1", length = 100)
	@Size(max = 100, message = "Prompt 1 maximum 100 character allowed")
	private String prompt1;

	@Column(name = "prompt2", length = 100)
	@Size(max = 100, message = "Prompt 2 maximum 100 character allowed")
	private String prompt2;

	@Column(name = "prompt3", length = 100)
	@Size(max = 100, message = "Prompt 3 maximum 100 character allowed")
	private String prompt3;

	@Column(name = "prompt4", length = 100)
	@Size(max = 100, message = "Prompt 4 maximum 100 character allowed")
	private String prompt4;

	@Column(name = "prompt5", length = 100)
	@Size(max = 100, message = "Prompt 5 maximum 100 character allowed")
	private String prompt5;

	@Column(name = "prompt6", length = 100)
	@Size(max = 100, message = "Prompt 6 maximum 100 character allowed")
	private String prompt6;

	@Column(name = "prompt7", length = 100)
	@Size(max = 100, message = "Prompt 7 maximum 100 character allowed")
	private String prompt7;

	@Column(name = "prompt8", length = 100)
	@Size(max = 100, message = "Prompt 8 maximum 100 character allowed")
	private String prompt8;

	@Column(name = "prompt9", length = 100)
	@Size(max = 100, message = "Prompt 9 maximum 100 character allowed")
	private String prompt9;

	@Column(name = "prompt10", length = 100)
	@Size(max = 100, message = "Prompt 10 maximum 100 character allowed")
	private String prompt10;

	@Column(name = "prompt11", length = 100)
	@Size(max = 100, message = "Prompt 11 maximum 100 character allowed")
	private String prompt11;

	@Column(name = "prompt12", length = 100)
	@Size(max = 100, message = "Prompt 12 maximum 100 character allowed")
	private String prompt12;

	@Column(name = "prompt13", length = 100)
	@Size(max = 100, message = "Prompt 13 maximum 100 character allowed")
	private String prompt13;

	@Column(name = "prompt14", length = 100)
	@Size(max = 100, message = "Prompt 14 maximum 100 character allowed")
	private String prompt14;

	@Column(name = "prompt15", length = 100)
	@Size(max = 100, message = "Prompt 15 maximum 100 character allowed")
	private String prompt15;

	@Column(name = "prompt16", length = 100)
	@Size(max = 100, message = "Prompt 16 maximum 100 character allowed")
	private String prompt16;

	@Column(name = "notes", length = 500)
	@Size(max = 500, message = "Notes maximum 500 character allowed")
	private String notes;

	@Column(name = "xtypetrn", length = 50)
	private String xtypetrn;

	@Column(name = "xtrn", length = 10)
	private String xtrn;

	@Transient
	private boolean newData;
}
