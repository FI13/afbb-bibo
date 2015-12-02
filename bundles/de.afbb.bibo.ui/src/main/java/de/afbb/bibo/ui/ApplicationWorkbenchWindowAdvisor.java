package de.afbb.bibo.ui;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import de.afbb.bibo.share.SessionHolder;
import de.afbb.bibo.share.model.Curator;

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
		configurer.setInitialSize(new Point(1200, 1024));
		configurer.setShowCoolBar(true);
		configurer.setShowProgressIndicator(true);
	}

	@Override
	public void postWindowOpen() {
		final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

		// FIXME for debugging purposes only! comment out when server is ready!
		// new LoginDialog(shell).open();
		// TODO start remove here
		// SessionHolder.getInstance().setSessionToken("token123");//$NON-NLS-1$
		final Curator curator = new Curator();
		curator.setName("Hugo");//$NON-NLS-1$
		SessionHolder.getInstance().setCurator(curator);
		// TODO end remove here

		if (shell != null && !shell.isDisposed()) {
			shell.setText(TITLE + " - Angemeldet als: " + SessionHolder.getInstance().getCurator().getName());

		}
	}

}
