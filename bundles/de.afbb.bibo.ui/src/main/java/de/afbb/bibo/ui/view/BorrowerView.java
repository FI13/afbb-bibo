package de.afbb.bibo.ui.view;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import de.afbb.bibo.share.model.Borrower;

/**
 * this view greets the user and gives hints to use the software
 * 
 * @author dbecker
 */
public class BorrowerView extends EditorPart {

	public static final String ID = "de.afbb.bibo.ui.view.borrower";//$NON-NLS-1$

	private Label labelFirstname;
	private Label labelLastname;
	private Text firstname;
	private Text lastname;

	@Override
	public void createPartControl(final Composite parent) {
		final Composite top = new Composite(parent, SWT.NONE);
		final GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		top.setLayout(layout);

		final Composite nameInputs = new Composite(top, SWT.NONE);
//		final GridLayout nameInputsLayout = new GridLayout(2, false);
//		nameInputs.setLayout(nameInputsLayout);
		// new Button(nameInputs, SWT.PUSH).setText("B1");
		// new Button(nameInputs, SWT.PUSH).setText("Wide Button 2");
		// new Button(nameInputs, SWT.PUSH).setText("Button 3");
		// new Button(nameInputs, SWT.PUSH).setText("B4");
		// new Button(nameInputs, SWT.PUSH).setText("Button 5");

//		labelFirstname = new Label(top, 0);
//		labelFirstname.setText("Vorname:");
//		final FormData formDataLabelFirstname = new FormData();

//		labelLastname = new Label(top, 0);
//		labelLastname.setText("Nachname:");
//
		firstname = new Text(top, SWT.BORDER);
		firstname.setText("Vorname");
		final GridData gd = GridDataFactory.swtDefaults().hint(200, SWT.DEFAULT).create();
		firstname.setData(gd);

		lastname = new Text(top, SWT.BORDER);
		lastname.setText("Nachname");
//		lastname.setSize(200, 21);
	}

	@Override
	protected void setInput(final IEditorInput input) {
		if (input instanceof Borrower) {
			super.setInput(input);
		}
	}

	@Override
	public void setFocus() {
		firstname.setFocus();
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
