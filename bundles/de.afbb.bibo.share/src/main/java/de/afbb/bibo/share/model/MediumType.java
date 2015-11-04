package de.afbb.bibo.share.model;

import de.afbb.bibo.share.beans.AbstractPropertyChangeSupport;

public class MediumType extends AbstractPropertyChangeSupport {

	public static final String FIELD_NAME = "typname";//$NON-NLS-1$

	private Integer id;
	private String name;
	private String iconPath;
	
	public MediumType(int id, String name, String iconPath) {
        this.id = id;
        this.name = name;
        this.iconPath = iconPath;
    }
	
	public MediumType() {
		
	}

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

	public String getName() {
		return name;
	}

	public void setName(final String typname) {
		changeSupport.firePropertyChange(FIELD_NAME, this.name, this.name = typname);
	}

    @Override
    public String toString() {
        return "MediumType{" + "id=" + id + ", name=" + name + ", iconPath=" + iconPath + '}';
    }

}
