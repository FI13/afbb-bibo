package de.afbb.bibo.ui.observable.value;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

/**
 * checks that an observable is filled
 * 
 * @author dbecker
 */
public class NotEmptyValue implements IValidator {

	public static final String MSG = "Dieses Feld muss gefüllt werden!";

	@Override
	public IStatus validate(final Object value) {
		if (value == null) {
			return ValidationStatus.info(MSG);
		}
		if (value instanceof String && ((String) value).isEmpty()) {
			return ValidationStatus.info(MSG);
		}
		return ValidationStatus.ok();
	}

}
