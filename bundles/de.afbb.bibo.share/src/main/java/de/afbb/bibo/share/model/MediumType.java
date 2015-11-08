package de.afbb.bibo.share.model;

import de.afbb.bibo.share.beans.AbstractPropertyChangeSupport;

/**
 * type of a medium
 */
public class MediumType extends AbstractPropertyChangeSupport implements Cloneable {

	public static final String FIELD_NAME = "name";//$NON-NLS-1$

	private Integer id;
	private String name;
	private String iconPath;

	/**
	 * Constructor. creates a dummy object with invalid id
	 */
	public MediumType() {
		this(-1);
	}

	/**
	 * Constructor for an instance that has only the id field filled.
	 *
	 * @param id
	 */
	public MediumType(final int id) {
		this(id, null, null);
	}

	public MediumType(final int id, final String name, final String iconPath) {
		super();
		this.id = id;
		this.name = name;
		this.iconPath = iconPath;
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
		changeSupport.firePropertyChange(FIELD_NAME, name, name = typname);
	}

	@Override
	public String toString() {
		return "MediumType{" + "id=" + id + ", name=" + name + ", iconPath=" + iconPath + '}';
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (final CloneNotSupportedException e) {
			// swallow exception and return null
			return null;
		}
	}
}
