package de.afbb.bibo.ui.form;

import java.net.ConnectException;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.afbb.bibo.databinding.BindingHelper;
import de.afbb.bibo.share.model.Copy;
import de.afbb.bibo.ui.BiboFormToolkit;
import de.afbb.bibo.ui.Messages;

/**
 * form that can display the movement information of a {@link Copy}
 *
 * @author David Becker
 *
 */
public class CopyMovementInventoryForm extends AbstractForm<Copy> {

	private Text txtInventoryDate;
	private Text txtLastBorrower;
	private Text txtLastCurator;
	private Text txtLastBorrowDate;

	public CopyMovementInventoryForm(final Composite parent, final Copy input, final DataBindingContext bindingContext,
			final BiboFormToolkit toolkit) throws ConnectException {
		super(parent, input, bindingContext, toolkit);
	}

	@Override
	protected void initUi(final Composite parent) {
		final Composite content = toolkit.createComposite(parent, SWT.NONE);
		content.setLayout(new GridLayout(2, false));
		content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridDataFactory.swtDefaults().span(2, 1).applyTo(toolkit.createLabel(content, "Letzte Inventur"));
		toolkit.createLabel(content, Messages.DATE);
		txtInventoryDate = toolkit.createText(content, EMPTY_STRING, SWT.READ_ONLY | SWT.BORDER);
		GridDataFactory.fillDefaults().grab(true, false).span(2, 1)
				.applyTo(new Label(content, SWT.SEPARATOR | SWT.HORIZONTAL));

		GridDataFactory.swtDefaults().span(2, 1).applyTo(toolkit.createLabel(content, "Rückgabe"));
		toolkit.createLabel(content, Messages.DATE);
		txtLastBorrowDate = toolkit.createText(content, EMPTY_STRING, SWT.READ_ONLY | SWT.BORDER);
		toolkit.createLabel(content, Messages.CURATOR);
		txtLastCurator = toolkit.createText(content, EMPTY_STRING, SWT.READ_ONLY | SWT.BORDER);
		toolkit.createLabel(content, Messages.BORROWER);
		txtLastBorrower = toolkit.createText(content, EMPTY_STRING, SWT.READ_ONLY | SWT.BORDER);

		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtLastCurator);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtLastBorrower);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtInventoryDate);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtLastBorrowDate);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtLastBorrower);
		GridDataFactory.fillDefaults().hint(200, SWT.DEFAULT).grab(true, true).applyTo(content);
	}

	@Override
	protected void initBinding() {
		BindingHelper.bindObjectToTextField(txtInventoryDate, getInputObservable(), Copy.class,
				Copy.FIELD_DATE_INVENTORY, bindingContext);
		BindingHelper.bindObjectToTextField(txtLastBorrowDate, getInputObservable(), Copy.class,
				Copy.FIELD_DATE_LAST_BORROW, bindingContext);

		BindingHelper.bindObjectToTextField(txtLastCurator, getInputObservable(), Copy.class, Copy.FIELD_LAST_CURATOR,
				bindingContext);
		BindingHelper.bindObjectToTextField(txtLastBorrower, getInputObservable(), Copy.class, Copy.FIELD_LAST_BORROWER,
				bindingContext);
	}

}
