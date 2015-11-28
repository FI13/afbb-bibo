package de.afbb.bibo.ui.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;

import de.afbb.bibo.share.model.Borrower;
import de.afbb.bibo.ui.dialog.CreateBorrowerDialog;

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
