package de.afbb.bibo.share.model;

import de.afbb.bibo.share.beans.AbstractPropertyChangeSupport;

/**
 * model class for a person that can lend a {@link Copy}
 */
public class Borrower extends AbstractPropertyChangeSupport implements Cloneable {

	public static final String FIELD_SURNAME = "surname";//$NON-NLS-1$
	public static final String FIELD_FIRSTNAME = "forename";//$NON-NLS-1$
	public static final String FIELD_INFO = "info";//$NON-NLS-1$
	public static final String FIELD_EMAIL = "email";//$NON-NLS-1$
	public static final String FIELD_PHONENUMER = "phoneNumber";//$NON-NLS-1$
	private Integer id;
	protected String surname;
	protected String forename;
	private String info;
	private String email;
	private String phoneNumber;

	public Borrower(final int id) {
		this(id, "", "", "", "", "");
	}

	public Borrower(final int id, final String forename, final String surname, final String info, final String email,
			final String phoneNumber) {
		this.id = id;
		this.forename = forename;
		this.surname = surname;
		this.info = info;
		this.email = email;
		this.phoneNumber = phoneNumber;
	}

	public Borrower() {

	}

	public String getInfo() {
		return info;
	}

	public void setInfo(final String info) {
		changeSupport.firePropertyChange(FIELD_INFO, this.info, this.info = info);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		changeSupport.firePropertyChange(FIELD_EMAIL, this.email, this.email = email);
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(final String phoneNumber) {
		changeSupport.firePropertyChange(FIELD_PHONENUMER, this.phoneNumber, this.phoneNumber = phoneNumber);
	}

	public Integer getId() {
		return id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(final String surname) {
		changeSupport.firePropertyChange(FIELD_SURNAME, this.surname, this.surname = surname);
	}

	public String getForename() {
		return forename;
	}

	public void setForename(final String firstName) {
		changeSupport.firePropertyChange(FIELD_FIRSTNAME, this.forename, this.forename = firstName);
	}

	public String getName() {
		return forename + " " + surname;
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (email == null ? 0 : email.hashCode());
		result = prime * result + (forename == null ? 0 : forename.hashCode());
		result = prime * result + (info == null ? 0 : info.hashCode());
		result = prime * result + (phoneNumber == null ? 0 : phoneNumber.hashCode());
		result = prime * result + (surname == null ? 0 : surname.hashCode());
		return result;
	}

	/**
	 * empty strings should be considered equals to null for easier validation
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Borrower)) {
			return false;
		}
		final Borrower other = (Borrower) obj;
		if (email == null || "".equals(email)) {
			if (other.email != null && !"".equals(other.email)) {
				return false;
			}
		} else if (!email.equals(other.email)) {
			return false;
		}
		if (forename == null || "".equals(forename)) {
			if (other.forename != null && !"".equals(other.forename)) {
				return false;
			}
		} else if (!forename.equals(other.forename)) {
			return false;
		}
		if (info == null || "".equals(info)) {
			if (other.info != null && !"".equals(other.info)) {
				return false;
			}
		} else if (!info.equals(other.info)) {
			return false;
		}
		if (phoneNumber == null || "".equals(phoneNumber)) {
			if (other.phoneNumber != null && !"".equals(other.phoneNumber)) {
				return false;
			}
		} else if (!phoneNumber.equals(other.phoneNumber)) {
			return false;
		}
		if (surname == null || "".equals(surname)) {
			if (other.surname != null && !"".equals(other.surname)) {
				return false;
			}
		} else if (!surname.equals(other.surname)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Borrower{" + "id=" + id + ", forename=" + forename + ", surname=" + surname + ", info=" + info
				+ ", email=" + email + ", phoneNumber=" + phoneNumber + '}';
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
