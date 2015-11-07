package de.afbb.bibo.share.model;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import de.afbb.bibo.share.beans.AbstractPropertyChangeSupport;

public class Borrower extends AbstractPropertyChangeSupport implements IEditorInput, Cloneable {

	private static final long serialVersionUID = -3149389839841886740L;
	public static final String FIELD_SURNAME = "surname";//$NON-NLS-1$
	public static final String FIELD_FIRSTNAME = "forename";//$NON-NLS-1$
	public static final String FIELD_INFO = "info";//$NON-NLS-1$
	public static final String FIELD_EMAIL = "email";//$NON-NLS-1$
	public static final String FIELD_PHONENUMER = "phoneNumber";//$NON-NLS-1$
	private Integer id;
	private String surname;
	private String forename;
	private String info;
	private String email;
	private String phoneNumber;

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
		this.info = info;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
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
		this.surname = surname;
	}

	public String getForename() {
		return forename;
	}

	public void setForename(final String firstName) {
		forename = firstName;
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
		return "Ausleiher erstellen: " + forename + " " + surname;
	}

	@Override
	public String getName() {
		return forename + " " + surname;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (email == null ? 0 : email.hashCode());
		result = prime * result + (forename == null ? 0 : forename.hashCode());
		result = prime * result + (id == null ? 0 : id.hashCode());
		result = prime * result + (info == null ? 0 : info.hashCode());
		result = prime * result + (phoneNumber == null ? 0 : phoneNumber.hashCode());
		result = prime * result + (surname == null ? 0 : surname.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Borrower other = (Borrower) obj;
		if (email == null) {
			if (other.email != null) {
				return false;
			}
		} else if (!email.equals(other.email)) {
			return false;
		}
		if (forename == null) {
			if (other.forename != null) {
				return false;
			}
		} else if (!forename.equals(other.forename)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (info == null) {
			if (other.info != null) {
				return false;
			}
		} else if (!info.equals(other.info)) {
			return false;
		}
		if (phoneNumber == null) {
			if (other.phoneNumber != null) {
				return false;
			}
		} else if (!phoneNumber.equals(other.phoneNumber)) {
			return false;
		}
		if (surname == null) {
			if (other.surname != null) {
				return false;
			}
		} else if (!surname.equals(other.surname)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Borrower{" + "id=" + id + ", forename=" + forename + ", surname=" + surname + ", info=" + info + ", email=" + email
				+ ", phoneNumber=" + phoneNumber + '}';
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
