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

/**
 * @author Zubayer Ahamed
 * @since Feb 24, 2021
 */
@Data
@Entity
@Table(name = "xcodes")
@IdClass(XcodesPK.class)
@EqualsAndHashCode(of = { "zid","xtype","xcode" }, callSuper = false)
public class Xcodes extends AbstractModel<String> {

	private static final long serialVersionUID = 304110246928300496L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xcode")
	private String xcode;

	@Id
	@Basic(optional = false)
	@Column(name = "xtype")
	private String xtype;

	
	//@Column(name = "xdescdet") //have to remove private String xdescdet;
	//public String xdescdet;
	 

	@Column(name = "xprops")
	private String xprops;

	@Column(name = "xacc")
	private String xacc;

	@Column(name = "xAccDisc")
	private String xAccDisc;
	
	@Column(name = "xAccVat")
	private String xAccVat;

	@Column(name = "xAccAit")
	private String xAccAit;

	@Column(name = "xAccPur")
	private String xAccPur;

	@Column(name = "xAccAdj")
	private String xAccAdj;

	@Column(name = "xAccCash")
	private String xAccCash;

	@Column(name = "xAccBank")
	private String xAccBank;

	@Column(name = "xAccArdBank")
	private String xAccArdBank; 

	@Column(name = "seqn")
	private Long seqn;

	@Column(name = "xlong")
	private String xlong;
	
	@Transient
	private String xdesc;
	
	@Transient
	private String xdesc1;

	@Transient
	private String xdesc2;
	
	@Transient
	private String xdesc3;
	
	@Transient
	private String xdesc4;
	
	@Transient
	private String xdesc5;
	
	@Transient
	private String xdesc6;
	
	@Transient
	private String xdesc7;
	
	@Transient
	private String xdesc8;
	
	@Transient
	private String xdesc9;

	@Column(name = "xparentcode")
	private String xparentcode;

	// Cost Center
	// Department Name
	// Store/Warehouse
}
