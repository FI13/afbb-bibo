package de.afbb.bibo.ui;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import de.afbb.bibo.share.SessionHolder;
import de.afbb.bibo.ui.dialog.LoginDialog;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	private static final String TITLE = "AfbB Bibliothek";

	public ApplicationWorkbenchWindowAdvisor(final IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	@Override
	public ActionBarAdvisor createActionBarAdvisor(final IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	@Override
	public void preWindowOpen() {
		final IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(600, 400));
		configurer.setShowCoolBar(true);
		configurer.setShowStatusLine(false);
	}

	@Override
	public void postWindowOpen() {
		final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		new LoginDialog(shell).open();
		shell.setText(TITLE + " - Angemeldet als: " + SessionHolder.getInstance().getCurator().getName());
	}

}
