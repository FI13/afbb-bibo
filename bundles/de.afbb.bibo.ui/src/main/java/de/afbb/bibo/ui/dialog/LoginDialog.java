package de.afbb.bibo.ui.dialog;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class LoginDialog extends TitleAreaDialog {

	private Text lastNameText;
	private Text txtFirstName;

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
		container.setLayout(layout);

		final Label lbtFirstName = new Label(container, SWT.NONE);
		lbtFirstName.setText("Name");

		final GridData dataFirstName = new GridData();
		dataFirstName.grabExcessHorizontalSpace = true;
		dataFirstName.horizontalAlignment = GridData.FILL;

		txtFirstName = new Text(container, SWT.BORDER);
		txtFirstName.setLayoutData(dataFirstName);

		final Label lbtLastName = new Label(container, SWT.NONE);
		lbtLastName.setText("Passwort");

		final GridData dataLastName = new GridData();
		dataLastName.grabExcessHorizontalSpace = true;
		dataLastName.horizontalAlignment = GridData.FILL;
		lastNameText = new Text(container, SWT.BORDER | SWT.PASSWORD);
		lastNameText.setLayoutData(dataLastName);
		return area;
	}

}
