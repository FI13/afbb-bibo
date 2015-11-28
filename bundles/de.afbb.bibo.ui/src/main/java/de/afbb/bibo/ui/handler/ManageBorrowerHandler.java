package de.afbb.bibo.ui.handler;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import de.afbb.bibo.share.impl.NavigationTreeViewNode;
import de.afbb.bibo.share.model.Borrower;
import de.afbb.bibo.ui.view.BorrowerView;

/**
 * handler that manages a {@link Borrower}
 * 
 * @author David Becker
 */
public class ManageBorrowerHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService()
				.getSelection();
		openForSelection((IStructuredSelection) selection);
		return null;
	}

	public static void openForSelection(IStructuredSelection selection) {
		Iterator<?> iterator = selection.iterator();
		while (iterator.hasNext()) {
			Object next = iterator.next();
			if (next instanceof NavigationTreeViewNode) {
				Object input = ((NavigationTreeViewNode) next).getValue();
				if (input instanceof Borrower) {
					try {
						IEditorPart openEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
								.openEditor((IEditorInput) input, BorrowerView.ID, true);
					} catch (final PartInitException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}
}
