package com.asl.entity;



import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name="cacommitteemember")
@IdClass(LandCommitteeMembersPK.class)
@EqualsAndHashCode(of= {"zid", "xcommittee", "xrow"}, callSuper = false)
public class LandCommitteeMembers extends AbstractModel<String>{

	
	private static final long serialVersionUID = -154924227393284163L;
	
	
	@Id
	@Basic(optional=false)
	@Column(name="zid")
	private String zid;

	@Id
	@Basic(optional=false)
	@Column(name="xcommittee")
	private String xcommittee;
	
	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;

	
	@Column(name = "xperson")
	private String xperson;
	
	@Column(name="xmembertype")
	private String xmembertype;
	
	@Column(name="xcontribution")
	private String xcontribution;
	
	@Column(name="xnote")
	private String xnote;
	
	@Transient
	private boolean newData;

}
