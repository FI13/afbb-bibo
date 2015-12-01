package de.afbb.bibo.share.internal.model;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import de.afbb.bibo.share.model.Borrower;

public class BorrowerInput extends Borrower implements IEditorInput {

	/**
	 * casting {@link Borrower} to {@link BorrowerInput} will most likely fail.
	 * use this constructor instead
	 *
	 * @param input
	 */
	public BorrowerInput(final Borrower input) {
		super();
		if (input != null) {
			setEmail(input.getEmail());
			setForename(input.getForename());
			setId(input.getId());
			setInfo(input.getInfo());
			setPhoneNumber(input.getPhoneNumber());
			setSurname(input.getSurname());
		}
	}

	@SuppressWarnings("rawtypes")
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

}
