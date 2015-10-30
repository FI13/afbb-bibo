package de.afbb.bibo.ui.observable.value;

import org.eclipse.core.databinding.observable.value.ComputedValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.IStatus;

public class StatusAsObservable extends ComputedValue {

	private final IObservableValue value;

	public StatusAsObservable(final IObservableValue value) {
		super(Boolean.class);
		this.value = value;
	}

	@Override
	protected Object calculate() {
		final IStatus v = (IStatus) value.getValue();
		return v == null || v.getSeverity() == IStatus.OK;
	}

}
