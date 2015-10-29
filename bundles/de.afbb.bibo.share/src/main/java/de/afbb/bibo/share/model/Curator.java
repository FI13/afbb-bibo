package de.afbb.bibo.share.model;

public class Curator {

	private Integer id;
	private String name;
	private String salt;
	private String passwordHash;
	private String password;

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
		this.password = password;
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
		this.name = name;
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
}
