package de.afbb.bibo.ui.view;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.nebula.widgets.xviewer.XViewer;
import org.eclipse.nebula.widgets.xviewer.XViewerColumn;
import org.eclipse.nebula.widgets.xviewer.XViewerColumn.SortDataType;
import org.eclipse.nebula.widgets.xviewer.XViewerFactory;
import org.eclipse.swt.SWT;
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
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

import de.afbb.bibo.databinding.BindingHelper;
import de.afbb.bibo.share.model.Copy;
import de.afbb.bibo.share.model.IconType;
import de.afbb.bibo.ui.BiboImageRegistry;
import de.afbb.bibo.ui.IconSize;
import de.afbb.bibo.ui.provider.BiboXViewerFactory;
import de.afbb.bibo.ui.provider.CopyLabelProvider;
import de.afbb.bibo.ui.provider.CopyTreeContentProvider;

/**
 * this view allows the registration of new exemplars
 * 
 * @author dbecker
 */
public class RegisterExemplarView extends AbstractEditView {

	public static final String ID = "de.afbb.bibo.ui.registerexemplar";//$NON-NLS-1$
	private static final String DOT = ".";//$NON-NLS-1$
	private static final String REGISTER_COPY = "register.copy";//$NON-NLS-1$
	private static final String TYPE = "Typ";
	private static final String BARCODE = "Barcode";
	private static final String ISBN = "ISBN";//$NON-NLS-1$
	private static final String TITLE = "Titel";
	private static final String AUTHOR = "Autor";
	private static final String PUBLISHER = "Verlag";
	private static final String LANGUAGE = "Sprache";
	private static final String EDITION = "Auflage";

	private final Set<Copy> copies = new HashSet<Copy>();
	private final Copy copyToModify = new Copy();

	private Group idGroup;
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

	private XViewer xViewer;
	private final XViewerFactory factory = new BiboXViewerFactory(REGISTER_COPY);
	private XViewerColumn columnType;
	private XViewerColumn columnBarcode;
	private XViewerColumn columnIsbn;
	private XViewerColumn columnTitle;
	private XViewerColumn columnAuthor;
	private XViewerColumn columnPublisher;
	private XViewerColumn columnLanguage;
	private XViewerColumn columnEdition;

	private static final int UNASSIGNED_GROUP = -1;
	private int highestAssignedGroup = UNASSIGNED_GROUP;

	/**
	 * listener that adds a copy to the list and clears the input fields afterwards
	 */
	Listener toListListener = new Listener() {

		@Override
		public void handleEvent(final Event event) {
			final Copy clone = (Copy) copyToModify.clone();
			copies.add(clone);
			copyToModify.setBarcode(EMPTY_STRING);
			copyToModify.setIsbn(EMPTY_STRING);
			copyToModify.setEdition(EMPTY_STRING);
			copyToModify.setTitle(EMPTY_STRING);
			copyToModify.setAuthor(EMPTY_STRING);
			copyToModify.setLanguage(EMPTY_STRING);
			copyToModify.setPublisher(EMPTY_STRING);
			xViewer.setInput(copies);
			bindingContext.updateTargets();
			txtBarcode.setFocus();
		}
	};

	/**
	 * listener that removes the selected item from the list and fills the input fields with its values
	 */
	Listener toEditListener = new Listener() {

		@Override
		public void handleEvent(final Event event) {
			// FIXME hack, should be solved be resetting the selection in viewer instead
			btnToEdit.setEnabled(false);
			btnUngroup.setEnabled(false);
			final TreePath[] paths = ((TreeSelection) xViewer.getSelection()).getPaths();
			if (paths.length > 0) {
				final Copy copy = (Copy) paths[0].getLastSegment();
				copyToModify.setBarcode(copy.getBarcode());
				copyToModify.setIsbn(copy.getIsbn());
				copyToModify.setEdition(copy.getEdition());
				copyToModify.setTitle(copy.getTitle());
				copyToModify.setAuthor(copy.getAuthor());
				copyToModify.setLanguage(copy.getLanguage());
				copyToModify.setPublisher(copy.getPublisher());
				// FIXME doesn't work when inside group
				copies.remove(copy);
				checkGroups();
				xViewer.setInput(copies);
				bindingContext.updateTargets();
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
			final Iterator<Copy> iterator = ((TreeSelection) xViewer.getSelection()).iterator();
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
			final Iterator<Copy> iterator = ((TreeSelection) xViewer.getSelection()).iterator();
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
	 * checks that there are no groups with only one member left. will reset group information on those copies
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

		// clear group id for copies that are in list and have a group id that is in purgedGroupes
		for (final Copy copy : copies) {
			if (purgedGroupes.contains(copy.getGroupId())) {
				copy.setGroupId(UNASSIGNED_GROUP);
			}
		}

	}

	/**
	 * listener that reacts when the selection changes and enables & disables control buttons
	 */
	SelectionListener xViewerSelectionListener = new SelectionListener() {

		@Override
		public void widgetSelected(final SelectionEvent e) {
			final ISelection selection = xViewer.getSelection();
			if (selection instanceof TreeSelection) {
				final boolean singleSelection = ((TreeSelection) selection).size() == 1;

				// check if any item on the selection is grouped
				boolean grouped = false;
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

	@Override
	public void initUi(final Composite parent) {
		final Composite top = toolkit.createComposite(parent, SWT.NONE);
		final GridLayout layout = new GridLayout(2, false);
		top.setLayout(layout);

		idGroup = createGroup(top, "Nummer");
		idGroup.setLayout(new GridLayout(2, false));
		toolkit.createLabel(idGroup, BARCODE);
		txtBarcode = toolkit.createText(idGroup, EMPTY_STRING);
		toolkit.createLabel(idGroup, ISBN);
		txtIsbn = toolkit.createText(idGroup, EMPTY_STRING);
		toolkit.createLabel(idGroup, EDITION);
		txtEdition = toolkit.createText(idGroup, EMPTY_STRING);

		final Group mediumGroup = createGroup(top, "Informationen");
		mediumGroup.setLayout(new GridLayout(4, false));
		toolkit.createLabel(mediumGroup, TITLE);
		txtTitle = toolkit.createText(mediumGroup, EMPTY_STRING);
		toolkit.createLabel(mediumGroup, AUTHOR);
		txtAuthor = toolkit.createText(mediumGroup, EMPTY_STRING);
		toolkit.createLabel(mediumGroup, LANGUAGE);
		txtLanguage = toolkit.createText(mediumGroup, EMPTY_STRING);
		toolkit.createLabel(mediumGroup, PUBLISHER);
		txtPublisher = toolkit.createText(mediumGroup, EMPTY_STRING);
		toolkit.createLabel(mediumGroup, "Typ");
		toolkit.createText(mediumGroup, "Typ");

		final Composite middle = toolkit.createComposite(top, SWT.NONE);
		middle.setLayout(new GridLayout(2, false));
		btnToList = toolkit.createButton(middle, "In Liste übernehmen", SWT.NONE);
		btnToEdit = toolkit.createButton(middle, "In Beareitung übernehmen", SWT.NONE);

		final Composite bottom = toolkit.createComposite(top, SWT.NONE);
		bottom.setLayout(new GridLayout(2, false));
		final GridData layoutDataMiddle = new GridData(SWT.CENTER, SWT.DEFAULT, true, false);
		layoutDataMiddle.horizontalSpan = 2;
		bottom.setLayoutData(layoutDataMiddle);

		initTableColumns();
		xViewer = new XViewer(bottom, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION, factory);
		xViewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		final CopyTreeContentProvider contentProvider = new CopyTreeContentProvider();
		xViewer.setContentProvider(contentProvider);
		xViewer.setLabelProvider(new CopyLabelProvider(xViewer, contentProvider));
		xViewer.getTree().addSelectionListener(xViewerSelectionListener);

		final Composite buttonComposite = toolkit.createComposite(bottom, SWT.NONE);
		buttonComposite.setLayout(new GridLayout());
		btnGroup = toolkit.createButton(buttonComposite, "Medien Gruppieren", SWT.TOP);
		btnUngroup = toolkit.createButton(buttonComposite, "Gruppierung Lösen", SWT.TOP);

		GridDataFactory.fillDefaults().applyTo(idGroup);
		GridDataFactory.fillDefaults().span(2, 1).align(SWT.CENTER, SWT.CENTER).grab(true, false).applyTo(middle);
		GridDataFactory.fillDefaults().applyTo(mediumGroup);
		GridDataFactory.fillDefaults().span(2, 1).grab(true, true).applyTo(bottom);
		GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.TOP).applyTo(buttonComposite);

		// set button images
		btnToList.setImage(BiboImageRegistry.getImage(IconType.ARROW_DOWN, IconSize.small));
		btnToEdit.setImage(BiboImageRegistry.getImage(IconType.ARROW_UP, IconSize.small));
		btnGroup.setImage(BiboImageRegistry.getImage(IconType.PLUS, IconSize.small));
		btnUngroup.setImage(BiboImageRegistry.getImage(IconType.MINUS, IconSize.small));

		// add listener to buttons
		btnToList.addListener(SWT.MouseDown, toListListener);
		btnToEdit.addListener(SWT.MouseDown, toEditListener);
		btnGroup.addListener(SWT.MouseDown, groupListener);
		btnUngroup.addListener(SWT.MouseDown, ungroupListener);

		// disable buttons
		btnToEdit.setEnabled(false);
		btnGroup.setEnabled(false);
		btnUngroup.setEnabled(false);
	}

	@Override
	protected void initBinding() {
		BindingHelper.bindStringToTextField(txtBarcode, copyToModify, Copy.class, Copy.FIELD_BARCODE, bindingContext, true);
		BindingHelper.bindStringToTextField(txtIsbn, copyToModify, Copy.class, Copy.FIELD_ISBN, bindingContext, false);
		BindingHelper.bindStringToTextField(txtEdition, copyToModify, Copy.class, Copy.FIELD_EDITION, bindingContext, false);
		BindingHelper.bindStringToTextField(txtTitle, copyToModify, Copy.class, Copy.FIELD_TITLE, bindingContext, false);
		BindingHelper.bindStringToTextField(txtAuthor, copyToModify, Copy.class, Copy.FIELD_AUTHOR, bindingContext, false);
		BindingHelper.bindStringToTextField(txtLanguage, copyToModify, Copy.class, Copy.FIELD_LANGUAGE, bindingContext, false);
		BindingHelper.bindStringToTextField(txtPublisher, copyToModify, Copy.class, Copy.FIELD_PUBLISHER, bindingContext, false);
	}

	@Override
	public void setFocus() {
		idGroup.setFocus();
	}

	@Override
	public void init(final IEditorSite site, final IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
	}

	private Group createGroup(final Composite parent, final String text) {
		final Group group = new Group(parent, SWT.SHADOW_NONE);
		toolkit.adapt(group);
		group.setText(text);
		group.setBackground(toolkit.getColors().getBackground());
		group.setForeground(toolkit.getColors().getForeground());
		toolkit.paintBordersFor(group);
		return group;
	}

	private void initTableColumns() {
		columnType = new XViewerColumn(REGISTER_COPY + DOT + TYPE, TYPE, 90, SWT.LEFT, true, SortDataType.String, false, "Typ des Mediums");
		columnBarcode = new XViewerColumn(REGISTER_COPY + DOT + BARCODE, BARCODE, 80, SWT.LEFT, true, SortDataType.Integer, false,
				"Barcode des Mediums");
		columnIsbn = new XViewerColumn(REGISTER_COPY + DOT + ISBN, ISBN, 80, SWT.LEFT, true, SortDataType.Integer, false,
				"ISBN des Mediums");
		columnTitle = new XViewerColumn(REGISTER_COPY + DOT + TITLE, TITLE, 150, SWT.LEFT, true, SortDataType.String, false, TITLE);
		columnAuthor = new XViewerColumn(REGISTER_COPY + DOT + AUTHOR, AUTHOR, 150, SWT.LEFT, true, SortDataType.String, false, AUTHOR);
		columnPublisher = new XViewerColumn(REGISTER_COPY + DOT + PUBLISHER, PUBLISHER, 150, SWT.LEFT, true, SortDataType.String, false,
				PUBLISHER);
		columnLanguage = new XViewerColumn(REGISTER_COPY + DOT + LANGUAGE, LANGUAGE, 150, SWT.LEFT, true, SortDataType.String, false,
				LANGUAGE);
		columnEdition = new XViewerColumn(REGISTER_COPY + DOT + EDITION, EDITION, 150, SWT.LEFT, true, SortDataType.String, false, EDITION);
		factory.registerColumns(columnType, columnBarcode, columnIsbn, columnTitle, columnAuthor, columnPublisher, columnLanguage,
				columnEdition);
	}

	@Override
	public boolean isDirty() {
		return !copies.isEmpty();
	}
}
