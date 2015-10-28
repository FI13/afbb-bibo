package de.afbb.bibo.ui.view;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

/**
 * this view greets the user and gives hints to use the software
 * 
 * @author dbecker
 */
public class WelcomeView extends EditorPart {

	public static final String ID = "de.afbb.bibo.ui.view.welcome";//$NON-NLS-1$

	/**
	 * The text control that's displaying the content of the email message.
	 */
	private Text messageText;

	@Override
	public void createPartControl(final Composite parent) {
		final Composite top = new Composite(parent, SWT.NONE);
		final GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		top.setLayout(layout);

		messageText = new Text(top, SWT.MULTI | SWT.WRAP);
		messageText.setText("TODO: hier Begrüßung des Nutzer einfügen und allgemeine Informationen zur Bedienung der Software anzeigen");
		messageText.setLayoutData(new GridData(GridData.FILL_BOTH));
	}

	@Override
	public void setFocus() {
		messageText.setFocus();
	}

	@Override
	public void doSave(final IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(final IEditorSite site, final IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
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
