package de.afbb.bibo.share.internal.model;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import de.afbb.bibo.share.model.Copy;

public class CopyInput extends Copy implements IEditorInput {

	/**
	 * default constructor
	 */
	public CopyInput() {
		super();
	}

	/**
	 * casting {@link Copy} to {@link CopyInput} will most likely fail. use this
	 * constructor instead
	 *
	 * @param input
	 */
	public CopyInput(final Copy input) {
		super(input.getId(), input.getEdition(), input.getBarcode(), input.getInventoryDate(), input.getCondition(),
				input.getBorrowDate(), input.getLastBorrowDate(), input.getGroupId(), input.getBorrower(),
				input.getLastBorrower(), input.getCurator(), input.getLastCurator(), input.getMedium().getMediumId(),
				input.getMedium().getIsbn(), input.getMedium().getTitle(), input.getMedium().getAuthor(),
				input.getMedium().getLanguage(), input.getMedium().getType(), input.getMedium().getPublisher());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(final Class adapter) {
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
	public String getName() {
		return getBarcode();
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return getBarcode();
	}
}
