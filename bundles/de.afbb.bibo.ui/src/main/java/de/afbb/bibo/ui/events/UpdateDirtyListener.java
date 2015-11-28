package de.afbb.bibo.ui.events;

import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import de.afbb.bibo.ui.view.IDirtyEvaluate;

/**
 *
 * focus listener that re-evaluates the dirty state of an {@link IDirtyEvaluate}
 * editor
 *
 * @author David Becker
 *
 */
public class UpdateDirtyListener extends FocusAdapter {

	IDirtyEvaluate editor = null;

	@Override
	public void focusLost(final FocusEvent e) {
		super.focusLost(e);
		/*
		 * will update an open editor when focus is lost in a dialog, but I
		 * don't think that will be a problem
		 */
		final IWorkbenchPart activePart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getActivePart();
		if (activePart instanceof IDirtyEvaluate) {
			((IDirtyEvaluate) activePart).updateDirtyState();
		}
	}

}
