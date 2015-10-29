package de.afbb.bibo.ui.dialog;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.afbb.bibo.databinding.BindingHelper;
import de.afbb.bibo.share.ServiceLocator;
import de.afbb.bibo.share.model.Typ;
import de.afbb.bibo.ui.Activator;
import de.afbb.bibo.ui.ImagePath;

public class CreateTypeDialog extends TitleAreaDialog {

	private Text txtName;
	private final Typ type = new Typ();
	private final DataBindingContext bindingContext = new DataBindingContext();
	ImageRegistry imageRegistry = new ImageRegistry();

	public CreateTypeDialog(final Shell parentShell) {
		super(parentShell);

		imageRegistry.put(ImagePath.ICON_BOOK2, Activator.getImageDescriptor(ImagePath.ICON_BOOK2));
		imageRegistry.put(ImagePath.ICON_CD, Activator.getImageDescriptor(ImagePath.ICON_CD));
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
		final GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 30;
		layout.horizontalSpacing = 10;
		container.setLayout(layout);

		final Label lbtName = new Label(container, SWT.NONE);
		lbtName.setText("Name");
		txtName = new Text(container, SWT.BORDER);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtName);

		final Composite iconComposite = new Composite(container, SWT.NONE);
		GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.CENTER).span(2, 1).applyTo(iconComposite);
		final GridLayout buttonLayout = new GridLayout(3, false);
		buttonLayout.marginWidth = 30;
		buttonLayout.horizontalSpacing = 10;
		iconComposite.setLayout(buttonLayout);

		final Button btnNone = new Button(iconComposite, SWT.RADIO);
		btnNone.setText("Ohne");
		final Button btnBook = new Button(iconComposite, SWT.RADIO);
		btnBook.setText("Buch");
		final Button btnCd = new Button(iconComposite, SWT.RADIO);
		btnCd.setText("CD");

		setUpButton(btnNone, null);
		setUpButton(btnBook, ImagePath.ICON_BOOK2);
		setUpButton(btnCd, ImagePath.ICON_CD);

		btnNone.setSelection(true);

		createBinding();
		return area;
	}

	private void setUpButton(final Button button, final String imagePath) {
		button.setImage(imageRegistry.get(imagePath));
		button.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				type.setIconPath(imagePath);
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {
			}
		});
	}

	@Override
	protected void buttonPressed(final int buttonId) {
		if (buttonId == Dialog.OK) {
			ServiceLocator.getInstance().getTypService().create(type);
			okPressed();
		} else {
			cancelPressed();
		}
	}

	private void createBinding() {
		BindingHelper.bindStringToTextField(txtName, type, Typ.class, Typ.FIELD_NAME, bindingContext);
	}

}
