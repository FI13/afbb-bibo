package de.afbb.bibo.ui.dialog;

import org.eclipse.swt.widgets.Shell;

import de.afbb.bibo.share.model.Borrower;
import de.afbb.bibo.ui.form.BorrowerForm;

/**
 * dialog that creates a new {@link Borrower}
 * 
 * @author David Becker
 *
 */
public class CreateBorrowerDialog extends AbstractFormDialog<Borrower, BorrowerForm> {

	public CreateBorrowerDialog(Shell parentShell) {
		super(parentShell, Borrower.class, BorrowerForm.class);
	}

	@Override
	protected String getTitle() {
		return "Neuanlage Ausleiher";
	}

}
