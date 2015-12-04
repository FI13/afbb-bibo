package de.afbb.bibo.ui.events;

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
public class TreeCollapseExpandListener implements IDoubleClickListener {

	private final TreeViewer viewer;

	/**
	 * Constructor
	 *
	 * @param viewer
	 *            to get the selection from
	 */
	public TreeCollapseExpandListener(final TreeViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public void doubleClick(final DoubleClickEvent event) {
		// safety check first
		if (event == null || event.getSelection() == null || !(event.getSelection() instanceof TreeSelection)) {
			return;
		}
		final TreeSelection selection = (TreeSelection) event.getSelection();
		final TreePath[] paths = selection.getPaths();
		final TreePath treePath = paths[paths.length - 1];
		final Object segment = treePath.getLastSegment();

		final TreeItem[] items = viewer.getTree().getItems();
		final TreeItem item = getItem(items, segment);
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
	TreeItem getItem(final TreeItem[] items, final Object segment) {
		TreeItem item = null;
		for (int i = 0; i < items.length; i++) {
			item = items[i];
			if (segment.equals(item.getData())) {
				return item;
			}
			final int itemCount = item.getItemCount();
			if (itemCount > 0) {
				final TreeItem item2 = getItem(item.getItems(), segment);
				if (item2 != null) {
					return item2;
				}
			}
		}
		return null;
	}

}
