package de.afbb.bibo.share.model;

import java.sql.Blob;

public class Typ {

	private Integer id;
	private String typname;
	private Blob icon;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTypname() {
		return typname;
	}
	public void setTypname(String typname) {
		this.typname = typname;
	}
	public Blob getIcon() {
		return icon;
	}
	public void setIcon(Blob icon) {
		this.icon = icon;
	}

	
}
