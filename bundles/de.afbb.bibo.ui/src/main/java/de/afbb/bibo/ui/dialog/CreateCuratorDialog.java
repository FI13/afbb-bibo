package de.afbb.bibo.ui.dialog;

import java.net.ConnectException;

import org.eclipse.core.databinding.beans.BeansObservables;
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
import de.afbb.bibo.share.model.Curator;

/**
 * dialog that creates a new instance of type {@link Curator}
 * 
 * @author dbecker
 */
public class CreateCuratorDialog extends AbstractDialog {

	private static final String FIELD_PASSWORD2 = "password2";//$NON-NLS-1$

	private Text txtName;
	private Text txtPassword;
	private Text txtPassword2;
	private final Curator curator = new Curator();

	private String password2 = "";//$NON-NLS-1$

	public CreateCuratorDialog(final Shell parentShell) {
		super(parentShell);
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
				if (ServiceLocator.getInstance().getCuratorService().exists(curator.getName())) {
					setMessage("Es gibt schon einen Verwalter mit dem gewählten Namen", IMessageProvider.INFORMATION);
				} else {
					// check that passwords are the same
					if (!password2.equals(curator.getPassword())) {
						setMessage("Passwort & Passwort-Wiederholung stimmen nicht überein!", IMessageProvider.ERROR);
					} else {
						// persist
						ServiceLocator.getInstance().getCuratorService().create(curator);
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
		return "Neuanlage Verwalter";
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(final String password2) {
		changeSupport.firePropertyChange(FIELD_PASSWORD2, this.password2, this.password2 = password2);
	}

}
