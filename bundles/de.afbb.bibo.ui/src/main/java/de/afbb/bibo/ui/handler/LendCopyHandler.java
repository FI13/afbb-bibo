package de.afbb.bibo.ui.handler;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import de.afbb.bibo.share.impl.NavigationTreeViewNode;
import de.afbb.bibo.share.model.Borrower;
import de.afbb.bibo.ui.view.LendCopyView;

public class LendCopyHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		final ISelection selection = window.getSelectionService().getSelection();
		if (selection instanceof IStructuredSelection) {
			final Iterator<?> iterator = ((IStructuredSelection) selection).iterator();
			while (iterator.hasNext()) {
				final Object next = iterator.next();
				if (next instanceof NavigationTreeViewNode) {
					final Object input = ((NavigationTreeViewNode) next).getValue();
					if (input instanceof Borrower) {
						try {
							final IViewPart showView = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
									.getActivePage().showView(LendCopyView.ID);
							if (showView instanceof LendCopyView) {
								((LendCopyView) showView).setInput((Borrower) input);
							}
						} catch (final PartInitException e) {
							e.printStackTrace();
							// shouldn't happen
						}
					}
				}
			}
		}
		return null;
	}

}
