package de.afbb.bibo.ui.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

/**
 * this view allows the registration of new exemplars
 * 
 * @author dbecker
 */
public class RegisterExemplarView extends AbstractEditView {

	public static final String ID = "de.afbb.bibo.ui.registerexemplar";//$NON-NLS-1$

	/**
	 * The text control that's displaying the content of the email message.
	 */
	private Text messageText;

	@Override
	public void initUi(final Composite parent) {
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
	protected void initBinding() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("TODO: implement");
	}

	@Override
	public void setFocus() {
		messageText.setFocus();
	}

	@Override
	public void init(final IEditorSite site, final IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
	}
}
