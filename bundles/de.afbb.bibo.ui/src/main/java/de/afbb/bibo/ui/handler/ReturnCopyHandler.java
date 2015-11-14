package de.afbb.bibo.ui.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import de.afbb.bibo.share.model.Curator;
import de.afbb.bibo.ui.view.ReturnCopyView;

public class ReturnCopyHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			// currator as dummy input to not mess with register view
			page.openEditor(new Curator(), ReturnCopyView.ID);
		} catch (final PartInitException e) {
			e.printStackTrace();
			// shouldn't happen
		}
		return null;
	}

}
