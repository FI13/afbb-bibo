package de.afbb.bibo.ui.view;

import java.net.ConnectException;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import de.afbb.bibo.share.impl.NavigationTreeService;
import de.afbb.bibo.ui.handler.ManageBorrowerHandler;
import de.afbb.bibo.ui.provider.NavigationTreeViewContentProvider;
import de.afbb.bibo.ui.provider.NavigationTreeViewLabelProvider;

public class NavigationView extends ViewPart {

	public static final String ID = "de.afbb.bibo.ui.navigationView";//$NON-NLS-1$
	private TreeViewer viewer;

	@Override
	public void createPartControl(final Composite parent) {
		NavigationTreeService navigationTree;
		try {
			navigationTree = new NavigationTreeService();
			viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
			viewer.setContentProvider(new NavigationTreeViewContentProvider());
			viewer.setLabelProvider(new NavigationTreeViewLabelProvider());
			viewer.addDoubleClickListener(new TreeCollapseExpandListener(viewer));
			viewer.addDoubleClickListener(new IDoubleClickListener() {

				@Override
				public void doubleClick(DoubleClickEvent event) {
					ManageBorrowerHandler.openForSelection((IStructuredSelection) event.getSelection());
					// TODO add handler for medium selection
				}
			});
			viewer.setAutoExpandLevel(2);
			viewer.setInput(navigationTree.getRoot());

			// popup menu related stuff
			MenuManager manager = new MenuManager();
			viewer.getTree().setMenu(manager.createContextMenu(viewer.getTree()));
			getSite().registerContextMenu(manager, viewer);
			getSite().setSelectionProvider(viewer);
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