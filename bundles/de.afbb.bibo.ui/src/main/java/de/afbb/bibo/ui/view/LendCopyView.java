package de.afbb.bibo.ui.view;

import java.net.ConnectException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
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

import de.afbb.bibo.databinding.BindingHelper;
import de.afbb.bibo.servletclient.ServiceLocator;
import de.afbb.bibo.share.SessionHolder;
import de.afbb.bibo.share.model.Borrower;
import de.afbb.bibo.share.model.Copy;
import de.afbb.bibo.share.model.IconType;
import de.afbb.bibo.share.model.Medium;
import de.afbb.bibo.ui.BiboImageRegistry;
import de.afbb.bibo.ui.IconSize;
import de.afbb.bibo.ui.form.CopyMovementForm;
import de.afbb.bibo.ui.form.CopyXviewerForm;
import de.afbb.bibo.ui.form.MediumInformationForm;

public class LendCopyView extends AbstractView<Borrower> {

	public static final String ID = "de.afbb.bibo.ui.lend.copy";//$NON-NLS-1$
	private static final String LEND_COPY = "lend.copy";//$NON-NLS-1$
	private static final String INPUT_COPY = "copyToModify";//$NON-NLS-1$

	private final Date now = new Date();
	private boolean printList = true;

	private Text txtCondition;
	private Text txtBarcode;
	private Button btnToList;
	private Button btnToEdit;
	private Button btnSave;
	private Button btnDelete;
	private Button btnPrint;

	private CopyXviewerForm xViewer;
	private CopyMovementForm movementForm;
	private MediumInformationForm informationForm;

	private final HashMap<String, Copy> copyCache = new HashMap<>();
	private final Set<Copy> copies = new HashSet<Copy>();
	private Copy copyToModify = new Copy();
	private final IObservableValue copyToModifyObservable = BeansObservables.observeValue(this, INPUT_COPY);

	/**
	 * listener that removes the selected item from the list and fills the input
	 * fields with its values
	 */
	Listener toEditListener = new Listener() {

		@Override
		public void handleEvent(final Event event) {
			final Copy copy = xViewer.getLastElementFromSelectionPath();
			delete(copy);
			moveToEdit(copy);
		}
	};
	Listener deleteListener = new Listener() {

		@Override
		public void handleEvent(final Event event) {
			final Copy copy = xViewer.getLastElementFromSelectionPath();
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
		public void handleEvent(final Event event) {
			doSave(null);
		}
	};

	SelectionListener togglePrint = new SelectionListener() {

		@Override
		public void widgetSelected(final SelectionEvent e) {
			printList = btnPrint.getSelection();

		}

		@Override
		public void widgetDefaultSelected(final SelectionEvent e) {
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
	public void doSave(final IProgressMonitor monitor) {

		final Job job = new Job("Medien ausleihen") {

			@Override
			protected IStatus run(final IProgressMonitor monitor) {
				try {
					ServiceLocator.getInstance().getCopyService().lendCopies(copies, printList);
				} catch (final ConnectException e) {
					handle(e);
				}
				return Status.OK_STATUS;
			}
		};
		job.schedule();
		closeView();
	};

	@Override
	public void setInput(final Borrower editorInput) {
		super.setInput(editorInput);
		setCopyToModify(null);
		copyCache.clear();
		copies.clear();
		xViewer.setInput(copies);
	}

	@Override
	protected Composite initUi(final Composite parent) throws ConnectException {
		final Composite content = toolkit.createComposite(parent, SWT.NONE);
		content.setLayout(new GridLayout(3, false));

		final Group copyGroup = toolkit.createGroup(content, "Exemplar");
		copyGroup.setLayout(new GridLayout(2, false));
		toolkit.createLabel(copyGroup, "Barcode");
		txtBarcode = toolkit.createText(copyGroup, EMPTY_STRING);
		txtBarcode.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(final FocusEvent e) {
				loadCopyFromDatabase(txtBarcode.getText());
			}

			@Override
			public void focusGained(final FocusEvent e) {
			}
		});
		GridDataFactory.swtDefaults().span(2, 1).applyTo(toolkit.createLabel(copyGroup, "Zustand"));
		txtCondition = toolkit.createText(copyGroup, EMPTY_STRING, SWT.MULTI);

		final Group statusGroup = toolkit.createGroup(content, "Informationen");
		movementForm = new CopyMovementForm(statusGroup, copyToModify, bindingContext, toolkit);

		final Group mediumGroup = toolkit.createGroup(content, "Allgemein");
		informationForm = new MediumInformationForm(mediumGroup, new Medium(), bindingContext, toolkit);

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
		GridDataFactory.fillDefaults().applyTo(mediumGroup);
		GridDataFactory.fillDefaults().span(2, 1).grab(true, true).applyTo(txtCondition);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtBarcode);
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

		btnToList.setEnabled(false);
		btnToEdit.setEnabled(false);
		btnSave.setEnabled(false);
		btnDelete.setEnabled(false);

		btnPrint.setSelection(printList);
		return content;
	}

	@Override
	protected void initBinding() throws ConnectException {
		BindingHelper.bindStringToTextField(txtBarcode, copyToModifyObservable, Copy.FIELD_BARCODE, bindingContext,
				false);
		BindingHelper.bindStringToTextField(txtCondition, copyToModifyObservable, Copy.FIELD_CONDITION, bindingContext,
				false);

		// one-way binding to update the pseudo-input in sub-forms
		bindingContext.bindValue(BeansObservables.observeValue(movementForm, INPUT), copyToModifyObservable,
				new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER), null);
		bindingContext.bindValue(BeansObservables.observeValue(informationForm, INPUT),
				BeansObservables.observeDetailValue(copyToModifyObservable, Copy.FIELD_MEDIUM, Medium.class),
				new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER), null);
	}

	@Override
	public void setFocus() {
		txtBarcode.setFocus();
	}

	private void updateSaveButton() {
		btnSave.setEnabled(isDirty());
	}

	private void moveToEdit(final Copy copy) {
		setCopyToModify(copy);
		bindingContext.updateTargets();
	}

	private void delete(final Copy copy) {
		copies.remove(copy);
		updateSaveButton();
		btnToEdit.setEnabled(false);
		btnDelete.setEnabled(false);
		xViewer.setInput(copies);
	}

	private void loadCopyFromDatabase(final String barcode) {
		// already loaded -> get from table
		if (copyCache.containsKey(barcode)) {
			moveToEdit(copyCache.get(barcode));
		} else {
			// normal fetch from database
			Copy copy = null;
			try {
				copy = ServiceLocator.getInstance().getCopyService().get(barcode);
			} catch (final ConnectException e) {
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
	private void setCopyToModify(final Copy copy) {
		if (copy != null) {
			copy.setBorrower(input);
			copy.setBorrowDate(now);
			copy.setCurator(SessionHolder.getInstance().getCurator());
		}
		changeSupport.firePropertyChange(INPUT_COPY, copyToModify, copyToModify = copy != null ? copy : new Copy());
		btnToList.setEnabled(copy != null);
		bindingContext.updateTargets();
	}

	/**
	 * @return the copyToModify
	 */
	public Copy getCopyToModify() {
		return copyToModify;
	}

	@Override
	protected String computePartName(final Borrower input) {
		return input != null ? "Ausleihe an " + input.getName() : null;
	}

	@Override
	public boolean isDirty() {
		return (copyToModify == null || copyToModify.getBarcode() == null || copyToModify.getBarcode().isEmpty())
				&& !copies.isEmpty();
	}

}
