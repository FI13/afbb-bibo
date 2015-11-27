package de.afbb.bibo.ui.dialog;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.afbb.bibo.ui.BiboFormToolkit;
import de.afbb.bibo.ui.form.AbstractForm;

/**
 * dialog that displays the content of an {@link AbstractForm}
 * 
 * @author David Becker
 *
 * @param <Input>
 *            type of input that is displayed in the form
 * @param <Form>
 *            form to show content
 */
public abstract class AbstractFormDialog<Input, Form extends AbstractForm<Input>> extends AbstractDialog {

	protected Input input;
	protected Form form;
	private Class<Form> formClass;
	private Class<Input> inputClass;
	protected final BiboFormToolkit toolkit = new BiboFormToolkit(Display.getDefault());

	protected AbstractFormDialog(Shell parentShell, Class<Input> inputClass, Class<Form> formClass) {
		super(parentShell);
		this.inputClass = inputClass;
		this.formClass = formClass;
	}

	protected Control createDialogArea(final Composite parent) {
		final Composite area = (Composite) super.createDialogArea(parent);
		final Composite container = toolkit.createComposite(area);
		container.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		final GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = 30;
		layout.horizontalSpacing = 10;
		container.setLayout(layout);

		// some black magic to create an instance of our form
		try {
			Constructor<Form> constructor = formClass.getConstructor(Composite.class, inputClass,
					DataBindingContext.class, BiboFormToolkit.class);
			form = constructor.newInstance(container, inputClass.newInstance(), bindingContext, toolkit);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		GridDataFactory.fillDefaults().grab(true, true).applyTo(form);
		return area;
	}

	@Override
	protected void initBinding() {
		/*
		 * binding is most likely done in sub-form, so we don't force clients to
		 * implement this method
		 */
	}

}
