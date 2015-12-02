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
import de.afbb.bibo.share.model.Copy;
import de.afbb.bibo.ui.BiboFormToolkit;
import de.afbb.bibo.ui.Messages;

/**
 * form that can display the movement information of a {@link Copy}
 *
 * @author david
 *
 */
public class CopyMovementForm extends AbstractForm<Copy> {

	private Text txtBorrower;
	private Text txtLastBorrower;
	private Text txtCurator;
	private Text txtLastCurator;
	private Text txtBorrowDate;
	private Text txtLastBorrowDate;

	public CopyMovementForm(final Composite parent, final Copy input, final DataBindingContext bindingContext,
			final BiboFormToolkit toolkit) throws ConnectException {
		super(parent, input, bindingContext, toolkit);
	}

	@Override
	protected void initUi(final Composite parent) {
		final Composite content = toolkit.createComposite(parent, SWT.NONE);
		content.setLayout(new GridLayout(2, false));
		content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridDataFactory.swtDefaults().span(2, 1).applyTo(toolkit.createLabel(content, "Ausleihvorgang"));
		toolkit.createLabel(content, Messages.DATE);
		txtBorrowDate = toolkit.createText(content, EMPTY_STRING, SWT.READ_ONLY | SWT.BORDER);
		toolkit.createLabel(content, Messages.CURATOR);
		txtCurator = toolkit.createText(content, EMPTY_STRING, SWT.READ_ONLY | SWT.BORDER);
		toolkit.createLabel(content, Messages.BORROWER);
		txtBorrower = toolkit.createText(content, EMPTY_STRING, SWT.READ_ONLY | SWT.BORDER);
		GridDataFactory.swtDefaults().span(2, 1).applyTo(toolkit.createLabel(content, "Rückgabe"));
		toolkit.createLabel(content, Messages.DATE);
		txtLastBorrowDate = toolkit.createText(content, EMPTY_STRING, SWT.READ_ONLY | SWT.BORDER);
		toolkit.createLabel(content, Messages.CURATOR);
		txtLastCurator = toolkit.createText(content, EMPTY_STRING, SWT.READ_ONLY | SWT.BORDER);
		toolkit.createLabel(content, Messages.BORROWER);
		txtLastBorrower = toolkit.createText(content, EMPTY_STRING, SWT.READ_ONLY | SWT.BORDER);

		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtCurator);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtBorrower);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtLastCurator);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtLastBorrower);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtBorrowDate);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtLastBorrowDate);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtLastBorrower);
		GridDataFactory.fillDefaults().hint(200, SWT.DEFAULT).grab(true, true).applyTo(content);
	}

	@Override
	protected void initBinding() {
		BindingHelper.bindObjectToTextField(txtBorrowDate, getInputObservable(), Copy.class, Copy.FIELD_DATE_BORROW,
				bindingContext);
		BindingHelper.bindObjectToTextField(txtLastBorrowDate, getInputObservable(), Copy.class,
				Copy.FIELD_DATE_LAST_BORROW, bindingContext);

		BindingHelper.bindObjectToTextField(txtCurator, getInputObservable(), Copy.class, Copy.FIELD_CURATOR,
				bindingContext);
		BindingHelper.bindObjectToTextField(txtLastCurator, getInputObservable(), Copy.class, Copy.FIELD_LAST_CURATOR,
				bindingContext);
		BindingHelper.bindObjectToTextField(txtBorrower, getInputObservable(), Copy.class, Copy.FIELD_BORROWER,
				bindingContext);
		BindingHelper.bindObjectToTextField(txtLastBorrower, getInputObservable(), Copy.class, Copy.FIELD_LAST_BORROWER,
				bindingContext);
	}

}
