package de.afbb.bibo.ui.view;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 
 * listener that collapses or expands the selection of given {@link TreeViewer}
 * 
 * @author David Becker
 *
 */
class TreeCollapseExpandListener implements IDoubleClickListener {

	private TreeViewer viewer;

	/**
	 * Constructor
	 * 
	 * @param viewer
	 *            to get the selection from
	 */
	TreeCollapseExpandListener(TreeViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public void doubleClick(DoubleClickEvent event) {
		// safety check first
		if (event == null || event.getSelection() == null || !(event.getSelection() instanceof TreeSelection)) {
			return;
		}
		TreeSelection selection = (TreeSelection) event.getSelection();
		TreePath[] paths = selection.getPaths();
		TreePath treePath = paths[paths.length - 1];
		Object segment = treePath.getLastSegment();

		TreeItem[] items = viewer.getTree().getItems();
		TreeItem item = getItem(items, segment);
		if (item != null) {
			item.setExpanded(!item.getExpanded());
			viewer.refresh();
		}
	}

	/**
	 * Recursively get the {@link TreeItem} that matches the given segment
	 * 
	 * @param items
	 *            to search children of
	 * @param segment
	 *            to match against
	 * @return <code>null</code> when no matching item was found, item otherwise
	 */
	TreeItem getItem(TreeItem[] items, Object segment) {
		TreeItem item = null;
		for (int i = 0; i < items.length; i++) {
			item = items[i];
			if (segment.equals(item.getData())) {
				return item;
			}
			int itemCount = item.getItemCount();
			if (itemCount > 0) {
				TreeItem item2 = getItem(item.getItems(), segment);
				if (item2 != null) {
					return item2;
				}
			}
		}
		return null;
	}

}
