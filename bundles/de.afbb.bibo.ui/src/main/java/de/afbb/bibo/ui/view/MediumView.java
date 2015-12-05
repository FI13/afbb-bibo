package de.afbb.bibo.ui.view;

import java.net.ConnectException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import de.afbb.bibo.share.model.Medium;

public class MediumView extends AbstractView<Medium> {

	public static final String ID = "de.afbb.bibo.ui.medium";//$NON-NLS-1$

	@Override
	protected Composite initUi(final Composite parent) throws ConnectException {
		final Composite content = toolkit.createComposite(parent, SWT.NONE);
		content.setLayout(new GridLayout(2, false));
		return content;
	}

	@Override
	protected void initBinding() throws ConnectException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
