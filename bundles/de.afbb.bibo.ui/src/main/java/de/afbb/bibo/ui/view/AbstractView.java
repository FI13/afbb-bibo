package de.afbb.bibo.ui.view;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.ConnectException;

import org.eclipse.core.databinding.AggregateValidationStatus;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.internal.runtime.LocalizationUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISaveablePart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.ViewPart;

import de.afbb.bibo.databinding.BindingHelper;
import de.afbb.bibo.exception.InvalidSessionException;
import de.afbb.bibo.ui.BiboFormToolkit;
import de.afbb.bibo.ui.Messages;
import de.afbb.bibo.ui.dialog.LoginDialog;

abstract class AbstractView<Input> extends ViewPart implements IDirtyEvaluate, ISaveablePart {

	protected static final String EMPTY_STRING = "";//$NON-NLS-1$
	public static final String INPUT = "input";//$NON-NLS-1$
	protected static final String DOT = ".";//$NON-NLS-1$
	private static final String OK = LocalizationUtils.safeLocalize("ok");//$NON-NLS-1$

	private Label lblValidationImage;
	private Label lblValidationMessage;
	private final IObservableValue validationStatus = new WritableValue(IStatus.OK, IStatus.class);
	protected final IObservableValue inputObservable = BeansObservables.observeValue(this, INPUT);

	protected final DataBindingContext bindingContext = new DataBindingContext();
	protected final BiboFormToolkit toolkit = new BiboFormToolkit(Display.getCurrent());
	protected final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

	protected Input input;
	protected Input inputCache;

	public void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(propertyName, listener);
	}

	@Override
	public void createPartControl(final Composite parent) {
		try {
			final Composite outer = toolkit.createComposite(parent, SWT.NONE);
			final GridLayout layout = new GridLayout(1, false);
			layout.marginHeight = layout.marginWidth = 0;
			outer.setLayout(layout);
			final GridData layoutData = new GridData(GridData.FILL_BOTH);
			outer.setLayoutData(layoutData);

			final Composite validationComposite = toolkit.createComposite(outer, SWT.NONE);
			validationComposite.setLayout(new GridLayout(2, false));
			lblValidationImage = toolkit.createLabel(validationComposite, EMPTY_STRING);
			lblValidationMessage = toolkit.createLabel(validationComposite, EMPTY_STRING);

			GridDataFactory.fillDefaults().grab(true, false).applyTo(validationComposite);
			GridDataFactory.fillDefaults().applyTo(lblValidationImage);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(lblValidationMessage);

			final ScrolledComposite content = new ScrolledComposite(outer, SWT.H_SCROLL | SWT.V_SCROLL);
			content.setExpandHorizontal(true);
			content.setExpandVertical(true);
			content.setLayout(layout);
			content.setLayoutData(layoutData);
			final Composite children = initUi(content);
			content.setMinSize(children.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			content.setContent(children);
			createBinding();
			additionalTasks();
		} catch (final ConnectException e) {
			handle(e);
		}
	}

	/**
	 * initializes the user interface of the editor
	 *
	 * @throws ConnectException
	 */
	protected abstract Composite initUi(final Composite parent) throws ConnectException;

	private void createBinding() throws ConnectException {
		initBinding();
		final AggregateValidationStatus aggregateValidationStatus = BindingHelper
				.aggregateValidationStatus(bindingContext);
		bindingContext.bindValue(validationStatus, aggregateValidationStatus,
				new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER),
				new UpdateValueStrategy().setAfterConvertValidator(new IValidator() {

					@Override
					public IStatus validate(final Object value) {
						final Status status = (Status) value;
						if (OK.equals(status.getMessage())) {
							setMessage(EMPTY_STRING, null);
						} else {
							final String newMessage = status.getMessage();
							Image newImage = null;
							if (newMessage != null) {
								switch (status.getSeverity()) {
								case IMessageProvider.NONE:
									break;
								case IMessageProvider.INFORMATION:
									newImage = JFaceResources.getImage(Dialog.DLG_IMG_MESSAGE_INFO);
									break;
								case IMessageProvider.WARNING:
									newImage = JFaceResources.getImage(Dialog.DLG_IMG_MESSAGE_WARNING);
									break;
								case IMessageProvider.ERROR:
									newImage = JFaceResources.getImage(Dialog.DLG_IMG_MESSAGE_ERROR);
									break;
								case IStatus.ERROR:
									newImage = JFaceResources.getImage(Dialog.DLG_IMG_MESSAGE_ERROR);
									break;
								}
							}
							setMessage(newMessage, newImage);
						}
						return Status.OK_STATUS;
					}

				}));
	}

	/**
	 * sets validation message
	 *
	 * @param message
	 * @param image
	 */
	private void setMessage(final String message, final Image image) {
		lblValidationImage.setImage(image);
		lblValidationMessage.setText(message);
		lblValidationMessage.getParent().layout(true, true);
		// update dirty state when validation message changes
		updateDirtyState();
	}

	@Override
	public void updateDirtyState() {
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	public void setInput(final Input editorInput) {
		changeSupport.firePropertyChange(INPUT, input, input = editorInput);
		firePropertyChange(IEditorPart.PROP_INPUT);
		inputCache = cloneInput(input);

		// set the name of the input as title (if possible)
		final String partName = computePartName(input);
		if (partName != null && !partName.isEmpty()) {
			/*
			 * javadoc says that setting the empty string would be fine, but
			 * apparently it clears the title because it tries to set the part
			 * name of this abstract editor instead of the implementation
			 */
			setPartName(partName);
		}
	}

	public Input getInput() {
		return input;
	}

	/**
	 * override this method to provide a way to clone the input.<br>
	 * default implementation returns null
	 *
	 * @param input
	 * @return
	 */
	protected Input cloneInput(final Input input) {
		// done this way to avoid use of reflection
		return null;
	}

	/**
	 * computes the part name from given input.<br>
	 * default implementation returns null
	 *
	 * @param input
	 * @see EditorPart#setPartName
	 * @return part name
	 */
	protected String computePartName(final Input input) {
		return null;
	}

	/**
	 * initializes the databinding for the editor
	 *
	 * @throws ConnectException
	 *             it is save to throw this exception from this method
	 */
	protected abstract void initBinding() throws ConnectException;

	/**
	 * can be overridden by clients to perform additional tasks after UI and
	 * binding are done.<br>
	 * default implementation does nothing
	 *
	 * @throws ConnectException
	 *             it is save to throw this exception from this method
	 */
	protected void additionalTasks() throws ConnectException {
		// default implementation does nothing
	}

	/**
	 * handles an connect exception by displaying an info dialog to the user and
	 * closing the view afterwards
	 *
	 * @param e
	 */
	protected void handle(final ConnectException e) {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				closeView();
				final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				if (e instanceof InvalidSessionException) {
					MessageDialog.openError(shell, Messages.MESSAGE_ERROR,
							"Ihre Sitzung ist abgelaufen, sie müssen sich neu anmelden.");
					new LoginDialog(shell).open();
				} else {
					final StringBuilder msg = new StringBuilder(Messages.MESSAGE_ERROR_CONNECTION);
					if (getSite().getPage().isPartVisible(AbstractView.this)) {
						msg.append(Messages.NEW_LINE);
						msg.append(Messages.MESSAGE_VIEW_CLOSE);
					}
					MessageDialog.openError(shell, Messages.MESSAGE_ERROR, msg.toString());
				}
			}
		});
	}

	protected void closeView() {
		getViewSite().getPage().hideView(AbstractView.this);
		// IViewReference[] viewReferences =
		// getSite().getPage().hideView(getSite().ge);
		// VgetActivePart(closEditor(AbstractEditView.this, false);
	}

	/**
	 * checks if all bindings in {@link #bindingContext} are ok
	 *
	 * @return
	 * @see IStatus#isOK()
	 */
	protected boolean isSaveAble() {
		return ((IStatus) validationStatus.getValue()).isOK();
	}

	@Override
	public void doSave(final IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public boolean isDirty() {
		return input != null && !input.equals(inputCache);
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public boolean isSaveOnCloseNeeded() {
		return false;
	}

	@Override
	public void dispose() {
		toolkit.dispose();
		bindingContext.dispose();
		super.dispose();
	}

	/**
	 * @return the inputObservable
	 */
	public IObservableValue getInputObservable() {
		return inputObservable;
	}
}
