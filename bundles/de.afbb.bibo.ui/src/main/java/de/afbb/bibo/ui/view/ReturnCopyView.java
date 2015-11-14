package de.afbb.bibo.ui.view;

import java.net.ConnectException;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

public class ReturnCopyView extends AbstractEditView {

	public static final String ID = "de.afbb.bibo.ui.return.copy";//$NON-NLS-1$
	private static final String TYPE = "Typ";
	private static final String BARCODE = "Barcode";
	private static final String ISBN = "ISBN";//$NON-NLS-1$
	private static final String TITLE = "Titel";
	private static final String AUTHOR = "Autor";
	private static final String PUBLISHER = "Verlag";
	private static final String LANGUAGE = "Sprache";
	private static final String EDITION = "Auflage";

	private Text txtCondition;
	private Text txtBarcode;
	private Text txtIsbn;
	private Text txtEdition;
	private Text txtTitle;
	private Text txtAuthor;
	private Text txtLanguage;
	private Text txtPublisher;
	private Button btnToList;
	private Button btnToEdit;
	private Button btnGroup;
	private Button btnUngroup;
	private Button btnSave;
	private CCombo comboMediumType;

	@Override
	protected void initUi(Composite parent) {
		final Composite content = toolkit.createComposite(parent, SWT.NONE);
		content.setLayout(new GridLayout(2, false));

		Group copyGroup = toolkit.createGroup(content, "Exemplar");
		copyGroup.setLayout(new GridLayout(2, false));
		toolkit.createLabel(copyGroup, "Barcode");
		txtBarcode = toolkit.createText(copyGroup, EMPTY_STRING);
		txtBarcode.addTraverseListener(new TraverseListener() {

			@Override
			public void keyTraversed(TraverseEvent e) {
				// tab key
				if (e.keyCode == 9) {
					loadCopy();
				}

			}
		});
		GridDataFactory.swtDefaults().span(2, 1).applyTo(toolkit.createLabel(copyGroup, "Zustand"));
		txtCondition = toolkit.createText(copyGroup, EMPTY_STRING, SWT.MULTI);

		Group mediumGroup = toolkit.createGroup(content, "Informationen");
		mediumGroup.setLayout(new GridLayout(2, false));
		toolkit.createLabel(mediumGroup, TITLE);
		txtTitle = toolkit.createText(mediumGroup, EMPTY_STRING);
		toolkit.createLabel(mediumGroup, AUTHOR);
		txtAuthor = toolkit.createText(mediumGroup, EMPTY_STRING);
		toolkit.createLabel(mediumGroup, LANGUAGE);
		txtLanguage = toolkit.createText(mediumGroup, EMPTY_STRING);
		toolkit.createLabel(mediumGroup, PUBLISHER);
		txtPublisher = toolkit.createText(mediumGroup, EMPTY_STRING);
		toolkit.createLabel(mediumGroup, "Typ");
		comboMediumType = new CCombo(mediumGroup, SWT.BORDER);

		GridDataFactory.fillDefaults().grab(true, true).applyTo(content);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(copyGroup);
		GridDataFactory.fillDefaults().hint(200, SWT.DEFAULT).applyTo(mediumGroup);
		GridDataFactory.fillDefaults().span(2, 1).grab(true, true).applyTo(txtCondition);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtBarcode);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtTitle);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtAuthor);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtLanguage);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtPublisher);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(comboMediumType);

		txtTitle.setEnabled(false);
		txtAuthor.setEnabled(false);
		txtLanguage.setEnabled(false);
		txtPublisher.setEnabled(false);
		comboMediumType.setEnabled(false);
	}

	@Override
	protected void initBinding() throws ConnectException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFocus() {
		txtBarcode.setFocus();
	}

	private void loadCopy() {
		// TODO implement
	}

}
