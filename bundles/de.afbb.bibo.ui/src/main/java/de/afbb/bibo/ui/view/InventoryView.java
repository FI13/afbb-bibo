package de.afbb.bibo.ui.view;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreePath;
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
import de.afbb.bibo.share.model.Copy;
import de.afbb.bibo.share.model.IconType;
import de.afbb.bibo.share.model.Medium;
import de.afbb.bibo.ui.BiboImageRegistry;
import de.afbb.bibo.ui.IconSize;
import de.afbb.bibo.ui.Messages;
import de.afbb.bibo.ui.form.CopyMovementInventoryForm;
import de.afbb.bibo.ui.form.CopyXviewerForm;
import de.afbb.bibo.ui.form.MediumInformationForm;

public class InventoryView extends AbstractView<Copy> {

	public static final String ID = "de.afbb.bibo.ui.inventory";//$NON-NLS-1$
	private static final String INVENTORY = "invertory";//$NON-NLS-1$

	private Text txtBarcode;
	private Text txtEdition;
	private Text txtCondition;
	private Button btnToList;
	private Button btnToEdit;
	private Button btnSave;

	private CopyXviewerForm xViewer;
	private CopyMovementInventoryForm movementForm;
	private MediumInformationForm mediumInformationForm;

	private final HashMap<String, Copy> copyCache = new HashMap<>();
	private final Set<Copy> copies = new HashSet<Copy>();

	@Override
	protected Composite initUi(final Composite parent) throws ConnectException {
		final Composite content = toolkit.createComposite(parent, SWT.NONE);
		content.setLayout(new GridLayout(3, false));

		final Group copyGroup = toolkit.createGroup(content, "Exemplar");
		copyGroup.setLayout(new GridLayout(2, false));
		toolkit.createLabel(copyGroup, "Barcode");
		txtBarcode = toolkit.createText(copyGroup, EMPTY_STRING, SWT.RIGHT | SWT.SINGLE);
		txtBarcode.setMessage("Barcode einscannen");
		txtBarcode.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(final FocusEvent e) {
				loadCopyFromDatabase(txtBarcode.getText());
			}

			@Override
			public void focusGained(final FocusEvent e) {
			}
		});
		toolkit.createLabel(copyGroup, Messages.EDITION);
		txtEdition = toolkit.createText(copyGroup, EMPTY_STRING, SWT.READ_ONLY);
		GridDataFactory.swtDefaults().span(2, 1).applyTo(toolkit.createLabel(copyGroup, "Zustand"));
		txtCondition = toolkit.createText(copyGroup, EMPTY_STRING, SWT.MULTI);

		final Group statusGroup = toolkit.createGroup(content, "Informationen");
		movementForm = new CopyMovementInventoryForm(statusGroup, input, bindingContext, toolkit);

		final Group mediumGroup = toolkit.createGroup(content, "Allgemein");
		mediumInformationForm = new MediumInformationForm(mediumGroup, new Medium(), bindingContext, toolkit);

		final Composite middle = toolkit.createComposite(content, SWT.NONE);
		middle.setLayout(new GridLayout(2, false));
		btnToList = toolkit.createButton(middle, "In Liste übernehmen", SWT.NONE);
		btnToEdit = toolkit.createButton(middle, "In Beareitung übernehmen", SWT.NONE);

		xViewer = new CopyXviewerForm(content, INVENTORY);
		xViewer.getTree().addSelectionListener(xViewerSelectionListener);

		final Composite footer = toolkit.createComposite(content, SWT.NONE);
		footer.setLayout(new GridLayout(1, false));
		btnSave = toolkit.createButton(footer, "Inventur abschließen", SWT.NONE);

		GridDataFactory.fillDefaults().grab(true, true).applyTo(content);
		GridDataFactory.fillDefaults().hint(150, SWT.DEFAULT).grab(true, false).applyTo(copyGroup);
		GridDataFactory.fillDefaults().applyTo(statusGroup);
		GridDataFactory.fillDefaults().applyTo(mediumGroup);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtBarcode);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtEdition);
		GridDataFactory.fillDefaults().span(2, 1).grab(true, true).applyTo(txtCondition);
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
			public void handleEvent(final Event event) {
				doSave(null);
			}
		});

		txtCondition.setEnabled(false);
		btnToList.setEnabled(false);
		btnToEdit.setEnabled(false);
		btnSave.setEnabled(false);

		return content;
	}

	@Override
	protected void initBinding() throws ConnectException {
		BindingHelper.bindStringToTextField(txtBarcode, getInputObservable(), Copy.FIELD_BARCODE, bindingContext,
				false);
		BindingHelper.bindStringToTextField(txtCondition, getInputObservable(), Copy.FIELD_CONDITION, bindingContext,
				false);
		BindingHelper.bindStringToTextField(txtEdition, getInputObservable(), Copy.FIELD_EDITION, bindingContext,
				false);

		// one-way binding to update the input in sub-form
		bindingContext.bindValue(BeansObservables.observeValue(mediumInformationForm, INPUT),
				BeansObservables.observeDetailValue(inputObservable, Copy.FIELD_MEDIUM, Medium.class),
				new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER), null);
		bindingContext.bindValue(BeansObservables.observeValue(movementForm, INPUT), inputObservable,
				new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER), null);
	}

	@Override
	public void setFocus() {
		txtBarcode.setFocus();
	}

	private void updateSaveButton() {
		firePropertyChange(PROP_DIRTY);
		btnSave.setEnabled(isDirty());
	}

	/**
	 * listener that removes the selected item from the list and fills the input
	 * fields with its values
	 */
	Listener toEditListener = new Listener() {

		@Override
		public void handleEvent(final Event event) {
			final TreePath[] paths = xViewer.getSelection().getPaths();
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
			final Copy clone = (Copy) input.clone();
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

	private void moveToEdit(final Copy copy) {
		copies.remove(copy);
		setCopyToModify(copy);
		updateSaveButton();
		btnToEdit.setEnabled(false);
		xViewer.setInput(copies);
		bindingContext.updateTargets();
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
		setInput(copy);

		txtCondition.setEnabled(copy != null);
		btnToList.setEnabled(copy != null);
		bindingContext.updateTargets();
	}

	@Override
	public boolean isDirty() {
		return isSaveAble() && !markedAsSaved
				&& (input == null || input.getBarcode() == null || input.getBarcode().isEmpty()) && !copies.isEmpty();
	}

	@Override
	public void doSave(final IProgressMonitor monitor) {
		super.doSave(monitor);
		final Job job = new Job("Inventur abschließen") {

			@Override
			protected IStatus run(final IProgressMonitor monitor) {
				try {
					ServiceLocator.getInstance().getCopyService().doInventory(copies);
				} catch (final ConnectException e) {
					handle(e);
				}
				return Status.OK_STATUS;
			}

		};
		job.schedule();
		closeView();
	}

}
