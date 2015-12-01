package de.afbb.bibo.ui;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import de.afbb.bibo.ui.view.BorrowerView;
import de.afbb.bibo.ui.view.LendCopyView;
import de.afbb.bibo.ui.view.NavigationView;
import de.afbb.bibo.ui.view.RegisterCopyView;
import de.afbb.bibo.ui.view.ReturnCopyView;
import de.afbb.bibo.ui.view.WelcomeView;

public class Perspective implements IPerspectiveFactory {

	public static final String ID = "de.afbb.bibo.ui.perspective";//$NON-NLS-1$

	@Override
	public void createInitialLayout(final IPageLayout layout) {
		final String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);

		layout.addStandaloneView(NavigationView.ID, false, IPageLayout.LEFT, 0.35f, editorArea);
		final IFolderLayout folder = layout.createFolder("de.afbb.bibo.ui.category.main", IPageLayout.RIGHT, 0.5f,
				NavigationView.ID);
		folder.addView(WelcomeView.ID);
		folder.addPlaceholder(BorrowerView.ID);
		folder.addPlaceholder(LendCopyView.ID);
		folder.addPlaceholder(RegisterCopyView.ID);
		folder.addPlaceholder(ReturnCopyView.ID);
		layout.getViewLayout(NavigationView.ID).setCloseable(false);
		layout.getViewLayout(NavigationView.ID).setMoveable(false);
	}
}
