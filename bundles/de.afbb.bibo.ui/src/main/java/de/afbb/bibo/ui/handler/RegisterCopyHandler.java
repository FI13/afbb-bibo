package de.afbb.bibo.ui.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import de.afbb.bibo.share.model.Copy;
import de.afbb.bibo.ui.view.RegisterCopyView;

/**
 * handler that creates new {@link Copy}s
 * 
 * @author dbecker
 */
public class RegisterCopyHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			page.openEditor(new Copy(), RegisterCopyView.ID);
		} catch (final PartInitException e) {
			e.printStackTrace();
			// shouldn't happen
		}
		return null;
	}

}
