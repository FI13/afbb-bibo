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
import de.afbb.bibo.share.model.Medium;
import de.afbb.bibo.ui.view.BorrowerView;
import de.afbb.bibo.ui.view.MediumView;

/**
 * handler that manages a {@link Borrower} or a {@link Medium}
 *
 * @author David Becker
 */
public class ManageBorrowerMediumHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService()
				.getSelection();
		openForSelection((IStructuredSelection) selection);
		return null;
	}

	private static void openForSelection(final IStructuredSelection selection) {
		final Iterator<?> iterator = selection.iterator();
		while (iterator.hasNext()) {
			final Object next = iterator.next();
			if (next instanceof NavigationTreeViewNode) {
				final Object input = ((NavigationTreeViewNode) next).getValue();
				if (input instanceof Borrower) {
					try {
						final IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench()
								.getActiveWorkbenchWindow();
						final IViewPart showView = activeWorkbenchWindow.getActivePage().showView(BorrowerView.ID);
						if (showView instanceof BorrowerView) {
							final BorrowerView view = (BorrowerView) showView;
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
					}
				} else if (input instanceof Medium) {
					try {
						final IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench()
								.getActiveWorkbenchWindow();
						final IViewPart showView = activeWorkbenchWindow.getActivePage().showView(MediumView.ID);
						if (showView instanceof MediumView) {
							final MediumView view = (MediumView) showView;
							final boolean dirty = view.isDirty();
							if (dirty
									&& MessageDialog.openQuestion(activeWorkbenchWindow.getShell(),
											"Offene Änderungen verwerfen?",
											"Es bestehen noch offen Änderungen.\nSollen diese Änderungen verworfen werden?")
									|| !dirty) {
								view.setInput((Medium) input);
							}
						}
					} catch (final PartInitException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}
}
