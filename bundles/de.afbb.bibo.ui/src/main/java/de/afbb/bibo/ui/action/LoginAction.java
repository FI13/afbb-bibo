package de.afbb.bibo.ui.action;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;

import de.afbb.bibo.ui.ICommandIds;
import de.afbb.bibo.ui.dialog.LoginDialog;

public class LoginAction extends Action {

	private final IWorkbenchWindow window;
	private final int instanceNum = 0;
	private final String viewId;

	public LoginAction(final IWorkbenchWindow window, final String label, final String viewId) {
		this.window = window;
		this.viewId = viewId;
		setText(label);
		// The id is used to refer to the action in a menu or toolbar
		setId(ICommandIds.CMD_OPEN);
		// Associate the action with a pre-defined command, to allow key bindings.
		setActionDefinitionId(ICommandIds.CMD_OPEN);
		setImageDescriptor(de.afbb.bibo.ui.Activator.getImageDescriptor("/icons/16x16login.png"));
	}

	@Override
	public void run() {
		if (window != null) {

			final LoginDialog loginDialog = new LoginDialog(window.getShell());
			loginDialog.open();

		}
	}
}
