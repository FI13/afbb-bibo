package de.afbb.bibo.ui.view;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ISelection;
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
import de.afbb.bibo.ui.BiboImageRegistry;
import de.afbb.bibo.ui.IconSize;
import de.afbb.bibo.ui.IconType;
import de.afbb.bibo.ui.provider.BiboXViewerFactory;
import de.afbb.bibo.ui.provider.CopyLabelProvider;
import de.afbb.bibo.ui.provider.CopyTreeContentProvider;

/**
 * this view allows the registration of new exemplars
 * 
 * @author dbecker
 */
public class RegisterExemplarView extends AbstractEditView {

	private static final String DOT = ".";//$NON-NLS-1$
	private static final String REGISTER_COPY = "register.copy";//$NON-NLS-1$
	public static final String ID = "de.afbb.bibo.ui.registerexemplar";//$NON-NLS-1$
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

	Listener toListListener = new Listener() {

		@Override
		public void handleEvent(final Event event) {
			final Copy clone = (Copy) copyToModify.clone();
			copies.add(clone);
			copyToModify.setBarcode("");
			copyToModify.setIsbn("");
			copyToModify.setEdition("");
			copyToModify.setTitle("");
			copyToModify.setAuthor("");
			copyToModify.setLanguage("");
			copyToModify.setPublisher("");
			xViewer.setInput(copies);
			bindingContext.updateTargets();
		}
	};
	Listener toEditListener = new Listener() {

		@Override
		public void handleEvent(final Event event) {
			System.err.println("to edit");
		}
	};
	SelectionListener xViewerSelectionListener = new SelectionListener() {

		@Override
		public void widgetSelected(final SelectionEvent e) {
			final ISelection selection = xViewer.getSelection();
			if (selection instanceof TreeSelection) {
				final boolean singleSelection = ((TreeSelection) selection).getPaths().length == 1;
				btnToEdit.setEnabled(singleSelection);
				btnGroup.setEnabled(!singleSelection);
				btnUngroup.setEnabled(!singleSelection);
			}
		}

		@Override
		public void widgetDefaultSelected(final SelectionEvent e) {
			// no double click event
		}
	};
	private Button btnToList;
	private Button btnToEdit;
	private Button btnGroup;
	private Button btnUngroup;

	@Override
	public void initUi(final Composite parent) {
		final Composite top = toolkit.createComposite(parent, SWT.NONE);
		final GridLayout layout = new GridLayout(2, false);
		top.setLayout(layout);

		idGroup = createGroup(top, "Nummer");
		idGroup.setLayout(new GridLayout(2, false));
		toolkit.createLabel(idGroup, BARCODE);
		txtBarcode = toolkit.createText(idGroup, "");
		toolkit.createLabel(idGroup, ISBN);
		txtIsbn = toolkit.createText(idGroup, "");
		toolkit.createLabel(idGroup, EDITION);
		txtEdition = toolkit.createText(idGroup, "");

		final Group mediumGroup = createGroup(top, "Informationen");
		mediumGroup.setLayout(new GridLayout(4, false));
		toolkit.createLabel(mediumGroup, TITLE);
		txtTitle = toolkit.createText(mediumGroup, "");
		toolkit.createLabel(mediumGroup, AUTHOR);
		txtAuthor = toolkit.createText(mediumGroup, "");
		toolkit.createLabel(mediumGroup, LANGUAGE);
		txtLanguage = toolkit.createText(mediumGroup, "");
		toolkit.createLabel(mediumGroup, PUBLISHER);
		txtPublisher = toolkit.createText(mediumGroup, "");
		toolkit.createLabel(mediumGroup, "Typ");
		toolkit.createText(mediumGroup, "Typ");

		final Composite middle = toolkit.createComposite(top, SWT.NONE);
		middle.setLayout(new GridLayout(2, false));
		btnToList = toolkit.createButton(middle, "In Liste übernehmen", SWT.NONE);
		btnToList.setImage(BiboImageRegistry.getImage(IconType.ARROW_DOWN, IconSize.small));
		btnToList.addListener(SWT.MouseDown, toListListener);
		btnToEdit = toolkit.createButton(middle, "In Beareitung übernehmen", SWT.NONE);
		btnToEdit.setImage(BiboImageRegistry.getImage(IconType.ARROW_UP, IconSize.small));
		btnToEdit.addListener(SWT.MouseDown, toEditListener);

		final Composite bottom = toolkit.createComposite(top, SWT.NONE);
		bottom.setLayout(new GridLayout(2, false));
		final GridData layoutDataMiddle = new GridData(SWT.CENTER, SWT.DEFAULT, true, false);
		layoutDataMiddle.horizontalSpan = 2;
		bottom.setLayoutData(layoutDataMiddle);

		initTableColumns();
		xViewer = new XViewer(bottom, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION, factory);
		xViewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		xViewer.setContentProvider(new CopyTreeContentProvider());
		xViewer.setLabelProvider(new CopyLabelProvider(xViewer));
		xViewer.getTree().addSelectionListener(xViewerSelectionListener);

		final Composite buttonComposite = toolkit.createComposite(bottom, SWT.NONE);
		buttonComposite.setLayout(new GridLayout());
		btnGroup = toolkit.createButton(buttonComposite, "Medien Gruppieren", SWT.TOP);
		btnGroup.setImage(BiboImageRegistry.getImage(IconType.PLUS, IconSize.small));
		btnUngroup = toolkit.createButton(buttonComposite, "Gruppierung Lösen", SWT.TOP);
		btnUngroup.setImage(BiboImageRegistry.getImage(IconType.MINUS, IconSize.small));

		GridDataFactory.fillDefaults().applyTo(idGroup);
		GridDataFactory.fillDefaults().span(2, 1).align(SWT.CENTER, SWT.CENTER).grab(true, false).applyTo(middle);
		GridDataFactory.fillDefaults().applyTo(mediumGroup);
		GridDataFactory.fillDefaults().span(2, 1).grab(true, true).applyTo(bottom);
		GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.TOP).applyTo(buttonComposite);

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
		columnType = new XViewerColumn(REGISTER_COPY + DOT + TYPE, TYPE, 50, SWT.LEFT, true, SortDataType.String, false, "Typ des Mediums");
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
}
