package de.afbb.bibo.ui;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	public static final String ID = "de.afbb.bibo.ui.perspective";//$NON-NLS-1$

	@Override
	public void createInitialLayout(final IPageLayout layout) {
		final String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);

		layout.addStandaloneView(NavigationView.ID, false, IPageLayout.LEFT, 0.25f, editorArea);
		layout.getViewLayout(NavigationView.ID).setCloseable(false);
		layout.getViewLayout(NavigationView.ID).setMoveable(false);
	}
}
