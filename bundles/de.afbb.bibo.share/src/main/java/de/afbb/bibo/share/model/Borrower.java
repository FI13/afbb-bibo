package de.afbb.bibo.share.model;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import de.afbb.bibo.share.beans.AbstractPropertyChangeSupport;

public class Borrower extends AbstractPropertyChangeSupport implements IEditorInput {

	private Integer id;
	private String surname = "";
	private String firstName = "";
	private String fiClass;

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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public String getFiClass() {
		return fiClass;
	}

	public void setFiClass(final String fiClass) {
		this.fiClass = fiClass;
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
		return "Ausleiher erstellen: " + firstName + " " + surname;
	}

	@Override
	public String getName() {
		return firstName + " " + surname;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (fiClass == null ? 0 : fiClass.hashCode());
		result = prime * result + (firstName == null ? 0 : firstName.hashCode());
		result = prime * result + (id == null ? 0 : id.hashCode());
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
		if (fiClass == null) {
			if (other.fiClass != null) {
				return false;
			}
		} else if (!fiClass.equals(other.fiClass)) {
			return false;
		}
		if (firstName == null) {
			if (other.firstName != null) {
				return false;
			}
		} else if (!firstName.equals(other.firstName)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
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
}
