package de.afbb.bibo.ui.view;

import java.net.ConnectException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import de.afbb.bibo.share.model.Borrower;
import de.afbb.bibo.ui.form.BorrowerForm;

/**
 * this view adds or edits a person who can borrow books
 * 
 * @author philippwiddra
 * @author David Becker
 */
public class BorrowerView extends AbstractEditView<Borrower> {

	public static final String ID = "de.afbb.bibo.ui.borrower";//$NON-NLS-1$

	private BorrowerForm borrowerForm;

	@Override
	protected String computePartName(Borrower input) {
		return input != null ? input.getName() : null;
	}

	@Override
	protected Borrower cloneInput(Borrower input) {
		return (Borrower) input.clone();
	}

	@Override
	public void setFocus() {
		borrowerForm.setFocus();
	}

	@Override
	public void doSave(final IProgressMonitor monitor) {

	}

	@Override
	public boolean isDirty() {
		return !input.equals(inputCache);
	}

	@Override
	protected Composite initUi(final Composite parent) throws ConnectException {
		final Composite content = toolkit.createComposite(parent, SWT.NONE);
		content.setLayout(new GridLayout(1, false));

		Group borrowerGroup = toolkit.createGroup(content, "Allgemein");
		borrowerForm = new BorrowerForm(borrowerGroup, input, bindingContext, toolkit);

		GridDataFactory.fillDefaults().grab(true, true).applyTo(content);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(borrowerForm);
		GridDataFactory.fillDefaults().hint(200, SWT.DEFAULT).grab(true, false).applyTo(borrowerGroup);

		return content;
	}

	@Override
	protected void initBinding() {
	}
}
