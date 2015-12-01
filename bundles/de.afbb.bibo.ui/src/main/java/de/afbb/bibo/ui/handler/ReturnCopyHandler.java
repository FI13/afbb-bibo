package de.afbb.bibo.ui.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import de.afbb.bibo.share.internal.model.CuratorInput;
import de.afbb.bibo.ui.view.ReturnCopyView;

public class ReturnCopyHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			// currator as dummy input to not mess with register view
			page.openEditor(new CuratorInput(), ReturnCopyView.ID);
		} catch (final PartInitException e) {
			e.printStackTrace();
			// shouldn't happen
		}
		return null;
	}

}
