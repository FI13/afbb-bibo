package de.afbb.bibo.ui.dialog;

import java.net.ConnectException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.widgets.Shell;

import de.afbb.bibo.share.ServiceLocator;
import de.afbb.bibo.share.model.Borrower;
import de.afbb.bibo.ui.Messages;
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

	@Override
	protected void buttonPressed(final int buttonId) {
		if (buttonId == Dialog.OK) {
			try {
				if (ServiceLocator.getInstance().getBorrowerService().exists(input.getForename(), input.getSurname())) {
					setMessage("Es gibt schon einen Ausleiher mit dem gewählten Namen", IMessageProvider.INFORMATION);
				} else {
					final Job job = new Job(getTitle()) {

						@Override
						protected IStatus run(IProgressMonitor monitor) {
							try {
								ServiceLocator.getInstance().getBorrowerService().create(input);
								return Status.OK_STATUS;
							} catch (final ConnectException e) {
								return Status.CANCEL_STATUS;
							}
						}
					};
					job.schedule();
					okPressed();
				}
			} catch (ConnectException e) {
				setMessage(Messages.MESSAGE_ERROR_CONNECTION, IMessageProvider.WARNING);
			}
		} else {
			cancelPressed();
		}
	}

}
