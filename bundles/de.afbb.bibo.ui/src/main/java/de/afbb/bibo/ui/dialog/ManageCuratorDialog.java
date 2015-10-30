package de.afbb.bibo.ui.dialog;

import java.net.ConnectException;

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.afbb.bibo.databinding.BindingHelper;
import de.afbb.bibo.share.ServiceLocator;
import de.afbb.bibo.share.SessionHolder;
import de.afbb.bibo.share.model.Curator;

/**
 * dialog that creates a new instance of type {@link Curator}
 * 
 * @author dbecker
 */
public class ManageCuratorDialog extends AbstractDialog {

	private static final String FIELD_PASSWORD2 = "password2";//$NON-NLS-1$

	private Text txtName;
	private Text txtPassword;
	private Text txtPassword2;
	private final Curator curator;

	private String password2 = "";//$NON-NLS-1$

	private final boolean createNew;

	public ManageCuratorDialog(final Shell parentShell, final boolean createNew) {
		super(parentShell);
		this.createNew = createNew;
		curator = createNew ? new Curator() : SessionHolder.getInstance().getCurator();
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite area = (Composite) super.createDialogArea(parent);
		final Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		final GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 30;
		layout.horizontalSpacing = 10;
		container.setLayout(layout);

		final Label lblName = new Label(container, SWT.NONE);
		lblName.setText("Name");
		txtName = new Text(container, SWT.BORDER);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtName);
		// disable name field on password change
		lblName.setEnabled(createNew);
		txtName.setEnabled(createNew);

		final Label lblPassword = new Label(container, SWT.NONE);
		lblPassword.setText("Passwort");
		txtPassword = new Text(container, SWT.BORDER | SWT.PASSWORD);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtPassword);

		final Label lblPassword2 = new Label(container, SWT.NONE);
		lblPassword2.setText("Passwort (Wiederholung)");
		txtPassword2 = new Text(container, SWT.BORDER | SWT.PASSWORD);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtPassword2);

		return area;
	}

	@Override
	protected void buttonPressed(final int buttonId) {
		if (buttonId == Dialog.OK) {
			try {
				// check that there is no curator with chosen name
				if (createNew && ServiceLocator.getInstance().getCuratorService().exists(curator.getName())) {
					setMessage("Es gibt schon einen Verwalter mit dem gewählten Namen", IMessageProvider.INFORMATION);
				} else {
					// check that passwords are the same
					if (!password2.equals(curator.getPassword())) {
						setMessage("Passwort & Passwort-Wiederholung stimmen nicht überein!", IMessageProvider.ERROR);
					} else {
						// TODO implement password strength check?
						// persist in background job
						final Job job = new Job(getTitle()) {

							@Override
							protected IStatus run(final IProgressMonitor monitor) {
								try {
									// TODO compute hash
									if (createNew) {
										ServiceLocator.getInstance().getCuratorService().create(curator);
									} else {
										ServiceLocator.getInstance().getCuratorService().update(curator);
									}
									return Status.OK_STATUS;
								} catch (final ConnectException e) {
									return Status.CANCEL_STATUS;
								}
							}
						};
						job.schedule();
						okPressed();
					}
				}
			} catch (final ConnectException e) {
				setMessage("Es besteht ein Verbindungs-Problem mit dem Server", IMessageProvider.WARNING);
			}
		} else {
			cancelPressed();
		}
	}

	@Override
	protected void createBinding() {
		BindingHelper.bindStringToTextField(txtName, curator, Curator.class, Curator.FIELD_NAME, bindingContext, true);
		BindingHelper.bindStringToTextField(txtPassword, curator, Curator.class, Curator.FIELD_PASSWORD, bindingContext, true);
		BindingHelper.bindStringToTextField(txtPassword2, BeansObservables.observeValue(this, FIELD_PASSWORD2), bindingContext, true);

	}

	@Override
	protected String getTitle() {
		return createNew ? "Neuanlage Verwalter" : "Passwort ändern";
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(final String password2) {
		changeSupport.firePropertyChange(FIELD_PASSWORD2, this.password2, this.password2 = password2);
	}

}
