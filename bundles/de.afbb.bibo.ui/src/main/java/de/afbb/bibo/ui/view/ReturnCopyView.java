package de.afbb.bibo.ui.view;

import java.net.ConnectException;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

public class ReturnCopyView extends AbstractEditView {
	
	public static final String ID = "de.afbb.bibo.ui.return.copy";//$NON-NLS-1$

	@Override
	protected void initUi(Composite parent) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initBinding() throws ConnectException {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
