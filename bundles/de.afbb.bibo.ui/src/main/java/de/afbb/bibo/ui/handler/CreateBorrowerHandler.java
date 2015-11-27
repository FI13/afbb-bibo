package de.afbb.bibo.ui.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import de.afbb.bibo.share.model.Borrower;
import de.afbb.bibo.ui.dialog.CreateBorrowerDialog;
import de.afbb.bibo.ui.dialog.ManageCuratorDialog;
import de.afbb.bibo.ui.view.BorrowerView;

/**
 * handler that creates a new {@link Borrower}
 * 
 * @author dbecker
 */
public class CreateBorrowerHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		new CreateBorrowerDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()).open();
		return null;
	}

}
