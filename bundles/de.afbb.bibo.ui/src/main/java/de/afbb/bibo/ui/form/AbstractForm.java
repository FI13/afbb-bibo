package de.afbb.bibo.ui.form;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.ConnectException;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import de.afbb.bibo.ui.BiboFormToolkit;

/**
 * super class for all forms
 *
 * @author David Becker
 *
 */
public abstract class AbstractForm<Input> extends Composite {

	protected static final String EMPTY_STRING = "";//$NON-NLS-1$
	protected static final String DOT = ".";//$NON-NLS-1$
	public static final String INPUT = "input";//$NON-NLS-1$

	protected final DataBindingContext bindingContext;
	protected final BiboFormToolkit toolkit;

	protected Input input;
	protected final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
	private final IObservableValue inputObservable = BeansObservables.observeValue(this, INPUT);

	public void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(propertyName, listener);
	}

	/**
	 * Constructor.<br>
	 * will be invoked by {@link AbstractFormDialog}
	 *
	 * @param parent
	 *            parent composite
	 * @param input
	 *            input to bind in this form
	 * @param bindingContext
	 *            context that bindings should happen in. can be
	 *            <code>null</code>
	 * @param toolkit
	 * @throws ConnectException
	 */
	public AbstractForm(final Composite parent, final Input input, final DataBindingContext bindingContext,
			final BiboFormToolkit toolkit) throws ConnectException {
		super(parent, SWT.NONE);
		this.input = input;
		this.bindingContext = bindingContext != null ? bindingContext : new DataBindingContext();
		this.toolkit = toolkit != null ? toolkit : new BiboFormToolkit(Display.getCurrent());
		createUi();
		initBinding();
	}

	/**
	 * creates a composite around the form that will expand automatically
	 *
	 * @param parent
	 */
	protected void createUi() {
		final GridLayout layoutOuter = new GridLayout(1, false);
		layoutOuter.marginHeight = layoutOuter.marginWidth = 0;
		setLayout(layoutOuter);
		initUi(this);
	}

	/**
	 * initializes the user interface of the editor
	 */
	protected abstract void initUi(final Composite parent);

	/**
	 * initializes the databinding for this form
	 *
	 * @throws ConnectException
	 */
	protected abstract void initBinding() throws ConnectException;

	/**
	 * @return the input
	 */
	public Input getInput() {
		return input;
	}

	/**
	 * @param input
	 *            the input to set
	 */
	public void setInput(final Input input) {
		changeSupport.firePropertyChange(INPUT, this.input, this.input = input);
	}

	/**
	 * @return the bindingContext
	 */
	public DataBindingContext getBindingContext() {
		return bindingContext;
	}

	/**
	 * @return the inputObservable
	 */
	public IObservableValue getInputObservable() {
		return inputObservable;
	}

}
