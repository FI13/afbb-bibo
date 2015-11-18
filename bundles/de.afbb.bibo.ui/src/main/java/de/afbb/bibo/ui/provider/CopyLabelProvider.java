package de.afbb.bibo.ui.provider;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.nebula.widgets.xviewer.XViewer;
import org.eclipse.nebula.widgets.xviewer.XViewerColumn;
import org.eclipse.nebula.widgets.xviewer.XViewerLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import de.afbb.bibo.share.model.Copy;
import de.afbb.bibo.ui.BiboImageRegistry;
import de.afbb.bibo.ui.IconSize;

/**
 * label provider for an {@link XViewer} that displays instances of {@link Copy}
 * 
 * @author dbecker
 */
public class CopyLabelProvider extends XViewerLabelProvider {

	private final ITreeContentProvider contentProvider;

	/**
	 * regular Constructor. doesn't provide highlighting of parent elements
	 *
	 * @param viewer
	 *            the {@link XViewer} that is used with this label provider
	 */
	public CopyLabelProvider(final XViewer viewer) {
		this(viewer, null);
	}

	/**
	 * Constructor.
	 *
	 * @param viewer
	 *            the {@link XViewer} that is used with this label provider
	 * @param contentProvider
	 *            the content provider for viewer. can be <code>null</code>
	 */
	public CopyLabelProvider(final XViewer viewer, final ITreeContentProvider contentProvider) {
		super(viewer);
		this.contentProvider = contentProvider;
	}

	@Override
	public void addListener(final ILabelProviderListener listener) {
	}

	@Override
	public void removeListener(final ILabelProviderListener listener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean isLabelProperty(final Object element, final String property) {
		return false;
	}

	@Override
	public Image getColumnImage(final Object element, final XViewerColumn xCol, final int columnIndex)
			throws Exception {
		final Copy copy = (Copy) element;
		if (columnIndex == 0 && copy.getMedium() != null && copy.getMedium().getType() != null) {
			return BiboImageRegistry.getImage(copy.getMedium().getType().getIcon(), IconSize.small);
		}
		return null;
	}

	@Override
	public String getColumnText(final Object element, final XViewerColumn xCol, final int columnIndex)
			throws Exception {
		final Copy copy = (Copy) element;
		String value = "";//$NON-NLS-1$
		if (columnIndex == 0 && copy.getMedium() != null && copy.getMedium().getType() != null) {
			value = copy.getMedium().getType().getName();
		} else if (columnIndex == 1 && copy.getBarcode() != null) {
			value = copy.getBarcode();
		} else if (columnIndex == 2 && copy.getMedium() != null && copy.getMedium().getIsbn() != null) {
			value = copy.getMedium().getIsbn();
		} else if (columnIndex == 3 && copy.getMedium() != null && copy.getMedium().getAuthor() != null) {
			value = copy.getMedium().getAuthor();
		} else if (columnIndex == 4 && copy.getMedium() != null && copy.getMedium().getPublisher() != null) {
			value = copy.getMedium().getPublisher();
		} else if (columnIndex == 5 && copy.getMedium() != null && copy.getMedium().getLanguage() != null) {
			value = copy.getMedium().getLanguage();
		} else if (columnIndex == 6 && copy.getEdition() != null) {
			value = copy.getEdition();
		}
		return value;
	}

	@Override
	public Color getBackground(final Object element, final int columnIndex) {
		// darken the background for parent elements
		if (contentProvider != null && contentProvider.hasChildren(element)) {
			return Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);
		}
		return null;
	}

}
