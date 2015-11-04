package de.afbb.bibo.share.model;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import de.afbb.bibo.share.beans.AbstractPropertyChangeSupport;

public class Curator extends AbstractPropertyChangeSupport implements IEditorInput {

	public static final String FIELD_NAME = "name";//$NON-NLS-1$
	public static final String FIELD_PASSWORD = "password";//$NON-NLS-1$

	private Integer id;
	private String name;
	private String salt;
	private String passwordHash;
	private String password;

	public Curator(int id, String name, String salt, String passwordHash) {
		this.id = id;
		this.name = name;
		this.salt = salt;
		this.passwordHash = passwordHash;
	}

	public Curator() {
	}

	/**
	 * getter for password
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * setter for password
	 *
	 * @param password
	 *            the password to set
	 */
	public void setPassword(final String password) {
		changeSupport.firePropertyChange(FIELD_PASSWORD, this.password, this.password = password);
	}

	/**
	 * getter for id
	 *
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * setter for id
	 *
	 * @param id
	 *            the id to set
	 */
	public void setId(final Integer id) {
		this.id = id;
	}

	/**
	 * getter for name
	 *
	 * @return the name
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * setter for name
	 *
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name) {
		changeSupport.firePropertyChange(FIELD_NAME, this.name, this.name = name);
	}

	/**
	 * getter for salt
	 *
	 * @return the salt
	 */
	public String getSalt() {
		return salt;
	}

	/**
	 * setter for salt
	 *
	 * @param salt
	 *            the salt to set
	 */
	public void setSalt(final String salt) {
		this.salt = salt;
	}

	/**
	 * getter for passwordHash
	 *
	 * @return the passwordhash
	 */
	public String getPasswordHash() {
		return passwordHash;
	}

	/**
	 * setter for passwordHash
	 *
	 * @param hash
	 *            the passwordHash to set
	 */
	public void setPasswordHash(final String hash) {
		passwordHash = hash;
	}

	@Override
	public Object getAdapter(final Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return name;
	}

	@Override
	public String toString() {
		return "Curator{" + "id=" + id + ", name=" + name + ", salt=" + salt + ", passwordHash=" + passwordHash + '}';
	}
}
