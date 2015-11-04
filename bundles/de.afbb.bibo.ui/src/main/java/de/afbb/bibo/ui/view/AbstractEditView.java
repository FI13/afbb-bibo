package de.afbb.bibo.ui.view;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.EditorPart;

abstract class AbstractEditView extends EditorPart {

	private Image imageValidation;
	private Label lblValidationImage;
	private Label lblValidationMessage;
	protected final DataBindingContext bindingContext = new DataBindingContext();
	protected FormToolkit toolkit = new FormToolkit(Display.getCurrent());

	@Override
	public void createPartControl(final Composite parent) {
//		final ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
//		scrolledComposite.setLayout(new GridLayout(1, false));
//
//		final Composite wrapperComposite = new Composite(scrolledComposite, SWT.NONE);
//		wrapperComposite.setLayout(new GridLayout(2, false));
//		wrapperComposite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
//		GridDataFactory.fillDefaults().grab(true, true).applyTo(wrapperComposite);
//
//		lblValidationImage = new Label(wrapperComposite, SWT.NONE);
//		lblValidationMessage = new Label(wrapperComposite, SWT.NONE);
//		lblValidationImage.setText("image");
//		lblValidationMessage.setText("text");
//
//		final Composite content = new Composite(wrapperComposite, SWT.NONE);
//		GridDataFactory.fillDefaults().grab(true, true).span(2, 1).applyTo(content);
//
//		initUi(content);
//
//		scrolledComposite.setContent(wrapperComposite);
//		wrapperComposite.setSize(wrapperComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
//		scrolledComposite.setMinSize(wrapperComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
//		scrolledComposite.setExpandHorizontal(true);
//		scrolledComposite.setExpandVertical(true);
		initUi(parent);
		initBinding();
	}

	/**
	 * initializes the UX of the editor
	 */
	protected abstract void initUi(final Composite parent);

	/**
	 * initializes the databinding for the editor
	 */
	protected abstract void initBinding();

	@Override
	public void doSave(final IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
}
