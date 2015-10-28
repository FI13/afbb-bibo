package de.afbb.bibo.ui.dialog;

import java.net.ConnectException;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.afbb.bibo.crypto.CryptoUtil;
import de.afbb.bibo.databinding.BindingHelper;
import de.afbb.bibo.share.ServiceLocator;
import de.afbb.bibo.share.SessionHolder;
import de.afbb.bibo.share.model.Curator;

public class LoginDialog extends TitleAreaDialog {

	private Text txtPassword;
	private Text txtName;
	private final Curator curator = new Curator();
	private final DataBindingContext bindingContext = new DataBindingContext();

	public LoginDialog(final Shell parentShell) {
		super(parentShell);
	}

	@Override
	public void create() {
		super.create();
		setTitle("Benutzer-Anmeldung");
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

		final Label lbtName = new Label(container, SWT.NONE);
		lbtName.setText("Name");
		txtName = new Text(container, SWT.BORDER);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtName);

		final Label lbtLastName = new Label(container, SWT.NONE);
		lbtLastName.setText("Passwort");
		txtPassword = new Text(container, SWT.BORDER | SWT.PASSWORD);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtPassword);

		createBinding();

		return area;
	}

	@Override
	protected void buttonPressed(final int buttonId) {
		validateLogin();
	}

	private boolean validateLogin() {
		try {
			final String salt = ServiceLocator.getInstance().getLoginService().requestSaltForUserName(curator.getName());
			final String hashPassword = CryptoUtil.hashPassword(curator.getPassword(), salt);
			final String sessionToken = ServiceLocator.getInstance().getLoginService().requestSessionTokenForHash(curator.getName(),
					hashPassword);
			if (sessionToken == null || sessionToken.isEmpty()) {
				setMessage("Name und Passwort stimmen nicht überein !", IMessageProvider.ERROR);
				return false;
			}
			setMessage("", IMessageProvider.NONE); //$NON-NLS-1$
			SessionHolder.getInstance().setSessionToken(sessionToken);
			return true;
		} catch (final ConnectException e) {
			setMessage("Es besteht ein Verbindungs-Problem mit dem Server", IMessageProvider.WARNING);
		}
		return false;
	}

	private void createBinding() {
		BindingHelper.bindStringToTextField(txtName, curator, Curator.class, Curator.FIELD_NAME, bindingContext);
		BindingHelper.bindStringToTextField(txtPassword, curator, Curator.class, Curator.FIELD_PASSWORD, bindingContext);
	}

}
