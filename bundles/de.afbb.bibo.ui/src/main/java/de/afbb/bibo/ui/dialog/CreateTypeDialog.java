package de.afbb.bibo.ui.dialog;

import java.net.ConnectException;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.layout.GridDataFactory;
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
import de.afbb.bibo.servletclient.ServiceLocator;
import de.afbb.bibo.share.model.IconType;
import de.afbb.bibo.share.model.MediumType;
import de.afbb.bibo.ui.BiboImageRegistry;
import de.afbb.bibo.ui.IconSize;
import de.afbb.bibo.ui.Messages;

public class CreateTypeDialog extends AbstractDialog {

	private Text txtName;
	private final MediumType type = new MediumType();

	public CreateTypeDialog(final Shell parentShell) {
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

		final Label lbtName = new Label(container, SWT.NONE);
		lbtName.setText("Name");
		txtName = new Text(container, SWT.BORDER);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtName);

		final Label lblIcon = new Label(container, SWT.NONE);
		lblIcon.setText("Icon");
		final Composite iconComposite = new Composite(container, SWT.NONE);
		GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.CENTER).applyTo(iconComposite);
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

		setUpButton(btnNone, IconType.NONE, null);
		setUpButton(btnBook, IconType.BOOK, IconSize.medium);
		setUpButton(btnCd, IconType.CD, IconSize.medium);

		btnNone.setSelection(true);
		setTitleImage(BiboImageRegistry.getImage(IconType.MEDIA, IconSize.huge));

		return area;
	}

	private void setUpButton(final Button button, final IconType iconType, final IconSize iconSize) {
		button.setImage(BiboImageRegistry.getImage(iconType, iconSize));
		button.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				type.setIcon(iconType);
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {
			}
		});
	}

	@Override
	protected void buttonPressed(final int buttonId) {
		if (buttonId == Dialog.OK) {
			try {
				ServiceLocator.getInstance().getTypService().create(type);
				okPressed();
			} catch (final ConnectException e) {
				setMessage(Messages.MESSAGE_ERROR_CONNECTION, IMessageProvider.WARNING);
			}
		} else {
			cancelPressed();
		}
	}

	@Override
	protected void initBinding() {
		BindingHelper.bindStringToTextField(txtName, type, MediumType.class, MediumType.FIELD_NAME, bindingContext,
				true);
	}

	@Override
	protected String getTitle() {
		return "Neuanlage Medien-Typ";
	}

}
