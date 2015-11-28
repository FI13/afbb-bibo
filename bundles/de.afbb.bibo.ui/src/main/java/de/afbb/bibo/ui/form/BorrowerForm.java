package de.afbb.bibo.ui.form;

import java.net.ConnectException;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import de.afbb.bibo.databinding.BindingHelper;
import de.afbb.bibo.share.model.Borrower;
import de.afbb.bibo.ui.BiboFormToolkit;
import de.afbb.bibo.ui.events.UpdateDirtyListener;

public class BorrowerForm extends AbstractForm<Borrower> {

	private Text textFirstname;
	private Text textLastname;
	private Text textEMail;
	private Text textTel;
	private Text textInfo;

	public BorrowerForm(final Composite parent, final Borrower input, final DataBindingContext bindingContext,
			final BiboFormToolkit toolkit) throws ConnectException {
		super(parent, input, bindingContext, toolkit);
	}

	@Override
	protected void initUi(final Composite parent) {
		final Composite content = toolkit.createComposite(parent, SWT.NONE);
		content.setLayout(new GridLayout(2, false));
		content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		toolkit.createLabel(content, "Vorname");
		textFirstname = toolkit.createText(content, EMPTY_STRING);

		toolkit.createLabel(content, "Nachname");
		textLastname = toolkit.createText(content, EMPTY_STRING);

		toolkit.createLabel(content, "E-Mail-Adresse");
		textEMail = toolkit.createText(content, EMPTY_STRING);

		toolkit.createLabel(content, "Telefonnummer");
		textTel = toolkit.createText(content, EMPTY_STRING);

		toolkit.createLabel(content, "Informationen");
		textInfo = toolkit.createText(content, EMPTY_STRING);

		final UpdateDirtyListener updateDirtyListener = new UpdateDirtyListener();
		textFirstname.addFocusListener(updateDirtyListener);
		textLastname.addFocusListener(updateDirtyListener);
		textEMail.addFocusListener(updateDirtyListener);
		textTel.addFocusListener(updateDirtyListener);
		textInfo.addFocusListener(updateDirtyListener);

		GridDataFactory.fillDefaults().grab(true, false).applyTo(textFirstname);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(textLastname);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(textEMail);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(textTel);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(textInfo);
	}

	@Override
	protected void initBinding() throws ConnectException {
		BindingHelper.bindStringToTextField(textFirstname, input, Borrower.class, Borrower.FIELD_FIRSTNAME,
				bindingContext, true);
		BindingHelper.bindStringToTextField(textLastname, input, Borrower.class, Borrower.FIELD_SURNAME, bindingContext,
				true);
		BindingHelper.bindStringToTextField(textEMail, input, Borrower.class, Borrower.FIELD_EMAIL, bindingContext,
				false);
		BindingHelper.bindStringToTextField(textTel, input, Borrower.class, Borrower.FIELD_PHONENUMER, bindingContext,
				false);
		BindingHelper.bindStringToTextField(textInfo, input, Borrower.class, Borrower.FIELD_INFO, bindingContext,
				false);
	}

}
