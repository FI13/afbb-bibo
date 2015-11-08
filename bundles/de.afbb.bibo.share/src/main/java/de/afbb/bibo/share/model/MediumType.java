package de.afbb.bibo.share.model;

import de.afbb.bibo.share.beans.AbstractPropertyChangeSupport;

/**
 * type of a medium
 */
public class MediumType extends AbstractPropertyChangeSupport implements Cloneable {

	public static final String FIELD_NAME = "name";//$NON-NLS-1$

	private Integer id;
	private String name;
	private IconType icon;

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

	public MediumType(final int id, final String name, final IconType icon) {
		super();
		this.id = id;
		this.name = name;
		this.icon = icon;
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

	public IconType getIcon() {
		return icon;
	}

	public void setIcon(final IconType icon) {
		this.icon = icon;
	}

	@Override
	public String toString() {
		return "MediumType{" + "id=" + id + ", name=" + name + ", icon=" + icon + '}';
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
