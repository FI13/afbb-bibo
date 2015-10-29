package de.afbb.bibo.ui;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import de.afbb.bibo.share.SessionHolder;
import de.afbb.bibo.share.model.Curator;
import de.afbb.bibo.ui.view.WelcomeView;

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
		configurer.setInitialSize(new Point(1000, 400));
		configurer.setShowCoolBar(true);
		configurer.setShowStatusLine(false);
	}

	@Override
	public void postWindowOpen() {
		final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		// FIXME for debugging purposes only! comment out when server is ready!
//		new LoginDialog(shell).open();
		// TODO start remove here
		SessionHolder.getInstance().setSessionToken("token123");//$NON-NLS-1$
		final Curator curator = new Curator();
		curator.setName("Hugo");//$NON-NLS-1$
		SessionHolder.getInstance().setCurator(curator);
		// TODO end remove here
		shell.setText(TITLE + " - Angemeldet als: " + SessionHolder.getInstance().getCurator().getName());

		// open welcome view
		final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			page.openEditor(SessionHolder.getInstance().getCurator(), WelcomeView.ID);
		} catch (final PartInitException e) {
			e.printStackTrace();
			// shouldn't happen
		}
	}

}
