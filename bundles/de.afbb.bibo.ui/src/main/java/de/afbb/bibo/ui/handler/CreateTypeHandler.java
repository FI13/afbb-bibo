package de.afbb.bibo.ui.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

/**
 * handler that creates a new {@link Type}
 * 
 * @author dbecker
 */
public class CreateTypeHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Neuanlage",
				"Handler für Neuanlage Typ\nSoll später einen neuen Dialog/Editor öffnen.");
		return event;
	}

}
