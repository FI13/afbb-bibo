package de.afbb.bibo.ui.view;

import java.net.ConnectException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

import de.afbb.bibo.databinding.BindingHelper;
import de.afbb.bibo.share.ServiceLocator;
import de.afbb.bibo.share.SessionHolder;
import de.afbb.bibo.share.model.Borrower;
import de.afbb.bibo.share.model.Copy;
import de.afbb.bibo.share.model.IconType;
import de.afbb.bibo.share.model.Medium;
import de.afbb.bibo.share.model.MediumType;
import de.afbb.bibo.ui.BiboImageRegistry;
import de.afbb.bibo.ui.IconSize;
import de.afbb.bibo.ui.Messages;
import de.afbb.bibo.ui.form.CopyXviewerForm;
import de.afbb.bibo.ui.provider.MediumTypeLabelProvider;

public class LendCopyView extends AbstractEditView {

	public static final String ID = "de.afbb.bibo.ui.lend.copy";//$NON-NLS-1$
	private static final String LEND_COPY = "lend.copy";//$NON-NLS-1$

	private final Date now = new Date();
	private boolean printList = true;

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
	private Text txtBorrowDate;
	private Text txtLastBorrowDate;
	private Button btnToList;
	private Button btnToEdit;
	private Button btnSave;
	private Button btnDelete;
	private Button btnPrint;

	private CCombo comboMediumType;

	private CopyXviewerForm xViewer;

	private final HashMap<String, Copy> copyCache = new HashMap<>();
	private final Set<Copy> copies = new HashSet<Copy>();
	private Copy copyToModify = new Copy();

	/**
	 * listener that removes the selected item from the list and fills the input
	 * fields with its values
	 */
	Listener toEditListener = new Listener() {

		@Override
		public void handleEvent(final Event event) {
			Copy copy = xViewer.getLastElementFromSelectionPath();
			delete(copy);
			moveToEdit(copy);
		}
	};
	Listener deleteListener = new Listener() {

		@Override
		public void handleEvent(final Event event) {
			Copy copy = xViewer.getLastElementFromSelectionPath();
			delete(copy);
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

	Listener saveListener = new Listener() {

		@Override
		public void handleEvent(Event event) {
			final Job job = new Job("Rückgabe abschließen") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					try {
						ServiceLocator.getInstance().getCopyService().lendCopies(copies, printList);
					} catch (ConnectException e) {
						handle(e);
					}
					return Status.OK_STATUS;
				}
			};
			job.schedule();
			closeEditor();
		}
	};

	SelectionListener togglePrint = new SelectionListener() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			printList = btnPrint.getSelection();

		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			printList = btnPrint.getSelection();

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
				btnDelete.setEnabled(singleSelection);
			}
		}

		@Override
		public void widgetDefaultSelected(final SelectionEvent e) {
			// no double click event
		}
	};

	@Override
	protected Composite initUi(Composite parent) {
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
		toolkit.createLabel(statusGroup, Messages.DATE);
		txtBorrowDate = toolkit.createText(statusGroup, EMPTY_STRING);
		toolkit.createLabel(statusGroup, Messages.CURATOR);
		txtCurator = toolkit.createText(statusGroup, EMPTY_STRING);
		toolkit.createLabel(statusGroup, Messages.BORROWER);
		txtBorrower = toolkit.createText(statusGroup, EMPTY_STRING);
		GridDataFactory.swtDefaults().span(2, 1).applyTo(toolkit.createLabel(statusGroup, "Letzter Ausleihvorgang"));
		toolkit.createLabel(statusGroup, Messages.DATE);
		txtLastBorrowDate = toolkit.createText(statusGroup, EMPTY_STRING);
		toolkit.createLabel(statusGroup, Messages.CURATOR);
		txtLastCurator = toolkit.createText(statusGroup, EMPTY_STRING);
		toolkit.createLabel(statusGroup, Messages.BORROWER);
		txtLastBorrower = toolkit.createText(statusGroup, EMPTY_STRING);

		Group mediumGroup = toolkit.createGroup(content, "Allgemein");
		mediumGroup.setLayout(new GridLayout(2, false));
		toolkit.createLabel(mediumGroup, Messages.TITLE);
		txtTitle = toolkit.createText(mediumGroup, EMPTY_STRING);
		toolkit.createLabel(mediumGroup, Messages.AUTHOR);
		txtAuthor = toolkit.createText(mediumGroup, EMPTY_STRING);
		toolkit.createLabel(mediumGroup, Messages.LANGUAGE);
		txtLanguage = toolkit.createText(mediumGroup, EMPTY_STRING);
		toolkit.createLabel(mediumGroup, Messages.PUBLISHER);
		txtPublisher = toolkit.createText(mediumGroup, EMPTY_STRING);
		toolkit.createLabel(mediumGroup, Messages.ISBN);
		txtIsbn = toolkit.createText(mediumGroup, EMPTY_STRING);
		toolkit.createLabel(mediumGroup, Messages.EDITION);
		txtEdition = toolkit.createText(mediumGroup, EMPTY_STRING);
		toolkit.createLabel(mediumGroup, Messages.TYPE);
		comboMediumType = new CCombo(mediumGroup, SWT.BORDER);

		final Composite middle = toolkit.createComposite(content, SWT.NONE);
		middle.setLayout(new GridLayout(3, false));
		btnToList = toolkit.createButton(middle, "In Liste übernehmen", SWT.NONE);
		btnToEdit = toolkit.createButton(middle, "Details anzeigen", SWT.NONE);
		btnDelete = toolkit.createButton(middle, "Aus Liste entfernen", SWT.NONE);

		xViewer = new CopyXviewerForm(content, LEND_COPY);
		xViewer.getTree().addSelectionListener(xViewerSelectionListener);

		final Composite footer = toolkit.createComposite(content, SWT.NONE);
		footer.setLayout(new GridLayout(2, false));
		btnSave = toolkit.createButton(footer, "Ausleihe abschließen", SWT.NONE);
		btnPrint = toolkit.createButton(footer, "Liste drucken", SWT.CHECK);

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
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtBorrowDate);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtLastBorrowDate);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtLastBorrower);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(comboMediumType);
		GridDataFactory.fillDefaults().span(3, 1).align(SWT.CENTER, SWT.CENTER).grab(true, false).applyTo(middle);
		GridDataFactory.fillDefaults().span(3, 1).grab(true, true).applyTo(xViewer.getControl());
		GridDataFactory.fillDefaults().span(3, 1).align(SWT.CENTER, SWT.CENTER).grab(true, false).applyTo(footer);

		btnToList.setImage(BiboImageRegistry.getImage(IconType.ARROW_DOWN, IconSize.small));
		btnToEdit.setImage(BiboImageRegistry.getImage(IconType.ARROW_UP, IconSize.small));
		btnSave.setImage(BiboImageRegistry.getImage(IconType.SAVE, IconSize.small));
		btnDelete.setImage(BiboImageRegistry.getImage(IconType.MINUS, IconSize.small));

		btnToList.addListener(SWT.MouseDown, toListListener);
		btnToEdit.addListener(SWT.MouseDown, toEditListener);
		btnDelete.addListener(SWT.MouseDown, deleteListener);
		btnSave.addListener(SWT.MouseDown, saveListener);
		btnPrint.addSelectionListener(togglePrint);

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
		btnDelete.setEnabled(false);
		txtBorrowDate.setEnabled(false);
		txtLastBorrowDate.setEnabled(false);

		btnPrint.setSelection(printList);
		return content;
	}

	@Override
	protected void initBinding() throws ConnectException {
		;
		BindingHelper.bindStringToTextField(txtBarcode, copyToModify, Copy.class, Copy.FIELD_BARCODE, bindingContext,
				false);
		BindingHelper.bindStringToTextField(txtEdition, copyToModify, Copy.class, Copy.FIELD_EDITION, bindingContext,
				false);
		BindingHelper.bindStringToTextField(txtCondition, copyToModify, Copy.class, Copy.FIELD_CONDITION,
				bindingContext, false);
		BindingHelper.bindStringToTextField(txtTitle, copyToModify, Copy.class,
				Copy.FIELD_MEDIUM + DOT + Medium.FIELD_TITLE, bindingContext, false);
		BindingHelper.bindStringToTextField(txtAuthor, copyToModify, Copy.class,
				Copy.FIELD_MEDIUM + DOT + Medium.FIELD_AUTHOR, bindingContext, false);
		BindingHelper.bindStringToTextField(txtLanguage, copyToModify, Copy.class,
				Copy.FIELD_MEDIUM + DOT + Medium.FIELD_LANGUAGE, bindingContext, false);
		BindingHelper.bindStringToTextField(txtPublisher, copyToModify, Copy.class,
				Copy.FIELD_MEDIUM + DOT + Medium.FIELD_PUBLISHER, bindingContext, false);
		BindingHelper.bindStringToTextField(txtIsbn, copyToModify, Copy.class,
				Copy.FIELD_MEDIUM + DOT + Medium.FIELD_ISBN, bindingContext, false);

		BindingHelper.bindObjectToCCombo(comboMediumType, copyToModify, Copy.class,
				Copy.FIELD_MEDIUM + DOT + Medium.FIELD_TYPE, MediumType.class,
				ServiceLocator.getInstance().getTypService().list(), new MediumTypeLabelProvider(), bindingContext,
				false);

		BindingHelper.bindObjectToTextField(txtBorrowDate, copyToModify, Copy.class, Copy.FIELD_DATE_BORROW,
				bindingContext);
		BindingHelper.bindObjectToTextField(txtLastBorrowDate, copyToModify, Copy.class, Copy.FIELD_DATE_LAST_BORROW,
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

	private void moveToEdit(Copy copy) {
		setCopyToModify(copy);
		bindingContext.updateTargets();
	}

	private void delete(Copy copy) {
		copies.remove(copy);
		updateSaveButton();
		btnToEdit.setEnabled(false);
		btnDelete.setEnabled(false);
		xViewer.setInput(copies);
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
		copyToModify.getMedium().setAuthor(copy != null ? copy.getMedium().getAuthor() : null);
		copyToModify.setCondition(copy != null ? copy.getCondition() : null);
		copyToModify.setEdition(copy != null ? copy.getEdition() : null);
		copyToModify.setInventoryDate(copy != null ? copy.getInventoryDate() : null);
		copyToModify.getMedium().setIsbn(copy != null ? copy.getMedium().getIsbn() : null);
		copyToModify.getMedium().setLanguage(copy != null ? copy.getMedium().getLanguage() : null);
		copyToModify.setLastBorrowDate(copy != null ? copy.getLastBorrowDate() : null);
		copyToModify.setLastBorrower(copy != null ? copy.getLastBorrower() : null);
		copyToModify.setLastCurator(copy != null ? copy.getLastCurator() : null);
		copyToModify.getMedium().setPublisher(copy != null ? copy.getMedium().getPublisher() : null);
		copyToModify.getMedium().setTitle(copy != null ? copy.getMedium().getTitle() : null);
		copyToModify.getMedium().setType(copy != null ? copy.getMedium().getType() : null);

		copyToModify.setBorrower(copy != null ? (Borrower) getEditorInput() : null);
		copyToModify.setBorrowDate(copy != null ? now : null);
		copyToModify.setCurator(copy != null ? SessionHolder.getInstance().getCurator() : null);

		btnToList.setEnabled(copy != null);
		bindingContext.updateTargets();
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		if (input instanceof Borrower) {
			setPartName("Ausleihe an " + ((Borrower) input).getName());
		}
	}

}
