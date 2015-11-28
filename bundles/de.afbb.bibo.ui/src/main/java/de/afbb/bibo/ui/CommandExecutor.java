package de.afbb.bibo.ui;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;

/**
 * utility class to execute commands
 * 
 * @author David Becker
 *
 */
public final class CommandExecutor {

	private static IHandlerService handlerService;

	private CommandExecutor() {
	}

	private static IHandlerService getHandlerService() {
		// non-thread safe lazy initialization
		if (handlerService == null) {
			handlerService = (IHandlerService) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.getActivePart().getSite().getService(IHandlerService.class);
		}
		return handlerService;
	}

	/**
	 * Executes a given command. might do nothing if there isn't an active
	 * handler to handle the event
	 * 
	 * @param commandId
	 */
	public static void executeCommand(String commandId) {
		try {
			if (commandId != null) {
				getHandlerService().executeCommand(commandId, null);
			}
		} catch (ExecutionException | NotDefinedException | NotEnabledException | NotHandledException e) {
			// just swallowing the exception here
		}
	}

}
