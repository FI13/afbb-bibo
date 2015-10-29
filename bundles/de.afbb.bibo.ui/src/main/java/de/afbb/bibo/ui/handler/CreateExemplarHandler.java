package de.afbb.bibo.ui.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

import de.afbb.bibo.share.model.Exemplar;

/**
 * handler that creates new {@link Exemplar}s
 * 
 * @author dbecker
 */
public class CreateExemplarHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Neuanlage",
				"Handler für Erfassung Exemplare\nSoll später einen neuen Dialog/Editor öffnen.");
		return event;
	}

}
