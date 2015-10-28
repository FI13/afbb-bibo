package de.afbb.bibo.databinding;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

/**
 * utility class to hold common binding methods
 * 
 * @author dbecker
 */
public final class BindingHelper {

	private BindingHelper() {
	}

	public static void bindStringToTextField(final Text textField, final Object entity, final Class<?> entityClass,
			final String propertyName, final DataBindingContext bindingContext) {
		final ISWTObservableValue targetObservable = SWTObservables.observeText(textField, SWT.Modify);
		final IObservableValue modelObservable = BeanProperties.value(entityClass, propertyName).observe(entity);
		bindingContext.bindValue(targetObservable, modelObservable);
	}

}
