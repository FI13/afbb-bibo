package de.afbb.bibo.ui.provider;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.afbb.bibo.share.model.Copy;

public class CopyTreeContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
	}

	@Override
	public Object[] getElements(final Object inputElement) {
		final HashSet<Copy> elements = new HashSet<>();
		if (inputElement != null && inputElement instanceof Collection<?>) {
			final Iterator<?> iterator = ((Collection<?>) inputElement).iterator();
			while (iterator.hasNext()) {
				final Object next = iterator.next();
				if (next != null && next instanceof Collection<?>) {
					elements.addAll((Collection<Copy>) next);
				}
			}
		}
		return elements.toArray();
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
