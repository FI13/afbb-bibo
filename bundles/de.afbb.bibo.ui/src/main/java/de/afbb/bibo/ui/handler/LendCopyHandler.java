package de.afbb.bibo.ui.handler;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import de.afbb.bibo.aggregation.NavigationTreeViewNode;
import de.afbb.bibo.share.model.Borrower;
import de.afbb.bibo.ui.view.LendCopyView;

public class LendCopyHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		final IWorkbenchWindow window = activeWorkbenchWindow;
		final ISelection selection = window.getSelectionService().getSelection();
		if (selection instanceof IStructuredSelection) {
			final Iterator<?> iterator = ((IStructuredSelection) selection).iterator();
			while (iterator.hasNext()) {
				final Object next = iterator.next();
				if (next instanceof NavigationTreeViewNode) {
					final Object input = ((NavigationTreeViewNode) next).getValue();
					if (input instanceof Borrower) {
						try {
							final IViewPart showView = activeWorkbenchWindow.getActivePage().showView(LendCopyView.ID);
							if (showView instanceof LendCopyView) {
								final LendCopyView view = (LendCopyView) showView;
								final boolean dirty = view.isDirty();
								if (dirty
										&& MessageDialog.openQuestion(activeWorkbenchWindow.getShell(),
												"Offene Änderungen verwerfen?",
												"Es bestehen noch offen Änderungen.\nSollen diese Änderungen verworfen werden?")
										|| !dirty) {
									view.setInput((Borrower) input);
								}
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
