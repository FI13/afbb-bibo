package de.afbb.bibo.ui.provider;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.nebula.widgets.xviewer.XViewer;
import org.eclipse.nebula.widgets.xviewer.XViewerColumn;
import org.eclipse.nebula.widgets.xviewer.XViewerLabelProvider;
import org.eclipse.swt.graphics.Image;

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
		return null;
	}

	@Override
	public String getColumnText(final Object element, final XViewerColumn xCol, final int columnIndex) throws Exception {
		return "test";
	}

}
