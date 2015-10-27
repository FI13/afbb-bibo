package de.afbb.bibo.ui.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class LoginDialog extends Dialog {

	public LoginDialog(final Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		super.createDialogArea(parent);
		final Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(SWT.DEFAULT, SWT.DEFAULT, true, true, 1, 1));
		final Label name = new Label(composite, SWT.DEFAULT);
		name.setLayoutData(new GridData(SWT.DEFAULT, SWT.DEFAULT, true, true, 1, 1));
		name.setText("Name");
		final Text textName = new Text(composite, SWT.DEFAULT);
		textName.setLayoutData(new GridData(SWT.DEFAULT, SWT.DEFAULT, true, true, 1, 1));
		final Label psw = new Label(composite, SWT.DEFAULT);
		psw.setLayoutData(new GridData(SWT.DEFAULT, SWT.DEFAULT, true, true, 1, 1));
		psw.setText("Passwort");
		final Text textPsw = new Text(composite, SWT.DEFAULT | SWT.PASSWORD);
		textPsw.setLayoutData(new GridData(SWT.DEFAULT, SWT.DEFAULT, true, true, 1, 1));
		composite.pack();
		return composite;
	}

}
