package de.afbb.bibo.ui.form;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.nebula.widgets.xviewer.XViewer;
import org.eclipse.nebula.widgets.xviewer.XViewerColumn;
import org.eclipse.nebula.widgets.xviewer.XViewerColumn.SortDataType;
import org.eclipse.nebula.widgets.xviewer.XViewerFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;

import de.afbb.bibo.share.model.Copy;
import de.afbb.bibo.ui.BiboFormToolkit;
import de.afbb.bibo.ui.Messages;
import de.afbb.bibo.ui.events.TreeCollapseExpandListener;
import de.afbb.bibo.ui.provider.BiboXViewerFactory;
import de.afbb.bibo.ui.provider.CopyLabelProvider;
import de.afbb.bibo.ui.provider.CopyTreeContentProvider;

/**
 * Form that displays a {@link Collection} of {@link Copy}s
 *
 * @author David Becker
 *
 */
public class CopyXviewerForm {

	private static final String DOT = ".";//$NON-NLS-1$
	private final String namespace;

	private final XViewerFactory factory;
	private XViewer xViewer;

	BiboFormToolkit toolkit = new BiboFormToolkit(Display.getCurrent());
	private Composite content;
	private final boolean showMovementColumns;

	public CopyXviewerForm(final Composite parent, final String namespace) {
		this(parent, SWT.NONE, namespace, true);
	}

	public CopyXviewerForm(final Composite parent, final int style, final String namespace,
			final boolean showMovementColumns) {
		this.namespace = namespace;
		this.showMovementColumns = showMovementColumns;
		factory = new BiboXViewerFactory(namespace);
		initUi(parent, style);
	}

	private void initUi(final Composite parent, final int style) {
		content = toolkit.createComposite(parent, SWT.NONE);
		final GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = layout.marginHeight = 0;
		content.setLayout(layout);
		content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		initTableColumns();
		xViewer = new XViewer(content, style | SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION, factory);
		xViewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		final CopyTreeContentProvider contentProvider = new CopyTreeContentProvider();
		xViewer.setContentProvider(contentProvider);
		xViewer.setLabelProvider(new CopyLabelProvider(xViewer, contentProvider));
		xViewer.addDoubleClickListener(new TreeCollapseExpandListener(xViewer));
		xViewer.getMenuManager().dispose();
		GridDataFactory.fillDefaults().hint(300, 150).grab(true, true).applyTo(xViewer.getControl());
	}

	private void initTableColumns() {
		final XViewerColumn columnType = new XViewerColumn(namespace + DOT + Messages.TYPE, Messages.TYPE, 90, SWT.LEFT,
				true, SortDataType.String, false, "Typ des Mediums");
		final XViewerColumn columnBarcode = new XViewerColumn(namespace + DOT + Messages.BARCODE, Messages.BARCODE, 80,
				SWT.RIGHT, true, SortDataType.Integer, false, "Barcode des Mediums");
		final XViewerColumn columnIsbn = new XViewerColumn(namespace + DOT + Messages.ISBN, Messages.ISBN, 120,
				SWT.RIGHT, true, SortDataType.Integer, false, "ISBN des Mediums");
		final XViewerColumn columnTitle = new XViewerColumn(namespace + DOT + Messages.TITLE, Messages.TITLE, 250,
				SWT.LEFT, true, SortDataType.String, false, Messages.TITLE);
		final XViewerColumn columnAuthor = new XViewerColumn(namespace + DOT + Messages.AUTHOR, Messages.AUTHOR, 150,
				SWT.LEFT, true, SortDataType.String, false, Messages.AUTHOR);
		final XViewerColumn columnPublisher = new XViewerColumn(namespace + DOT + Messages.PUBLISHER,
				Messages.PUBLISHER, 150, SWT.LEFT, true, SortDataType.String, false, Messages.PUBLISHER);
		final XViewerColumn columnLanguage = new XViewerColumn(namespace + DOT + Messages.LANGUAGE, Messages.LANGUAGE,
				150, SWT.LEFT, true, SortDataType.String, false, Messages.LANGUAGE);
		final XViewerColumn columnEdition = new XViewerColumn(namespace + DOT + Messages.EDITION, Messages.EDITION, 150,
				SWT.LEFT, true, SortDataType.String, false, Messages.EDITION);

		if (showMovementColumns) {
			final XViewerColumn columnLendDate = new XViewerColumn(namespace + DOT + Messages.LEND_DATE,
					Messages.LEND_DATE, 150, SWT.CENTER, true, SortDataType.Date, false, Messages.LEND_DATE);
			final XViewerColumn columnLendCurator = new XViewerColumn(namespace + DOT + Messages.LEND_CURATOR,
					Messages.LEND_CURATOR, 150, SWT.LEFT, true, SortDataType.String, false, Messages.LEND_CURATOR);
			final XViewerColumn columnLendBorrower = new XViewerColumn(namespace + DOT + Messages.LEND_BORROWER,
					Messages.LEND_BORROWER, 150, SWT.LEFT, true, SortDataType.String, false, Messages.LEND_BORROWER);
			final XViewerColumn columnReturnDate = new XViewerColumn(namespace + DOT + Messages.RETURN_DATE,
					Messages.RETURN_DATE, 150, SWT.CENTER, true, SortDataType.Date, false, Messages.RETURN_DATE);
			final XViewerColumn columnReturnCurator = new XViewerColumn(namespace + DOT + Messages.RETURN_CURATOR,
					Messages.RETURN_CURATOR, 150, SWT.LEFT, true, SortDataType.String, false, Messages.RETURN_CURATOR);
			final XViewerColumn columnReturnBorrower = new XViewerColumn(namespace + DOT + Messages.RETURN_BORROWER,
					Messages.RETURN_BORROWER, 150, SWT.LEFT, true, SortDataType.String, false,
					Messages.RETURN_BORROWER);

			factory.registerColumns(columnType, columnBarcode, columnIsbn, columnTitle, columnAuthor, columnPublisher,
					columnLanguage, columnEdition, columnLendDate, columnLendCurator, columnLendBorrower,
					columnReturnDate, columnReturnCurator, columnReturnBorrower);
		} else {
			factory.registerColumns(columnType, columnBarcode, columnIsbn, columnTitle, columnAuthor, columnPublisher,
					columnLanguage, columnEdition);
		}
	}

	public TreeSelection getSelection() {
		return (TreeSelection) xViewer.getSelection();
	}

	@SuppressWarnings("unchecked") // should be fine
	public Iterator<Copy> getSelectionIterator() {
		return ((StructuredSelection) xViewer.getSelection()).iterator();
	}

	public Tree getTree() {
		return xViewer.getTree();
	}

	public void setInput(final Collection<Copy> copies) {
		xViewer.setInput(copies);
	}

	public Control getControl() {
		return content;
	}

	/**
	 * get the last element of the selection path. on a flat hierarchy it is the
	 * single row
	 *
	 * @return
	 */
	public Copy getLastElementFromSelectionPath() {
		final TreePath[] paths = ((TreeSelection) xViewer.getSelection()).getPaths();
		if (paths.length > 0) {
			return (Copy) paths[0].getLastSegment();
		}
		return null;
	}

}
