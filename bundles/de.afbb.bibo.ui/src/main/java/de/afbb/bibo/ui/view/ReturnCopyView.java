package de.afbb.bibo.ui.view;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.nebula.widgets.xviewer.XViewer;
import org.eclipse.nebula.widgets.xviewer.XViewerColumn;
import org.eclipse.nebula.widgets.xviewer.XViewerFactory;
import org.eclipse.nebula.widgets.xviewer.XViewerColumn.SortDataType;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import de.afbb.bibo.databinding.BindingHelper;
import de.afbb.bibo.share.ServiceLocator;
import de.afbb.bibo.share.model.Copy;
import de.afbb.bibo.share.model.IconType;
import de.afbb.bibo.share.model.Medium;
import de.afbb.bibo.share.model.MediumType;
import de.afbb.bibo.ui.BiboImageRegistry;
import de.afbb.bibo.ui.IconSize;
import de.afbb.bibo.ui.provider.BiboXViewerFactory;
import de.afbb.bibo.ui.provider.CopyLabelProvider;
import de.afbb.bibo.ui.provider.CopyTreeContentProvider;
import de.afbb.bibo.ui.provider.MediumTypeLabelProvider;

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
	private static final String DATE = "Datum";
	private static final String CURATOR = "Bediener";
	private static final String BORROWER = "Ausleiher";

	private Text txtCondition;
	private Text txtBarcode;
	private Text txtIsbn;
	private Text txtEdition;
	private Text txtTitle;
	private Text txtAuthor;
	private Text txtLanguage;
	private Text txtPublisher;
	private Text txtBorrower;
	private Text txtLastBorrower;
	private Text txtCurator;
	private Text txtLastCurator;
	private Button btnToList;
	private Button btnToEdit;
	private Button btnSave;
	private CCombo comboMediumType;
	private DateTime timeBorrowDate;
	private DateTime timeLastBorrowDate;

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

	private final HashMap<String, Copy> copyCache = new HashMap<>();
	private final Set<Copy> copies = new HashSet<Copy>();
	private Copy copyToModify = new Copy();

	@Override
	protected void initUi(Composite parent) {
		final Composite content = toolkit.createComposite(parent, SWT.NONE);
		content.setLayout(new GridLayout(3, false));

		Group copyGroup = toolkit.createGroup(content, "Exemplar");
		copyGroup.setLayout(new GridLayout(2, false));
		toolkit.createLabel(copyGroup, "Barcode");
		txtBarcode = toolkit.createText(copyGroup, EMPTY_STRING);
		txtBarcode.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				loadCopyFromDatabase(txtBarcode.getText());
			}

			@Override
			public void focusGained(FocusEvent e) {
			}
		});
		GridDataFactory.swtDefaults().span(2, 1).applyTo(toolkit.createLabel(copyGroup, "Zustand"));
		txtCondition = toolkit.createText(copyGroup, EMPTY_STRING, SWT.MULTI);

		Group statusGroup = toolkit.createGroup(content, "Informationen");
		statusGroup.setLayout(new GridLayout(2, false));
		GridDataFactory.swtDefaults().span(2, 1).applyTo(toolkit.createLabel(statusGroup, "Aktueller Ausleihvorgang"));
		toolkit.createLabel(statusGroup, DATE);
		timeBorrowDate = toolkit.createDateTime(statusGroup);
		toolkit.createLabel(statusGroup, CURATOR);
		txtCurator = toolkit.createText(statusGroup, EMPTY_STRING);
		toolkit.createLabel(statusGroup, BORROWER);
		txtBorrower = toolkit.createText(statusGroup, EMPTY_STRING);
		GridDataFactory.swtDefaults().span(2, 1).applyTo(toolkit.createLabel(statusGroup, "Letzter Ausleihvorgang"));
		toolkit.createLabel(statusGroup, DATE);
		timeLastBorrowDate = toolkit.createDateTime(statusGroup);
		toolkit.createLabel(statusGroup, CURATOR);
		txtLastCurator = toolkit.createText(statusGroup, EMPTY_STRING);
		toolkit.createLabel(statusGroup, BORROWER);
		txtLastBorrower = toolkit.createText(statusGroup, EMPTY_STRING);

		Group mediumGroup = toolkit.createGroup(content, "Allgemein");
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
		xViewer.getTree().addSelectionListener(xViewerSelectionListener);
		xViewer.getMenuManager().dispose();

		final Composite footer = toolkit.createComposite(content, SWT.NONE);
		footer.setLayout(new GridLayout(1, false));
		btnSave = toolkit.createButton(footer, "Rückgabe abschließen", SWT.NONE);

		GridDataFactory.fillDefaults().grab(true, true).applyTo(content);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(copyGroup);
		GridDataFactory.fillDefaults().applyTo(statusGroup);
		GridDataFactory.fillDefaults().hint(200, SWT.DEFAULT).applyTo(mediumGroup);
		GridDataFactory.fillDefaults().span(2, 1).grab(true, true).applyTo(txtCondition);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtBarcode);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtTitle);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtAuthor);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtLanguage);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtPublisher);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtIsbn);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtEdition);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtCurator);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtBorrower);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtLastCurator);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtLastBorrower);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(comboMediumType);
		GridDataFactory.fillDefaults().span(3, 1).align(SWT.CENTER, SWT.CENTER).grab(true, false).applyTo(middle);
		GridDataFactory.fillDefaults().span(3, 1).grab(true, true).applyTo(xViewer.getControl());
		GridDataFactory.fillDefaults().span(3, 1).align(SWT.CENTER, SWT.CENTER).grab(true, false).applyTo(footer);

		btnToList.setImage(BiboImageRegistry.getImage(IconType.ARROW_DOWN, IconSize.small));
		btnToEdit.setImage(BiboImageRegistry.getImage(IconType.ARROW_UP, IconSize.small));
		btnSave.setImage(BiboImageRegistry.getImage(IconType.SAVE, IconSize.small));

		btnToList.addListener(SWT.MouseDown, toListListener);
		btnToEdit.addListener(SWT.MouseDown, toEditListener);
		btnSave.addListener(SWT.MouseDown, new Listener() {

			@Override
			public void handleEvent(Event event) {
				final Job job = new Job("Rückgabe abschließen") {

					@Override
					protected IStatus run(IProgressMonitor monitor) {
						try {
							ServiceLocator.getInstance().getCopyService().returnCopy(copies);
						} catch (ConnectException e) {
							handle(e);
						}
						return Status.OK_STATUS;
					}

				};
				job.schedule();
				closeEditor();
			}
		});

		txtCondition.setEnabled(false);
		txtTitle.setEnabled(false);
		txtAuthor.setEnabled(false);
		txtLanguage.setEnabled(false);
		txtPublisher.setEnabled(false);
		txtIsbn.setEnabled(false);
		txtEdition.setEnabled(false);
		txtCurator.setEnabled(false);
		txtBorrower.setEnabled(false);
		txtLastCurator.setEnabled(false);
		txtLastBorrower.setEnabled(false);
		comboMediumType.setEnabled(false);
		btnToList.setEnabled(false);
		btnToEdit.setEnabled(false);
		btnSave.setEnabled(false);
		timeBorrowDate.setEnabled(false);
		timeLastBorrowDate.setEnabled(false);
	}

	@Override
	protected void initBinding() throws ConnectException {
		BindingHelper.bindStringToTextField(txtBarcode, copyToModify, Copy.class, Copy.FIELD_BARCODE, bindingContext,
				false);
		BindingHelper.bindStringToTextField(txtIsbn, copyToModify, Copy.class, Copy.FIELD_ISBN, bindingContext, false);
		BindingHelper.bindStringToTextField(txtEdition, copyToModify, Copy.class, Copy.FIELD_EDITION, bindingContext,
				false);
		BindingHelper.bindStringToTextField(txtTitle, copyToModify, Copy.class, Copy.FIELD_TITLE, bindingContext,
				false);
		BindingHelper.bindStringToTextField(txtAuthor, copyToModify, Copy.class, Copy.FIELD_AUTHOR, bindingContext,
				false);
		BindingHelper.bindStringToTextField(txtLanguage, copyToModify, Copy.class, Copy.FIELD_LANGUAGE, bindingContext,
				false);
		BindingHelper.bindStringToTextField(txtPublisher, copyToModify, Copy.class, Copy.FIELD_PUBLISHER,
				bindingContext, false);
		BindingHelper.bindStringToTextField(txtCondition, copyToModify, Copy.class, Copy.FIELD_CONDITION,
				bindingContext, false);

		BindingHelper.bindObjectToCCombo(comboMediumType, copyToModify, Copy.class, Medium.FIELD_TYPE, MediumType.class,
				ServiceLocator.getInstance().getTypService().list(), new MediumTypeLabelProvider(), bindingContext,
				false);

		BindingHelper.bindDate(timeBorrowDate, copyToModify, Copy.class, Copy.FIELD_DATE_BORROW, bindingContext);
		BindingHelper.bindDate(timeLastBorrowDate, copyToModify, Copy.class, Copy.FIELD_DATE_LAST_BORROW,
				bindingContext);

		BindingHelper.bindObjectToTextField(txtCurator, copyToModify, Copy.class, Copy.FIELD_CURATOR, bindingContext);
		BindingHelper.bindObjectToTextField(txtLastCurator, copyToModify, Copy.class, Copy.FIELD_LAST_CURATOR,
				bindingContext);
		BindingHelper.bindObjectToTextField(txtBorrower, copyToModify, Copy.class, Copy.FIELD_BORROWER, bindingContext);
		BindingHelper.bindObjectToTextField(txtLastBorrower, copyToModify, Copy.class, Copy.FIELD_LAST_BORROWER,
				bindingContext);
	}

	@Override
	public void setFocus() {
		txtBarcode.setFocus();
	}

	private void updateSaveButton() {
		btnSave.setEnabled(
				(copyToModify.getBarcode() == null || copyToModify.getBarcode().isEmpty()) && !copies.isEmpty());
	}

	/**
	 * listener that removes the selected item from the list and fills the input
	 * fields with its values
	 */
	Listener toEditListener = new Listener() {

		@Override
		public void handleEvent(final Event event) {
			final TreePath[] paths = ((TreeSelection) xViewer.getSelection()).getPaths();
			if (paths.length > 0) {
				final Copy copy = (Copy) paths[0].getLastSegment();
				moveToEdit(copy);
			}
		}
	};

	/**
	 * listener that adds a copy to the list and clears the input fields
	 * afterwards
	 */
	Listener toListListener = new Listener() {

		@Override
		public void handleEvent(final Event event) {
			final Copy clone = (Copy) copyToModify.clone();
			copies.add(clone);
			setCopyToModify(null);
			updateSaveButton();
			xViewer.setInput(copies);
			txtBarcode.setFocus();
		}
	};

	/**
	 * listener that reacts when the selection changes and enables & disables
	 * control buttons
	 */
	SelectionListener xViewerSelectionListener = new SelectionListener() {

		@Override
		public void widgetSelected(final SelectionEvent e) {
			final ISelection selection = xViewer.getSelection();
			if (selection instanceof TreeSelection) {
				final boolean singleSelection = ((TreeSelection) selection).size() == 1;
				btnToEdit.setEnabled(singleSelection);
			}
		}

		@Override
		public void widgetDefaultSelected(final SelectionEvent e) {
			// no double click event
		}
	};

	private void moveToEdit(Copy copy) {
		copies.remove(copy);
		setCopyToModify(copy);
		updateSaveButton();
		btnToEdit.setEnabled(false);
		xViewer.setInput(copies);
		bindingContext.updateTargets();
	}

	private void loadCopyFromDatabase(String barcode) {
		// already loaded -> get from table
		if (copyCache.containsKey(barcode)) {
			moveToEdit(copyCache.get(barcode));
		} else {
			// normal fetch from database
			Copy copy = null;
			try {
				copy = ServiceLocator.getInstance().getCopyService().get(barcode);
			} catch (ConnectException e) {
				handle(e);
			}
			setCopyToModify(copy);
			copyCache.put(barcode, copy);
		}
	}

	/**
	 * fills the controls with the appropriate informations
	 * 
	 * @param copy
	 */
	private void setCopyToModify(Copy copy) {
		copyToModify.setBarcode(copy != null ? copy.getBarcode() : null);
		copyToModify.setAuthor(copy != null ? copy.getAuthor() : null);
		copyToModify.setBorrowDate(copy != null ? copy.getBorrowDate() : null);
		copyToModify.setBorrower(copy != null ? copy.getBorrower() : null);
		copyToModify.setCondition(copy != null ? copy.getCondition() : null);
		copyToModify.setCurator(copy != null ? copy.getCurator() : null);
		copyToModify.setEdition(copy != null ? copy.getEdition() : null);
		copyToModify.setInventoryDate(copy != null ? copy.getInventoryDate() : null);
		copyToModify.setIsbn(copy != null ? copy.getIsbn() : null);
		copyToModify.setLanguage(copy != null ? copy.getLanguage() : null);
		copyToModify.setLastBorrowDate(copy != null ? copy.getLastBorrowDate() : null);
		copyToModify.setLastBorrower(copy != null ? copy.getLastBorrower() : null);
		copyToModify.setLastCurator(copy != null ? copy.getLastCurator() : null);
		copyToModify.setPublisher(copy != null ? copy.getPublisher() : null);
		copyToModify.setTitle(copy != null ? copy.getTitle() : null);
		copyToModify.setType(copy != null ? copy.getType() : null);
		txtCondition.setEnabled(copy != null);
		btnToList.setEnabled(copy != null);
		bindingContext.updateTargets();
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
