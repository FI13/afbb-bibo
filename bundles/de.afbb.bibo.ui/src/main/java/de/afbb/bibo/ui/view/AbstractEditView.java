package de.afbb.bibo.ui.view;

import java.net.ConnectException;

import org.eclipse.core.databinding.AggregateValidationStatus;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.EditorPart;

import de.afbb.bibo.databinding.BindingHelper;
import de.afbb.bibo.ui.BiboFormToolkit;
import de.afbb.bibo.ui.Messages;

abstract class AbstractEditView extends EditorPart {

	protected static final String EMPTY_STRING = "";//$NON-NLS-1$
	protected static final String DOT = ".";//$NON-NLS-1$
	private static final String OK = LocalizationUtils.safeLocalize("ok");//$NON-NLS-1$

	private Label lblValidationImage;
	private Label lblValidationMessage;
	private final IObservableValue validationStatus = new WritableValue(IStatus.OK, IStatus.class);
	protected final DataBindingContext bindingContext = new DataBindingContext();
	protected BiboFormToolkit toolkit = new BiboFormToolkit(Display.getCurrent());

	@Override
	public void createPartControl(final Composite parent) {
		try {
			final Composite outer = toolkit.createComposite(parent, SWT.NONE);
			GridLayout layoutOuter = new GridLayout(1, false);
			layoutOuter.marginHeight = layoutOuter.marginWidth = 0;
			outer.setLayout(layoutOuter);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(outer);

			Composite validationComposite = toolkit.createComposite(outer, SWT.NONE);
			validationComposite.setLayout(new GridLayout(2, false));
			lblValidationImage = toolkit.createLabel(validationComposite, EMPTY_STRING);
			lblValidationMessage = toolkit.createLabel(validationComposite, EMPTY_STRING);
			lblValidationMessage.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));

			initUi(outer);
			createBinding();
			additionalTasks();
		} catch (ConnectException e) {
			handle(e);
		}
	}

	/**
	 * initializes the UX of the editor
	 */
	protected abstract void initUi(final Composite parent);

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
							String newMessage = status.getMessage();
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
	private void setMessage(String message, Image image) {
		lblValidationImage.setImage(image);
		lblValidationMessage.setText(message);
		// update dirty state when validation message changes
		updateDirtyState();
	}

	protected void updateDirtyState() {
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	/**
	 * initializes the databinding for the editor
	 */
	protected abstract void initBinding() throws ConnectException;

	/**
	 * can be overridden by clients to perform additional tasks after UI and
	 * binding are done.<br>
	 * default implementation does nothing
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
				StringBuilder msg = new StringBuilder(Messages.MESSAGE_ERROR_CONNECTION);
				if (getSite().getPage().isPartVisible(AbstractEditView.this)) {
					msg.append(Messages.NEW_LINE);
					msg.append(Messages.MESSAGE_VIEW_CLOSE);
				}
				MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
						Messages.MESSAGE_ERROR, msg.toString());
				closeEditor();
			}
		});
	}

	protected void closeEditor() {
		getSite().getPage().closeEditor(AbstractEditView.this, false);
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
	public void init(final IEditorSite site, final IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
	}

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
