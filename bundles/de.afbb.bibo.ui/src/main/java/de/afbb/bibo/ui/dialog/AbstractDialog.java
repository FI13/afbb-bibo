package de.afbb.bibo.ui.dialog;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.widgets.Shell;

import de.afbb.bibo.databinding.BindingHelper;
import de.afbb.bibo.ui.observable.value.StatusToObservable;

public abstract class AbstractDialog extends TitleAreaDialog {

	protected final DataBindingContext bindingContext = new DataBindingContext();

	protected AbstractDialog(final Shell parentShell) {
		super(parentShell);
	}

	@Override
	public void create() {
		super.create();
		setTitle(getTitle());

		createBinding();
		aggregateStatus();

	}

	protected void aggregateStatus() {
		bindingContext.bindValue(SWTObservables.observeEnabled(getButton(IDialogConstants.OK_ID)), new StatusToObservable(BindingHelper
				.aggregateValidationStatus(bindingContext)), null, null);
	}

	protected abstract void createBinding();

	protected abstract String getTitle();

}
