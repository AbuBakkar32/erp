package com.asl.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class LandSurveyPK implements Serializable{
	
	private static final long serialVersionUID = -5805988816178805979L;
	
	private String zid;
	private String xland;
	private int xrow;
}
