package de.afbb.bibo.databinding;

import java.util.HashMap;

import org.eclipse.core.databinding.AggregateValidationStatus;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import de.afbb.bibo.ui.observable.value.NotEmptyValue;

/**
 * utility class to hold common binding methods
 * 
 * @author dbecker
 */
public final class BindingHelper {

	private static final HashMap<Control, ControlDecoration> decorations = new HashMap<Control, ControlDecoration>();

	private BindingHelper() {
	}

	/**
	 * creates a bidirectional binding for a text field.<br>
	 * changes in model will be instantly reflected by the target and vice versa.
	 * 
	 * @param textField
	 *            widget for input
	 * @param entity
	 *            class that holds the model informations
	 * @param entityClass
	 *            class of entity
	 * @param propertyName
	 *            name of the property that should be binded. expected to be of {@link String} type
	 * @param bindingContext
	 *            context of the binding
	 * @param isRequired
	 *            should the field be filled by the user?
	 * @return binding
	 */
	public static Binding bindStringToTextField(final Text textField, final Object entity, final Class<?> entityClass,
			final String propertyName, final DataBindingContext bindingContext, final boolean isRequired) {
		final ISWTObservableValue targetObservable = SWTObservables.observeText(textField, SWT.Modify);
		final IObservableValue modelObservable = BeanProperties.value(entityClass, propertyName).observe(entity);
		final UpdateValueStrategy targetToModel = isRequired ? new UpdateValueStrategy().setAfterConvertValidator(new NotEmptyValue())
				: null;

		final Binding binding = bindingContext.bindValue(targetObservable, modelObservable, targetToModel, null);

		if (isRequired) {
			createControlDecoration(textField, FieldDecorationRegistry.DEC_INFORMATION, NotEmptyValue.MSG, isRequired);
		}

		return binding;
	}

	/**
	 * creates a control decoration to given control. will add the control to decorations map
	 * 
	 * @param control
	 *            to add decoration to
	 * @param decorationId
	 *            id for decoration. like {@link FieldDecorationRegistry#DEC_ERROR} or {@link FieldDecorationRegistry#DEC_WARNING}
	 * @param message
	 *            to display when decoration is shown
	 * @param isRequired
	 *            should the required asterisk be shown?
	 * @return created decoration
	 */
	public static ControlDecoration createControlDecoration(final Control control, final String decorationId, final String message,
			final boolean isRequired) {
		final ControlDecoration controlDecoration = new ControlDecoration(control, SWT.RIGHT | SWT.BOTTOM);
		final FieldDecoration fieldDecoration = FieldDecorationRegistry.getDefault().getFieldDecoration(decorationId);
		controlDecoration.setImage(fieldDecoration.getImage());
		decorations.put(control, controlDecoration);
		control.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(final DisposeEvent e) {
				final ControlDecoration remove = decorations.remove(control);
				remove.dispose();
			}

		});
		if (isRequired) {
			final ControlDecoration requiredDecoration = new ControlDecoration(control, SWT.RIGHT | SWT.TOP);
			final FieldDecoration requiredFieldDecoration = FieldDecorationRegistry.getDefault().getFieldDecoration(
					FieldDecorationRegistry.DEC_REQUIRED);
			requiredDecoration.setImage(requiredFieldDecoration.getImage());
			control.addDisposeListener(new DisposeListener() {

				@Override
				public void widgetDisposed(final DisposeEvent e) {
					requiredDecoration.dispose();
				}

			});
		}
		return controlDecoration;
	}

	/**
	 * aggregates the validation status for given {@link DataBindingContext}
	 * 
	 * @param bindingContext
	 * @return
	 */
	public static AggregateValidationStatus aggregateValidationStatus(final DataBindingContext bindingContext) {
		final AggregateValidationStatus aggregateStatus = new AggregateValidationStatus(bindingContext,
				AggregateValidationStatus.MAX_SEVERITY);
		aggregateStatus.addChangeListener(new IChangeListener() {

			@Override
			public void handleChange(final ChangeEvent event) {
				for (final Object o : bindingContext.getBindings()) {
					final Binding binding = (Binding) o;
					final IStatus status = (IStatus) binding.getValidationStatus().getValue();

					Control control = null;
					if (binding.getTarget() instanceof ISWTObservableValue) {
						final ISWTObservableValue swtObservableValue = (ISWTObservableValue) binding.getTarget();
						control = (Control) swtObservableValue.getWidget();
					}
					final ControlDecoration decoration = decorations.get(control);
					if (decoration != null) {
						if (status.isOK()) {
							decoration.hide();
						} else {
							decoration.setDescriptionText(status.getMessage());
							decoration.show();
						}
					}
				}
			}
		});
		return aggregateStatus;

	}

}
