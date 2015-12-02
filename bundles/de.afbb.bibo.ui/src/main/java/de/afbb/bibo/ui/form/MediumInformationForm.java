package de.afbb.bibo.ui.form;

import java.net.ConnectException;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import de.afbb.bibo.databinding.BindingHelper;
import de.afbb.bibo.share.ServiceLocator;
import de.afbb.bibo.share.model.Copy;
import de.afbb.bibo.share.model.Medium;
import de.afbb.bibo.share.model.MediumType;
import de.afbb.bibo.ui.BiboFormToolkit;
import de.afbb.bibo.ui.Messages;
import de.afbb.bibo.ui.provider.MediumTypeLabelProvider;

/**
 * form to display the medium informations of a {@link Copy}
 *
 * @author David Becker
 *
 */
public class MediumInformationForm extends AbstractForm<Copy> {

	private Text txtIsbn;
	private Text txtEdition;
	private Text txtTitle;
	private Text txtAuthor;
	private Text txtLanguage;
	private Text txtPublisher;

	private CCombo comboMediumType;

	public MediumInformationForm(final Composite parent, final Copy input, final DataBindingContext bindingContext,
			final BiboFormToolkit toolkit) throws ConnectException {
		super(parent, input, bindingContext, toolkit);
	}

	@Override
	protected void initUi(final Composite parent) {
		final Composite content = toolkit.createComposite(parent, SWT.NONE);
		content.setLayout(new GridLayout(2, false));
		content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		toolkit.createLabel(content, Messages.TITLE);
		txtTitle = toolkit.createText(content, EMPTY_STRING, SWT.READ_ONLY | SWT.BORDER);
		toolkit.createLabel(content, Messages.AUTHOR);
		txtAuthor = toolkit.createText(content, EMPTY_STRING, SWT.READ_ONLY | SWT.BORDER);
		toolkit.createLabel(content, Messages.LANGUAGE);
		txtLanguage = toolkit.createText(content, EMPTY_STRING, SWT.READ_ONLY | SWT.BORDER);
		toolkit.createLabel(content, Messages.PUBLISHER);
		txtPublisher = toolkit.createText(content, EMPTY_STRING, SWT.READ_ONLY | SWT.BORDER);
		toolkit.createLabel(content, Messages.ISBN);
		txtIsbn = toolkit.createText(content, EMPTY_STRING, SWT.RIGHT | SWT.READ_ONLY | SWT.BORDER);
		toolkit.createLabel(content, Messages.EDITION);
		txtEdition = toolkit.createText(content, EMPTY_STRING, SWT.READ_ONLY | SWT.BORDER);
		toolkit.createLabel(content, Messages.TYPE);
		comboMediumType = new CCombo(content, SWT.SHADOW_NONE | SWT.BORDER | SWT.READ_ONLY);
		toolkit.adapt(comboMediumType);

		comboMediumType.setEnabled(false);

		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtTitle);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtAuthor);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtLanguage);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtPublisher);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtIsbn);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtEdition);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(comboMediumType);
		GridDataFactory.fillDefaults().hint(200, SWT.DEFAULT).grab(true, true).applyTo(content);
	}

	@Override
	protected void initBinding() throws ConnectException {
		BindingHelper.bindStringToTextField(txtEdition, getInputObservable(), Copy.FIELD_EDITION, bindingContext,
				false);
		BindingHelper.bindStringToTextField(txtTitle, getInputObservable(),
				Copy.FIELD_MEDIUM + DOT + Medium.FIELD_TITLE, bindingContext, false);
		BindingHelper.bindStringToTextField(txtAuthor, getInputObservable(),
				Copy.FIELD_MEDIUM + DOT + Medium.FIELD_AUTHOR, bindingContext, false);
		BindingHelper.bindStringToTextField(txtLanguage, getInputObservable(),
				Copy.FIELD_MEDIUM + DOT + Medium.FIELD_LANGUAGE, bindingContext, false);
		BindingHelper.bindStringToTextField(txtPublisher, getInputObservable(),
				Copy.FIELD_MEDIUM + DOT + Medium.FIELD_PUBLISHER, bindingContext, false);
		BindingHelper.bindStringToTextField(txtIsbn, getInputObservable(), Copy.FIELD_MEDIUM + DOT + Medium.FIELD_ISBN,
				bindingContext, false);

		BindingHelper.bindObjectToCCombo(comboMediumType, getInputObservable(), Copy.class,
				Copy.FIELD_MEDIUM + DOT + Medium.FIELD_TYPE, MediumType.class,
				ServiceLocator.getInstance().getTypService().list(), new MediumTypeLabelProvider(), bindingContext,
				false);

	}

}
