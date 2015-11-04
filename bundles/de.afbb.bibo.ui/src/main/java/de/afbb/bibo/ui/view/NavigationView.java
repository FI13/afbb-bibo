package de.afbb.bibo.ui.view;

import java.net.ConnectException;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import de.afbb.bibo.share.impl.NavigationTreeService;

public class NavigationView extends ViewPart {

	public static final String ID = "de.afbb.bibo.ui.navigationView";
	private TreeViewer viewer;

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(final Composite parent) {
		NavigationTreeService navigationTree;
		try {
			navigationTree = new NavigationTreeService();
			viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
			viewer.setContentProvider(new NavigationTreeViewContentProvider());
			viewer.setLabelProvider(new NavigationTreeViewLabelProvider());
			viewer.setInput(navigationTree.getRoot());
		} catch (final ConnectException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}