package de.afbb.bibo.share.model;

public class Typ {

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
		this.typname = typname;
	}

}
