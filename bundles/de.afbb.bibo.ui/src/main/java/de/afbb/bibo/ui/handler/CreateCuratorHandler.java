package de.afbb.bibo.ui.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

import de.afbb.bibo.share.model.Curator;

/**
 * handler that creates a new {@link Curator}
 * 
 * @author dbecker
 */
public class CreateCuratorHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Neuanlage",
				"Handler für Neuanlage Verwalter\nSoll später einen neuen Dialog öffnen.");
		return event;
	}

}
