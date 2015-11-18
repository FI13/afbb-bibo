package de.afbb.bibo.ui.form;

import java.util.Collection;
import java.util.Set;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.nebula.widgets.xviewer.XViewer;
import org.eclipse.nebula.widgets.xviewer.XViewerColumn;
import org.eclipse.nebula.widgets.xviewer.XViewerFactory;
import org.eclipse.nebula.widgets.xviewer.XViewerColumn.SortDataType;
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
	private String namespace;

	private final XViewerFactory factory;
	private XViewer xViewer;
	private XViewerColumn columnType;
	private XViewerColumn columnBarcode;
	private XViewerColumn columnIsbn;
	private XViewerColumn columnTitle;
	private XViewerColumn columnAuthor;
	private XViewerColumn columnPublisher;
	private XViewerColumn columnLanguage;
	private XViewerColumn columnEdition;

	BiboFormToolkit toolkit = new BiboFormToolkit(Display.getCurrent());
	private Composite content;

	public CopyXviewerForm(Composite parent, String namespace) {
		this(parent, SWT.NONE, namespace);
	}

	public CopyXviewerForm(Composite parent, int style, String namespace) {
		this.namespace = namespace;
		factory = new BiboXViewerFactory(namespace);
		initUi(parent, style);
	}

	private void initUi(Composite parent, int style) {
		content = toolkit.createComposite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = layout.marginHeight = 0;
		content.setLayout(layout);
		content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		initTableColumns();
		xViewer = new XViewer(content, style | SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION, factory);
		xViewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		final CopyTreeContentProvider contentProvider = new CopyTreeContentProvider();
		xViewer.setContentProvider(contentProvider);
		xViewer.setLabelProvider(new CopyLabelProvider(xViewer, contentProvider));
		xViewer.getMenuManager().dispose();
		GridDataFactory.fillDefaults().grab(true, true).applyTo(xViewer.getControl());
	}

	private void initTableColumns() {
		columnType = new XViewerColumn(namespace + DOT + Messages.TYPE, Messages.TYPE, 90, SWT.LEFT, true,
				SortDataType.String, false, "Typ des Mediums");
		columnBarcode = new XViewerColumn(namespace + DOT + Messages.BARCODE, Messages.BARCODE, 80, SWT.LEFT, true,
				SortDataType.Integer, false, "Barcode des Mediums");
		columnIsbn = new XViewerColumn(namespace + DOT + Messages.ISBN, Messages.ISBN, 80, SWT.LEFT, true,
				SortDataType.Integer, false, "ISBN des Mediums");
		columnTitle = new XViewerColumn(namespace + DOT + Messages.TITLE, Messages.TITLE, 150, SWT.LEFT, true,
				SortDataType.String, false, Messages.TITLE);
		columnAuthor = new XViewerColumn(namespace + DOT + Messages.AUTHOR, Messages.AUTHOR, 150, SWT.LEFT, true,
				SortDataType.String, false, Messages.AUTHOR);
		columnPublisher = new XViewerColumn(namespace + DOT + Messages.PUBLISHER, Messages.PUBLISHER, 150, SWT.LEFT,
				true, SortDataType.String, false, Messages.PUBLISHER);
		columnLanguage = new XViewerColumn(namespace + DOT + Messages.LANGUAGE, Messages.LANGUAGE, 150, SWT.LEFT, true,
				SortDataType.String, false, Messages.LANGUAGE);
		columnEdition = new XViewerColumn(namespace + DOT + Messages.EDITION, Messages.EDITION, 150, SWT.LEFT, true,
				SortDataType.String, false, Messages.EDITION);
		factory.registerColumns(columnType, columnBarcode, columnIsbn, columnTitle, columnAuthor, columnPublisher,
				columnLanguage, columnEdition);
	}

	public TreeSelection getSelection() {
		return (TreeSelection) xViewer.getSelection();
	}

	public Tree getTree() {
		return xViewer.getTree();
	}

	public void setInput(Set<Copy> copies) {
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
