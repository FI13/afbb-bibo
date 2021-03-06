package de.afbb.bibo.ui.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;

import de.afbb.bibo.share.model.Curator;
import de.afbb.bibo.ui.dialog.ManageCuratorDialog;

/**
 * handler that creates a new {@link Curator}
 * 
 * @author dbecker
 */
public class ManageCuratorHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		new ManageCuratorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), false).open();
		return null;
	}

}
