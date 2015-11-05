package de.afbb.bibo.ui.provider;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class CopyTreeContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
	}

	@Override
	public Object[] getElements(final Object inputElement) {
		return null;
	}

	@Override
	public Object[] getChildren(final Object parentElement) {
		return null;
	}

	@Override
	public Object getParent(final Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(final Object element) {
		return false;
	}

}
