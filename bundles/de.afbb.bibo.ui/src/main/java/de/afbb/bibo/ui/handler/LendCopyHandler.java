package de.afbb.bibo.ui.handler;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import de.afbb.bibo.share.impl.NavigationTreeViewNode;
import de.afbb.bibo.ui.view.LendCopyView;

public class LendCopyHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		ISelection selection = window.getSelectionService().getSelection();
		if (selection instanceof IStructuredSelection) {
			Iterator iterator = ((IStructuredSelection) selection).iterator();
			while (iterator.hasNext()) {
				Object next = iterator.next();
				if (next instanceof NavigationTreeViewNode) {
					Object input = ((NavigationTreeViewNode) next).getValue();
					if (input instanceof IEditorInput) {
						try {
							window.getActivePage().openEditor((IEditorInput) input, LendCopyView.ID);
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
