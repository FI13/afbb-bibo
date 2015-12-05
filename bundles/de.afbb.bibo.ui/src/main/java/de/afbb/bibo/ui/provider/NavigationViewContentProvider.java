package de.afbb.bibo.ui.provider;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.afbb.bibo.aggregation.NavigationTreeViewNode;

public class NavigationViewContentProvider implements IStructuredContentProvider, ITreeContentProvider {

	@Override
	public void inputChanged(final Viewer v, final Object oldInput, final Object newInput) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public Object[] getElements(final Object parent) {
		return getChildren(parent);
	}

	@Override
	public Object getParent(final Object child) {
		if (child instanceof NavigationTreeViewNode) {
			return ((NavigationTreeViewNode) child).getParent();
		}
		return null;
	}

	@Override
	public Object[] getChildren(final Object parent) {
		if (parent instanceof NavigationTreeViewNode) {
			return ((NavigationTreeViewNode) parent).getChildren();
		}
		return new Object[0];
	}

	@Override
	public boolean hasChildren(final Object parent) {
		if (parent instanceof NavigationTreeViewNode) {
			return ((NavigationTreeViewNode) parent).hasChildren();
		}
		return false;
	}
}