package de.afbb.bibo.ui.observable.value;

import org.eclipse.core.databinding.observable.value.ComputedValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

/**
 * checks that two observables observe the same value
 * 
 * @author dbecker
 */
public class EqualsValue extends ComputedValue {

	private final IObservableValue value1;
	private final IObservableValue value2;
	private final String msg;

	/**
	 * Constructor.
	 *
	 * @param value1
	 *            observable
	 * @param value2
	 *            observable
	 * @throws IllegalArgumentException
	 *             when any parameter is null
	 */
	public EqualsValue(final IObservableValue value1, final IObservableValue value2, final String msg) {
		super(IStatus.class);
		if (value1 == null || value2 == null || msg == null) {
			throw new IllegalArgumentException("no argument should be null");//$NON-NLS-1$
		}
		this.value1 = value1;
		this.value2 = value2;
		this.msg = msg;
	}

	@Override
	protected Object calculate() {
		final Object object1 = value1.getValue();
		final Object object2 = value2.getValue();

		return object1 == null && object2 != null || !object1.equals(object2) ? ValidationStatus.error(msg) : ValidationStatus.ok();
	}

}
