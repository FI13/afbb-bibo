package de.afbb.bibo.ui;

/**
 * Interface defining the application's command IDs. Key bindings can be defined
 * for specific commands. To associate an action with a command, use
 * IAction.setActionDefinitionId(commandId).
 *
 * @see org.eclipse.jface.action.IAction#setActionDefinitionId(String)
 */
public interface ICommandIds {

	public static final String CMD_LEND = "de.afbb.bibo.ui.lend";//$NON-NLS-1$
	public static final String CMD_LOGIN = "de.afbb.bibo.ui.login";//$NON-NLS-1$
	public static final String CMD_RETURN = "de.afbb.bibo.ui.return";//$NON-NLS-1$

	public static final String CMD_CREATE_CURATOR = "de.afbb.bibo.ui.create.curator";//$NON-NLS-1$
	public static final String CMD_CREATE_COPY = "de.afbb.bibo.ui.create.copy";//$NON-NLS-1$
	public static final String CMD_CREATE_BORROWER = "de.afbb.bibo.ui.create.borrower";//$NON-NLS-1$
	public static final String CMD_CREATE_TYPE = "de.afbb.bibo.ui.create.type";//$NON-NLS-1$

	public static final String CMD_MANAGE_CURATOR = "de.afbb.bibo.ui.manage.curator";//$NON-NLS-1$
	public static final String CMD_MANAGE_BORROWER_MEDIUM = "de.afbb.bibo.ui.manage.BorrowerMedium";//$NON-NLS-1$

}
