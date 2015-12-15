package de.afbb.bibo.ui.view;

import java.io.IOException;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import de.afbb.bibo.amazon.ParserMedium;
import de.afbb.bibo.databinding.BindingHelper;
import de.afbb.bibo.servletclient.ServiceLocator;
import de.afbb.bibo.share.model.Copy;
import de.afbb.bibo.share.model.IconType;
import de.afbb.bibo.share.model.Medium;
import de.afbb.bibo.share.model.MediumType;
import de.afbb.bibo.ui.BiboImageRegistry;
import de.afbb.bibo.ui.IconSize;
import de.afbb.bibo.ui.Messages;
import de.afbb.bibo.ui.form.CopyXviewerForm;
import de.afbb.bibo.ui.provider.MediumTypeLabelProvider;

/**
 * this view allows the registration of new exemplars
 *
 * @author dbecker
 */
public class RegisterCopyView extends AbstractView<Copy> {

	public static final String ID = "de.afbb.bibo.ui.registerexemplar";//$NON-NLS-1$
	private static final String REGISTER_COPY = "register.copy";//$NON-NLS-1$
	private static final String BARCODE = "barcode";//$NON-NLS-1$

	private final Set<Copy> copies = new HashSet<Copy>();

	private Group idGroup;
	private Text txtBarcode;
	private Text txtIsbn;
	private Text txtEdition;
	private Text txtTitle;
	private Text txtAuthor;
	private Text txtLanguage;
	private Text txtPublisher;
	private Text txtCondition;
	private Button btnToList;
	private Button btnToEdit;
	private Button btnGroup;
	private Button btnUngroup;
	private Button btnSave;
	private CCombo comboMediumType;

	private CopyXviewerForm xViewer;

	private static final int UNASSIGNED_GROUP = -1;
	private int highestAssignedGroup = UNASSIGNED_GROUP + 1;
	private final HashMap<String, Medium> mediumCache = new HashMap<>();

	private String barcode = null;
	private final IObservableValue barcodeObservable = BeansObservables.observeValue(this, BARCODE);

	/**
	 * listener that adds a copy to the list and clears the input fields
	 * afterwards
	 */
	Listener toListListener = new Listener() {

		@Override
		public void handleEvent(final Event event) {
			final Copy clone = (Copy) input.clone();
			copies.add(clone);
			// hack to reset the input
			setInput(null);
			setInput(new Copy());
			updateSaveButton();
			xViewer.setInput(copies);
			txtBarcode.setFocus();
			updateToListButton();
		}
	};

	/**
	 * listener that removes the selected item from the list and fills the input
	 * fields with its values
	 */
	Listener toEditListener = new Listener() {

		@Override
		public void handleEvent(final Event event) {
			// FIXME hack, should be solved be resetting the selection in viewer
			// instead
			btnToEdit.setEnabled(false);
			btnUngroup.setEnabled(false);
			final TreePath[] paths = xViewer.getSelection().getPaths();
			if (paths.length > 0) {
				final Copy copy = (Copy) paths[0].getLastSegment();
				setInput(copy);
				copies.remove(copy);
				bindingContext.updateTargets();
				checkGroups();
				updateToListButton();
				updateSaveButton();
				xViewer.setInput(copies);
			}
		}
	};

	/**
	 * listener that groups the selected items to one group
	 */
	Listener groupListener = new Listener() {

		@Override
		public void handleEvent(final Event event) {
			highestAssignedGroup++;
			final Iterator<Copy> iterator = xViewer.getSelectionIterator();
			while (iterator.hasNext()) {
				final Copy next = iterator.next();
				next.setGroupId(highestAssignedGroup);
			}
			xViewer.setInput(copies);
		}
	};

	/**
	 * listener that resets the group of the selected items
	 */
	Listener ungroupListener = new Listener() {

		@Override
		public void handleEvent(final Event event) {
			/*
			 * set groupId to UNASSIGNED_GROUP for selection
			 */
			final Set<Integer> purgedGroupes = new HashSet<>();
			final Iterator<Copy> iterator = xViewer.getSelectionIterator();
			while (iterator.hasNext()) {
				final Copy copy = iterator.next();
				if (copies.contains(copy)) {
					copy.setGroupId(UNASSIGNED_GROUP);
				} else {
					// copy not in copies -> parent
					purgedGroupes.add(copy.getGroupId());
				}
			}

			// ungroup for all children of parent
			for (final Integer groupId : purgedGroupes) {
				for (final Copy copy : copies) {
					if (groupId.equals(copy.getGroupId())) {
						copy.setGroupId(UNASSIGNED_GROUP);
					}
				}
			}

			checkGroups();
			xViewer.setInput(copies);
		}
	};

	/**
	 * checks that there are no groups with only one member left. will reset
	 * group information on those copies
	 */
	private void checkGroups() {
		/*
		 * Map<groupId, amount>
		 */
		final Map<Integer, Integer> leftItems = new HashMap<>();

		// calculate the amount of copies that are left for each group id
		int groupId;
		for (final Copy copy : copies) {
			groupId = copy.getGroupId();
			if (UNASSIGNED_GROUP != groupId) {
				if (leftItems.containsKey(groupId)) {
					leftItems.put(groupId, leftItems.get(groupId) + 1);
				} else {
					leftItems.put(groupId, 1);
				}
			}
		}

		// check which group id has only one item left
		final Set<Integer> purgedGroupes = new HashSet<>();
		final Iterator<Integer> it = leftItems.keySet().iterator();
		while (it.hasNext()) {
			final Integer key = it.next();
			final Integer amountLeft = leftItems.get(key);
			if (Integer.valueOf(1).equals(amountLeft)) {
				purgedGroupes.add(key);
			}
		}

		if (purgedGroupes.isEmpty()) {
			// nothing left to do here
			return;
		}

		// clear group id for copies that are in list and have a group id that
		// is in purgedGroupes
		for (final Copy copy : copies) {
			if (purgedGroupes.contains(copy.getGroupId())) {
				copy.setGroupId(UNASSIGNED_GROUP);
			}
		}

	}

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

				// check if any item on the selection is grouped
				boolean grouped = false;
				@SuppressWarnings("unchecked")
				final Iterator<Copy> iterator = ((TreeSelection) selection).iterator();
				while (iterator.hasNext()) {
					final Copy next = iterator.next();
					if (next.getGroupId() > UNASSIGNED_GROUP) {
						grouped = true;
						break;
					}
				}
				btnToEdit.setEnabled(singleSelection);
				btnGroup.setEnabled(!singleSelection && !grouped);
				btnUngroup.setEnabled(singleSelection && grouped);
			}
		}

		@Override
		public void widgetDefaultSelected(final SelectionEvent e) {
			// no double click event
		}
	};

	FocusListener updateToListButtonListener = new FocusListener() {

		@Override
		public void focusLost(final FocusEvent e) {
			updateToListButton();
		}

		@Override
		public void focusGained(final FocusEvent e) {
			// nothing to do
		}
	};

	@Override
	public Composite initUi(final Composite parent) {
		final Composite content = toolkit.createComposite(parent, SWT.NONE);
		content.setLayout(new GridLayout(3, false));

		idGroup = toolkit.createGroup(content, "Nummer");
		idGroup.setLayout(new GridLayout(2, false));
		toolkit.createLabel(idGroup, Messages.BARCODE);
		txtBarcode = toolkit.createText(idGroup, EMPTY_STRING, SWT.SINGLE | SWT.RIGHT);
		txtBarcode.setMessage("Barcode einscannen");
		txtBarcode.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(final FocusEvent e) {
				checkBarcodeExists();
			}

			@Override
			public void focusGained(final FocusEvent e) {
			}
		});
		toolkit.createLabel(idGroup, Messages.ISBN);
		txtIsbn = toolkit.createText(idGroup, EMPTY_STRING);
		txtIsbn.setMessage("ISBN einscannen");
		txtIsbn.setToolTipText("Sie können hier auch eine Seriennummer eingeben, wenn das Medium keine ISBN besitzt.");
		txtIsbn.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(final FocusEvent e) {
				loadMediumFromDatabase(txtIsbn.getText());
			}

			@Override
			public void focusGained(final FocusEvent e) {
			}
		});
		toolkit.createLabel(idGroup, Messages.EDITION);
		txtEdition = toolkit.createText(idGroup, EMPTY_STRING);

		final Group mediumGroup = toolkit.createGroup(content, "Informationen");
		mediumGroup.setLayout(new GridLayout(2, false));
		toolkit.createLabel(mediumGroup, Messages.TITLE);
		txtTitle = toolkit.createText(mediumGroup, EMPTY_STRING);
		txtTitle.addFocusListener(updateToListButtonListener);
		toolkit.createLabel(mediumGroup, Messages.AUTHOR);
		txtAuthor = toolkit.createText(mediumGroup, EMPTY_STRING);
		toolkit.createLabel(mediumGroup, Messages.LANGUAGE);
		txtLanguage = toolkit.createText(mediumGroup, EMPTY_STRING);
		toolkit.createLabel(mediumGroup, Messages.PUBLISHER);
		txtPublisher = toolkit.createText(mediumGroup, EMPTY_STRING);
		toolkit.createLabel(mediumGroup, "Typ");
		comboMediumType = toolkit.createCombo(mediumGroup);
		comboMediumType.addFocusListener(updateToListButtonListener);

		final Group conditionGroup = toolkit.createGroup(content, "Zustand");
		conditionGroup.setLayout(new GridLayout(1, false));
		txtCondition = toolkit.createText(conditionGroup, EMPTY_STRING, SWT.MULTI);
		txtCondition.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		final Composite middle = toolkit.createComposite(content, SWT.NONE);
		middle.setLayout(new GridLayout(2, false));
		btnToList = toolkit.createButton(middle, "In Liste übernehmen", SWT.NONE);
		btnToEdit = toolkit.createButton(middle, "In Beareitung übernehmen", SWT.NONE);

		final Composite bottom = toolkit.createComposite(content, SWT.NONE);
		bottom.setLayout(new GridLayout(2, false));
		final GridData layoutDataMiddle = new GridData(SWT.CENTER, SWT.DEFAULT, true, false);
		layoutDataMiddle.horizontalSpan = 2;
		bottom.setLayoutData(layoutDataMiddle);

		xViewer = new CopyXviewerForm(bottom, SWT.NONE, REGISTER_COPY, false);
		xViewer.getTree().addSelectionListener(xViewerSelectionListener);

		final Composite buttonComposite = toolkit.createComposite(bottom, SWT.NONE);
		final GridLayout layoutButtonComposite = new GridLayout();
		layoutButtonComposite.marginHeight = layoutButtonComposite.marginWidth = 0;
		buttonComposite.setLayout(layoutButtonComposite);
		btnGroup = toolkit.createButton(buttonComposite, "Medien gruppieren", SWT.TOP);
		btnUngroup = toolkit.createButton(buttonComposite, "Gruppierung lösen", SWT.TOP);
		btnSave = toolkit.createButton(buttonComposite, "Erfassung abschließen", SWT.TOP);

		GridDataFactory.fillDefaults().grab(true, true).applyTo(content);
		GridDataFactory.fillDefaults().hint(250, SWT.DEFAULT).applyTo(idGroup);
		GridDataFactory.fillDefaults().span(3, 1).align(SWT.CENTER, SWT.CENTER).grab(true, false).applyTo(middle);
		GridDataFactory.fillDefaults().hint(400, SWT.DEFAULT).applyTo(mediumGroup);
		GridDataFactory.fillDefaults().applyTo(conditionGroup);
		GridDataFactory.fillDefaults().span(3, 1).grab(true, true).applyTo(bottom);
		GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.TOP).applyTo(buttonComposite);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtBarcode);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtIsbn);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtEdition);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtTitle);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtAuthor);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtLanguage);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(txtPublisher);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(comboMediumType);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.END).applyTo(btnGroup);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.END).applyTo(btnUngroup);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.END).applyTo(btnSave);

		// set button images
		btnToList.setImage(BiboImageRegistry.getImage(IconType.ARROW_DOWN, IconSize.small));
		btnToEdit.setImage(BiboImageRegistry.getImage(IconType.ARROW_UP, IconSize.small));
		btnGroup.setImage(BiboImageRegistry.getImage(IconType.PLUS, IconSize.small));
		btnUngroup.setImage(BiboImageRegistry.getImage(IconType.MINUS, IconSize.small));
		btnSave.setImage(BiboImageRegistry.getImage(IconType.SAVE, IconSize.small));

		// add listener to buttons
		btnToList.addListener(SWT.MouseDown, toListListener);
		btnToEdit.addListener(SWT.MouseDown, toEditListener);
		btnGroup.addListener(SWT.MouseDown, groupListener);
		btnUngroup.addListener(SWT.MouseDown, ungroupListener);
		btnSave.addListener(SWT.MouseDown, new Listener() {

			@Override
			public void handleEvent(final Event event) {
				doSave(null);
			}
		});

		// disable buttons
		btnToList.setEnabled(false);
		btnToEdit.setEnabled(false);
		btnGroup.setEnabled(false);
		btnUngroup.setEnabled(false);
		updateSaveButton();

		// add required decorations
		BindingHelper.createRequiredControlDecoration(txtBarcode);
		BindingHelper.createRequiredControlDecoration(txtTitle);
		BindingHelper.createRequiredControlDecoration(comboMediumType);

		return content;
	}

	@Override
	protected void initBinding() throws ConnectException {
		BindingHelper.bindStringToTextField(txtBarcode, getInputObservable(), Copy.FIELD_BARCODE, bindingContext,
				false);
		BindingHelper.bindStringToTextField(txtEdition, getInputObservable(), Copy.FIELD_EDITION, bindingContext,
				false);
		BindingHelper.bindStringToTextField(txtCondition, getInputObservable(), Copy.FIELD_CONDITION, bindingContext,
				false);
		final IObservableValue mediumObservable = BeansObservables.observeDetailValue(getInputObservable(),
				Copy.FIELD_MEDIUM, Medium.class);
		BindingHelper.bindStringToTextField(txtTitle, mediumObservable, Medium.FIELD_TITLE, bindingContext, false);
		BindingHelper.bindStringToTextField(txtAuthor, mediumObservable, Medium.FIELD_AUTHOR, bindingContext, false);
		BindingHelper.bindStringToTextField(txtLanguage, mediumObservable, Medium.FIELD_LANGUAGE, bindingContext,
				false);
		BindingHelper.bindStringToTextField(txtPublisher, mediumObservable, Medium.FIELD_PUBLISHER, bindingContext,
				false);
		BindingHelper.bindStringToTextField(txtIsbn, mediumObservable, Medium.FIELD_ISBN, bindingContext, false);

		BindingHelper.bindObjectToCCombo(comboMediumType, mediumObservable, Medium.class, Medium.FIELD_TYPE,
				MediumType.class, ServiceLocator.getInstance().getTypService().list(), new MediumTypeLabelProvider(),
				bindingContext, false);

		// dummy binding to validate that barcode isn't taken
		final UpdateValueStrategy modelToTarget = new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE);
		modelToTarget.setAfterGetValidator(new BarcodeTakenValidator());
		bindingContext.bindValue(new WritableValue(null, String.class), barcodeObservable,
				new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER), modelToTarget);
	}

	private void loadMediumFromDatabase(final String isbn) {
		// check if we have it cached already first
		if (!mediumCache.containsKey(isbn)) {
			// normal fetch from database
			Medium medium = null;
			try {
				medium = ServiceLocator.getInstance().getMediumService().getMedium(isbn);
				if (medium == null) {
					try {
						medium = ParserMedium.getInstance().getMedium(isbn);
					} catch (final IOException e) {
					}
				}
			} catch (final ConnectException e) {
				handle(e);
			}
			mediumCache.put(isbn, medium);
		}
		final Medium medium = mediumCache.get(isbn);
		if (medium != null && input != null) {
			input.setMedium(medium);
			setInput(input);
		}
	}

	@Override
	public void setFocus() {
		idGroup.setFocus();
	}

	private void checkBarcodeExists() {
		// disabled by default, will be enabled if barcode can't be found
		btnToList.setEnabled(false);
		setBarcode(null);
		final String barcode = txtBarcode.getText();
		if (!barcode.isEmpty()) {
			// check list of local copies first
			for (final Copy copy : copies) {
				if (barcode.equals(copy.getBarcode())) {
					return;
				}
			}
			// check database next
			try {
				if (ServiceLocator.getInstance().getCopyService().exists(barcode)) {
					setBarcode(barcode);
					return;
				}
			} catch (final ConnectException e) {
				handle(e);
			}
			updateToListButton();
		}

	}

	private void updateToListButton() {
		btnToList.setEnabled(input != null && input.getMedium() != null && !input.getBarcode().isEmpty()
				&& !input.getMedium().getTitle().isEmpty() && input.getMedium().getType() != null
				&& !input.getMedium().getType().getName().isEmpty());
	}

	private void updateSaveButton() {
		firePropertyChange(PROP_DIRTY);
		btnSave.setEnabled(isDirty());
	}

	@Override
	public boolean isDirty() {
		return isSaveAble() && !copies.isEmpty();
	}

	@Override
	public void doSave(final IProgressMonitor monitor) {
		super.doSave(monitor);
		final Job job = new Job("Erfassung abschließen") {

			@Override
			protected IStatus run(final IProgressMonitor monitor) {
				try {
					ServiceLocator.getInstance().getCopyService().registerCopies(copies);
				} catch (final ConnectException e) {
					handle(e);
				}
				return Status.OK_STATUS;
			}

		};
		job.schedule();
		closeView();
	}

	private static class BarcodeTakenValidator implements IValidator {

		private static final String MSG = "Dieser Barcode wird schon verwendet!";

		@Override
		public IStatus validate(final Object value) {
			return value == null ? ValidationStatus.ok() : ValidationStatus.error(MSG);
		}

	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(final String barcode) {
		changeSupport.firePropertyChange(BARCODE, this.barcode, this.barcode = barcode);
	}
}
