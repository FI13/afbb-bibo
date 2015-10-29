package de.afbb.bibo.ui.dialog;

import org.eclipse.core.databinding.DataBindingContext;
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

import de.afbb.bibo.share.model.Typ;

public class CreateTypeDialog extends TitleAreaDialog {

	private Text txtName;
	private final Typ type = new Typ();
	private final DataBindingContext bindingContext = new DataBindingContext();

	public CreateTypeDialog(final Shell parentShell) {
		super(parentShell);
	}

	@Override
	public void create() {
		super.create();
		setTitle("Neuanlage Medien-Typ");
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite area = (Composite) super.createDialogArea(parent);
		final Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		final GridLayout layout = new GridLayout(3, false);
		layout.marginWidth = 30;
		layout.horizontalSpacing = 10;
		container.setLayout(layout);

		final Label lbtName = new Label(container, SWT.NONE);
		lbtName.setText("Name");
		txtName = new Text(container, SWT.BORDER);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtName);

//		final Label lbtLastName = new Label(container, SWT.NONE);
//		lbtLastName.setText("Passwort");
//		txtPassword = new Text(container, SWT.BORDER | SWT.PASSWORD);
//		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtPassword);

		createBinding();

		return area;
	}

	private void createBinding() {
//		BindingHelper.bindStringToTextField(txtName, type, Typ.class, Typ.FIELD_NAME, bindingContext);
	}

}
