package de.afbb.bibo.ui.dialog;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
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
import de.afbb.bibo.share.model.Curator;
import de.afbb.bibo.ui.observable.value.EqualsValue;

/**
 * dialog that creates a new instance of type {@link Curator}
 * 
 * @author dbecker
 */
public class CreateCuratorDialog extends AbstractDialog {

	private Text txtName;
	private Text txtPassword;
	private Text txtPassword2;
	private final Curator curator = new Curator();

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
	protected void createBinding() {
		final Binding bindingName = BindingHelper.bindStringToTextField(txtName, curator, Curator.class, Curator.FIELD_NAME, bindingContext,
				true);
		final Binding bindingPassword = BindingHelper.bindStringToTextField(txtPassword, curator, Curator.class, Curator.FIELD_PASSWORD,
				bindingContext, true);

		// validate that the two passwords are the same
		final ISWTObservableValue observePassword2 = SWTObservables.observeText(txtPassword2, SWT.Modify);
		new EqualsValue((IObservableValue) bindingPassword.getModel(), observePassword2,
				"Passwort & Passwort-Wiederholung passen nicht zusammen!");
	}

	@Override
	protected String getTitle() {
		return "Neuanlage Verwalter";
	}

}
