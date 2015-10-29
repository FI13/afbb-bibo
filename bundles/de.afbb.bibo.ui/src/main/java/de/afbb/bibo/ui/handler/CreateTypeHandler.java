package de.afbb.bibo.ui.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;

import de.afbb.bibo.ui.dialog.CreateTypeDialog;

/**
 * handler that creates a new {@link Type}
 * 
 * @author dbecker
 */
public class CreateTypeHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		new CreateTypeDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()).open();
		return null;
	}

}
