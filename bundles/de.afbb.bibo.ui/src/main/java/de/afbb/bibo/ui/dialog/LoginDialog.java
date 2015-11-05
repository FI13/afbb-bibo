package de.afbb.bibo.ui.dialog;

import java.net.ConnectException;

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
import org.eclipse.ui.PlatformUI;

import de.afbb.bibo.crypto.CryptoUtil;
import de.afbb.bibo.databinding.BindingHelper;
import de.afbb.bibo.share.ServiceLocator;
import de.afbb.bibo.share.SessionHolder;
import de.afbb.bibo.share.model.Curator;
import de.afbb.bibo.ui.Messages;

/**
 * dialog that tries to log the user in.<br>
 * will set the returned session token into {@link SessionHolder} on success and will terminate the program if the user cancels the dialog
 * 
 * @author dbecker
 */
public class LoginDialog extends AbstractDialog {

	private Text txtPassword;
	private Text txtName;
	private final Curator curator = new Curator();
	private boolean loginSuccessful = false;

	public LoginDialog(final Shell parentShell) {
		super(parentShell);
	}

	@Override
	public boolean close() {
		return loginSuccessful ? super.close() : loginSuccessful;
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

		initBinding();

		return area;
	}

	@Override
	protected void buttonPressed(final int buttonId) {
		if (Dialog.OK == buttonId) {
			loginSuccessful = validateLogin();
			okPressed();
		} else {
			PlatformUI.getWorkbench().close();
		}
	}

	private boolean validateLogin() {
		try {
			final String salt = ServiceLocator.getInstance().getLoginService().requestSaltForUserName(curator.getName());
			final String hashPassword = CryptoUtil.hashPassword(curator.getPassword(), salt);
			boolean loggedIn = ServiceLocator.getInstance().getLoginService().loginWithHash(curator.getName(),
					hashPassword);
			if (!loggedIn) {
				setMessage("Name und Passwort stimmen nicht überein !", IMessageProvider.ERROR);
				return false;
			}
			setMessage("", IMessageProvider.NONE); //$NON-NLS-1$
			SessionHolder.getInstance().setCurator(curator);
			return true;
		} catch (final ConnectException e) {
			setMessage(Messages.MSG_CONNECTION_ERROR, IMessageProvider.WARNING);
		}
		return false;
	}

	@Override
	protected void initBinding() {
		BindingHelper.bindStringToTextField(txtName, curator, Curator.class, Curator.FIELD_NAME, bindingContext, true);
		BindingHelper.bindStringToTextField(txtPassword, curator, Curator.class, Curator.FIELD_PASSWORD, bindingContext, true);
	}

	@Override
	protected String getTitle() {
		return "Benutzer-Anmeldung";
	}

}
