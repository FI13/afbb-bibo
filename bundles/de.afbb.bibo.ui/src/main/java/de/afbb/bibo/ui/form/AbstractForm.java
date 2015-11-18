package de.afbb.bibo.ui.form;

import org.eclipse.core.databinding.DataBindingContext;
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

	protected final DataBindingContext bindingContext;
	protected final BiboFormToolkit toolkit;

	protected Input input;

	/**
	 * Constructor
	 * 
	 * @param parent
	 * @param style
	 * @param bindingContext
	 *            context that bindings should happen in. can be
	 *            <code>null</code>
	 */
	public AbstractForm(Composite parent, int style, Input input, DataBindingContext bindingContext,
			BiboFormToolkit toolkit) {
		super(parent, style);
		this.input = input;
		this.bindingContext = bindingContext != null ? bindingContext : new DataBindingContext();
		this.toolkit = toolkit != null ? toolkit : new BiboFormToolkit(Display.getCurrent());
		createUi();
		initBinding();

		setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
	}

	/**
	 * creates a composite around the form that will expand automatically
	 * 
	 * @param parent
	 */
	protected void createUi() {
		GridLayout layoutOuter = new GridLayout(1, false);
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
	 */
	protected abstract void initBinding();

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
	public void setInput(Input input) {
		this.input = input;
	}

	/**
	 * @return the bindingContext
	 */
	public DataBindingContext getBindingContext() {
		return bindingContext;
	}

}
