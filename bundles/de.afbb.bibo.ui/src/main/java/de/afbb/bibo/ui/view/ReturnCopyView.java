package de.afbb.bibo.ui.view;

import java.net.ConnectException;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.nebula.widgets.xviewer.XViewer;
import org.eclipse.nebula.widgets.xviewer.XViewerColumn;
import org.eclipse.nebula.widgets.xviewer.XViewerFactory;
import org.eclipse.nebula.widgets.xviewer.XViewerColumn.SortDataType;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import de.afbb.bibo.share.model.IconType;
import de.afbb.bibo.ui.BiboImageRegistry;
import de.afbb.bibo.ui.IconSize;
import de.afbb.bibo.ui.provider.BiboXViewerFactory;
import de.afbb.bibo.ui.provider.CopyLabelProvider;
import de.afbb.bibo.ui.provider.CopyTreeContentProvider;

public class ReturnCopyView extends AbstractEditView {

	public static final String ID = "de.afbb.bibo.ui.return.copy";//$NON-NLS-1$
	private static final String RETURN_COPY = "return.copy";//$NON-NLS-1$
	private static final String DOT = ".";//$NON-NLS-1$
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
	private Button btnSave;
	private CCombo comboMediumType;

	private XViewer xViewer;
	private final XViewerFactory factory = new BiboXViewerFactory(RETURN_COPY);
	private XViewerColumn columnType;
	private XViewerColumn columnBarcode;
	private XViewerColumn columnIsbn;
	private XViewerColumn columnTitle;
	private XViewerColumn columnAuthor;
	private XViewerColumn columnPublisher;
	private XViewerColumn columnLanguage;
	private XViewerColumn columnEdition;

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
		toolkit.createLabel(mediumGroup, ISBN);
		txtIsbn = toolkit.createText(mediumGroup, EMPTY_STRING);
		toolkit.createLabel(mediumGroup, EDITION);
		txtEdition = toolkit.createText(mediumGroup, EMPTY_STRING);
		toolkit.createLabel(mediumGroup, TYPE);
		comboMediumType = new CCombo(mediumGroup, SWT.BORDER);

		final Composite middle = toolkit.createComposite(content, SWT.NONE);
		middle.setLayout(new GridLayout(2, false));
		btnToList = toolkit.createButton(middle, "In Liste übernehmen", SWT.NONE);
		btnToEdit = toolkit.createButton(middle, "In Beareitung übernehmen", SWT.NONE);
		
		initTableColumns();
		xViewer = new XViewer(content, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION, factory);
		xViewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		final CopyTreeContentProvider contentProvider = new CopyTreeContentProvider();
		xViewer.setContentProvider(contentProvider);
		xViewer.setLabelProvider(new CopyLabelProvider(xViewer, contentProvider));
//		xViewer.getTree().addSelectionListener(xViewerSelectionListener);
		xViewer.getMenuManager().dispose();

		final Composite footer = toolkit.createComposite(content, SWT.NONE);
		footer.setLayout(new GridLayout(1, false));
		btnSave = toolkit.createButton(footer, "Rückgabe abschließen", SWT.NONE);

		GridDataFactory.fillDefaults().grab(true, true).applyTo(content);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(copyGroup);
		GridDataFactory.fillDefaults().hint(200, SWT.DEFAULT).applyTo(mediumGroup);
		GridDataFactory.fillDefaults().span(2, 1).grab(true, true).applyTo(txtCondition);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtBarcode);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtTitle);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtAuthor);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtLanguage);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtPublisher);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtIsbn);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtEdition);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(comboMediumType);
		GridDataFactory.fillDefaults().span(2, 1).align(SWT.CENTER, SWT.CENTER).grab(true, false).applyTo(middle);
		GridDataFactory.fillDefaults().span(2, 1).grab(true, true).applyTo(xViewer.getControl());
		GridDataFactory.fillDefaults().span(2, 1).align(SWT.CENTER, SWT.CENTER).grab(true, false).applyTo(footer);

		btnToList.setImage(BiboImageRegistry.getImage(IconType.ARROW_DOWN, IconSize.small));
		btnToEdit.setImage(BiboImageRegistry.getImage(IconType.ARROW_UP, IconSize.small));
		btnSave.setImage(BiboImageRegistry.getImage(IconType.SAVE, IconSize.small));

		txtTitle.setEnabled(false);
		txtAuthor.setEnabled(false);
		txtLanguage.setEnabled(false);
		txtPublisher.setEnabled(false);
		txtIsbn.setEnabled(false);
		txtEdition.setEnabled(false);
		comboMediumType.setEnabled(false);
		btnToList.setEnabled(false);
		btnToEdit.setEnabled(false);
		btnSave.setEnabled(false);
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

	private void initTableColumns() {
		columnType = new XViewerColumn(RETURN_COPY + DOT + TYPE, TYPE, 90, SWT.LEFT, true, SortDataType.String, false,
				"Typ des Mediums");
		columnBarcode = new XViewerColumn(RETURN_COPY + DOT + BARCODE, BARCODE, 80, SWT.LEFT, true,
				SortDataType.Integer, false, "Barcode des Mediums");
		columnIsbn = new XViewerColumn(RETURN_COPY + DOT + ISBN, ISBN, 80, SWT.LEFT, true, SortDataType.Integer, false,
				"ISBN des Mediums");
		columnTitle = new XViewerColumn(RETURN_COPY + DOT + TITLE, TITLE, 150, SWT.LEFT, true, SortDataType.String,
				false, TITLE);
		columnAuthor = new XViewerColumn(RETURN_COPY + DOT + AUTHOR, AUTHOR, 150, SWT.LEFT, true, SortDataType.String,
				false, AUTHOR);
		columnPublisher = new XViewerColumn(RETURN_COPY + DOT + PUBLISHER, PUBLISHER, 150, SWT.LEFT, true,
				SortDataType.String, false, PUBLISHER);
		columnLanguage = new XViewerColumn(RETURN_COPY + DOT + LANGUAGE, LANGUAGE, 150, SWT.LEFT, true,
				SortDataType.String, false, LANGUAGE);
		columnEdition = new XViewerColumn(RETURN_COPY + DOT + EDITION, EDITION, 150, SWT.LEFT, true,
				SortDataType.String, false, EDITION);
		factory.registerColumns(columnType, columnBarcode, columnIsbn, columnTitle, columnAuthor, columnPublisher,
				columnLanguage, columnEdition);
	}

}
