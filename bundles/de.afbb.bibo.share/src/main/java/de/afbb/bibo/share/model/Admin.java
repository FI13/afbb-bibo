package de.afbb.bibo.share.model;

public class Admin {

	private Integer id;
	private String name;
	private String salt;
	private String hash;

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
	 * getter for hash
	 * 
	 * @return the hash
	 */
	public String getHash() {
		return hash;
	}

	/**
	 * setter for hash
	 * 
	 * @param hash
	 *            the hash to set
	 */
	public void setHash(final String hash) {
		this.hash = hash;
	}
}
