package de.afbb.bibo.ui.provider;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.nebula.widgets.xviewer.XViewer;
import org.eclipse.nebula.widgets.xviewer.XViewerColumn;
import org.eclipse.nebula.widgets.xviewer.XViewerLabelProvider;
import org.eclipse.swt.graphics.Image;

import de.afbb.bibo.share.model.Copy;
import de.afbb.bibo.ui.BiboImageRegistry;
import de.afbb.bibo.ui.IconSize;

public class CopyLabelProvider extends XViewerLabelProvider {

	public CopyLabelProvider(final XViewer viewer) {
		super(viewer);
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
	public Image getColumnImage(final Object element, final XViewerColumn xCol, final int columnIndex) throws Exception {
		final Copy copy = (Copy) element;
		if (columnIndex == 0 && copy.getType() != null) {
			return BiboImageRegistry.getImage(copy.getType().getIcon(), IconSize.small);
		}
		return null;
	}

	@Override
	public String getColumnText(final Object element, final XViewerColumn xCol, final int columnIndex) throws Exception {
		final Copy copy = (Copy) element;
		String value = "";//$NON-NLS-1$
		if (columnIndex == 0 && copy.getType() != null) {
			value = copy.getType().getName();
		} else if (columnIndex == 1 && copy.getBarcode() != null) {
			value = copy.getBarcode();
		} else if (columnIndex == 2 && copy.getIsbn() != null) {
			value = copy.getIsbn();
		} else if (columnIndex == 3 && copy.getAuthor() != null) {
			value = copy.getAuthor();
		} else if (columnIndex == 4 && copy.getPublisher() != null) {
			value = copy.getPublisher();
		} else if (columnIndex == 5 && copy.getLanguage() != null) {
			value = copy.getLanguage();
		} else if (columnIndex == 6 && copy.getEdition() != null) {
			value = copy.getEdition();
		}
		return value;
	}

}
