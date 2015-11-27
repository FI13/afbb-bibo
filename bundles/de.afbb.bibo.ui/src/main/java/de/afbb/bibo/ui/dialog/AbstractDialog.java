package de.afbb.bibo.ui.dialog;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

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
import de.afbb.bibo.ui.observable.value.StatusAsObservable;

/**
 * abstract super class for dialogs that provide consolidation of error messages
 * in title area
 * 
 * @author dbecker
 */
abstract class AbstractDialog extends TitleAreaDialog {

	protected final DataBindingContext bindingContext = new DataBindingContext();
	private final IObservableValue validationStatus = new WritableValue(IStatus.OK, IStatus.class);
	protected final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

	protected AbstractDialog(final Shell parentShell) {
		super(parentShell);
	}

	public void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(propertyName, listener);
	}

	@Override
	public void create() {
		super.create();
		setTitle(getTitle());

		initBinding();
		aggregateStatus();
	}

	/**
	 * aggregates the validation status for binding context and displays message
	 * in title area
	 */
	protected void aggregateStatus() {
		final AggregateValidationStatus aggregateValidationStatus = BindingHelper
				.aggregateValidationStatus(bindingContext);
		bindingContext.bindValue(validationStatus, aggregateValidationStatus,
				new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER),
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
		bindingContext.bindValue(SWTObservables.observeEnabled(getButton(IDialogConstants.OK_ID)),
				new StatusAsObservable(aggregateValidationStatus), null, null);
	}

	/**
	 * initializes the databinding for the dialog
	 */
	protected abstract void initBinding();

	/**
	 * get the title for the dialog
	 * 
	 * @return title
	 */
	protected abstract String getTitle();

}
