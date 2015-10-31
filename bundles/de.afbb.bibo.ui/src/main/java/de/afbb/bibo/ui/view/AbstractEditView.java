package de.afbb.bibo.ui.view;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.EditorPart;

abstract class AbstractEditView extends EditorPart {

	@Override
	public void createPartControl(final Composite parent) {
		initUi(parent);
	}

	/**
	 * initializes the UX of the editor
	 */
	protected abstract void initUi(final Composite parent);

	/**
	 * initializes the databinding for the editor
	 */
	protected abstract void initBinding();

	@Override
	public void doSave(final IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
}
