package de.afbb.bibo.ui.dialog;

import org.eclipse.core.databinding.AggregateValidationStatus;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.widgets.Shell;

import de.afbb.bibo.databinding.BindingHelper;
import de.afbb.bibo.ui.observable.value.StatusToObservable;

public abstract class AbstractDialog extends TitleAreaDialog {

	protected final DataBindingContext bindingContext = new DataBindingContext();
	private final IObservableValue validationStatus = new WritableValue(IStatus.OK, IStatus.class);

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
		final AggregateValidationStatus aggregateValidationStatus = BindingHelper.aggregateValidationStatus(bindingContext);
		bindingContext.bindValue(validationStatus, aggregateValidationStatus, new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER),
				new UpdateValueStrategy().setAfterConvertValidator(new IValidator() {

					@Override
					public IStatus validate(final Object value) {
						final Status status = (Status) value;
						switch (status.getSeverity()) {
							case IStatus.OK:
								setMessage("", IMessageProvider.NONE);//$NON-NLS-1$
								break;
							case IStatus.INFO:
								setMessage(status.getMessage(), IMessageProvider.INFORMATION);
								break;
							case IStatus.WARNING:
								setMessage(status.getMessage(), IMessageProvider.WARNING);
								break;
							case IStatus.ERROR:
								setMessage(status.getMessage(), IMessageProvider.ERROR);
								break;
						}
						return Status.OK_STATUS;
					}
				}));
		bindingContext.bindValue(SWTObservables.observeEnabled(getButton(IDialogConstants.OK_ID)), new StatusToObservable(
				aggregateValidationStatus), null, null);
	}

	protected abstract void createBinding();

	protected abstract String getTitle();

}
