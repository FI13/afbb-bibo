package de.afbb.bibo.ui.view;

import java.net.ConnectException;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import de.afbb.bibo.aggregation.NavigationTreeService;
import de.afbb.bibo.ui.CommandExecutor;
import de.afbb.bibo.ui.ICommandIds;
import de.afbb.bibo.ui.events.TreeCollapseExpandListener;
import de.afbb.bibo.ui.provider.NavigationViewContentProvider;
import de.afbb.bibo.ui.provider.NavigationViewLabelProvider;

public class NavigationView extends ViewPart {

	public static final String ID = "de.afbb.bibo.ui.navigationView";//$NON-NLS-1$
	private TreeViewer viewer;

	@Override
	public void createPartControl(final Composite parent) {
		NavigationTreeService navigationTree;
		try {
			viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
			viewer.setContentProvider(new NavigationViewContentProvider());
			ColumnViewerToolTipSupport.enableFor(viewer);
			viewer.setLabelProvider(new NavigationViewLabelProvider());
			viewer.addDoubleClickListener(new TreeCollapseExpandListener(viewer));
			viewer.addDoubleClickListener(new IDoubleClickListener() {

				@Override
				public void doubleClick(final DoubleClickEvent event) {
					CommandExecutor.executeCommand(ICommandIds.CMD_MANAGE_BORROWER_MEDIUM);
				}
			});
			viewer.setAutoExpandLevel(2);
			navigationTree = new NavigationTreeService(viewer);
			viewer.setInput(navigationTree.getRoot());

			// popup menu related stuff
			final MenuManager manager = new MenuManager();
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