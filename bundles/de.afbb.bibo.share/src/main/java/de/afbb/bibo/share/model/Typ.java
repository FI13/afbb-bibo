package de.afbb.bibo.share.model;

import de.afbb.bibo.share.beans.AbstractPropertyChangeSupport;

public class Typ extends AbstractPropertyChangeSupport {

	public static final String FIELD_NAME = "typname";//$NON-NLS-1$

	private Integer id;
	private String typname;
	private String iconPath;

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(final String iconPath) {
		this.iconPath = iconPath;
	}

	public Integer getId() {
		return id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public String getTypname() {
		return typname;
	}

	public void setTypname(final String typname) {
		changeSupport.firePropertyChange(FIELD_NAME, this.typname, this.typname = typname);
	}

}
