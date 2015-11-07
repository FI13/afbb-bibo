package de.afbb.bibo.ui.view;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

import de.afbb.bibo.databinding.BindingHelper;
import de.afbb.bibo.share.model.Borrower;

/**
 * this view adds or edits a person who can borrow books
 * 
 * @author philippwiddra
 */
public class BorrowerView extends AbstractEditView {

	public static final String ID = "de.afbb.bibo.ui.borrower";//$NON-NLS-1$

	private Borrower input;
	private Borrower inputCache;

	protected final DataBindingContext bindingContext = new DataBindingContext();

	private Label labelFirstname;
	private Label labelLastname;
	private Label labelEMail;
	private Label labelTel;
	private Label labelInfo;
	private Text textFirstname;
	private Text textLastname;
	private Text textEMail;
	private Text textTel;
	private Text textInfo;
//	private Button buttonSave;
//	private Button buttonCancel;

	@Override
	protected void setInput(final IEditorInput input) {
		if (input instanceof Borrower) {
			this.input = (Borrower) input;
			try {
				inputCache = (Borrower)((Borrower) this.input).clone();
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			super.setInput(input);
		}
	}

	@Override
	public void setFocus() {
		textFirstname.setFocus();
	}

	@Override
	public void doSave(final IProgressMonitor monitor) {

	}

	@Override
	public void init(final IEditorSite site, final IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
	}

	@Override
	public boolean isDirty() {
		return !input.equals(inputCache);
	}

	@Override
	protected void initUi(final Composite parent) {
		final Composite top = new Composite(parent, SWT.NONE);
		final GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 10;
		layout.marginWidth = 10;
		top.setLayout(layout);

		final int textWidth = 250;

		// Label Vorname
		labelFirstname = new Label(top, SWT.NONE);
		labelFirstname.setText("Vorname:");

		// Textbox Vorname
		textFirstname = new Text(top, SWT.BORDER);
		GridDataFactory.swtDefaults().hint(textWidth, SWT.DEFAULT).applyTo(textFirstname);

		// Label Nachname
		labelLastname = new Label(top, SWT.NONE);
		labelLastname.setText("Nachname:");

		// Textbox Nachname
		textLastname = new Text(top, SWT.BORDER);
		GridDataFactory.swtDefaults().hint(textWidth, SWT.DEFAULT).applyTo(textLastname);

		// Label E-Mail
		labelEMail = new Label(top, SWT.NONE);
		labelEMail.setText("E-Mail-Adresse:");
		GridDataFactory.swtDefaults().applyTo(labelEMail);

		// Textbox E-Mail
		textEMail = new Text(top, SWT.BORDER);
		GridDataFactory.swtDefaults().hint(textWidth, SWT.DEFAULT).applyTo(textEMail);

		// Label Telefonnummer
		labelTel = new Label(top, SWT.NONE);
		labelTel.setText("Telefonnummer:");

		// Textbox Telefonnummer
		textTel = new Text(top, SWT.BORDER);
		GridDataFactory.swtDefaults().hint(textWidth, SWT.DEFAULT).applyTo(textTel);

		// Label Informationen
		labelInfo = new Label(top, SWT.NONE);
		labelInfo.setText("Informationen:");

		// Textbox Informationen
		textInfo = new Text(top, SWT.BORDER);
		GridDataFactory.swtDefaults().hint(textWidth, SWT.DEFAULT).applyTo(textInfo);

//		// Button Speichern
//		buttonSave = new Button(top, SWT.NONE);
//		buttonSave.setText("Speichern");
//		GridDataFactory.swtDefaults().hint(150, SWT.DEFAULT).applyTo(buttonSave);
//
//		// Button Verwerfen
//		buttonCancel = new Button(top, SWT.NONE);
//		buttonCancel.setText("Verwerfen");
//		GridDataFactory.swtDefaults().hint(150, SWT.DEFAULT).applyTo(buttonCancel);

	}

	@Override
	protected void initBinding() {
		// Databinding
		BindingHelper.bindStringToTextField(textFirstname, input, Borrower.class, Borrower.FIELD_FIRSTNAME, bindingContext, true);
		BindingHelper.bindStringToTextField(textLastname, input, Borrower.class, Borrower.FIELD_SURNAME, bindingContext, true);
		BindingHelper.bindStringToTextField(textEMail, input, Borrower.class, Borrower.FIELD_EMAIL, bindingContext, false);
		BindingHelper.bindStringToTextField(textTel, input, Borrower.class, Borrower.FIELD_PHONENUMER, bindingContext, false);
		BindingHelper.bindStringToTextField(textInfo, input, Borrower.class, Borrower.FIELD_INFO, bindingContext, false);
	}
}
