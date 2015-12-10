package de.afbb.bibo.ui;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import de.afbb.bibo.share.SessionHolder;
import de.afbb.bibo.ui.dialog.LoginDialog;
import de.afbb.bibo.ui.view.WelcomeView;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	private static final String TITLE = "AFBB Bibliothek";

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
		final IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		final Shell shell = activeWorkbenchWindow.getShell();

		new LoginDialog(shell).open();

		if (shell != null && !shell.isDisposed() && SessionHolder.getInstance().getCurator() != null) {
			shell.setText(TITLE + " - Angemeldet als: " + SessionHolder.getInstance().getCurator().getName());
			if (SessionHolder.getInstance().getCurator().isShowWelcome()) {
				try {
					activeWorkbenchWindow.getActivePage().showView(WelcomeView.ID);
				} catch (final PartInitException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
