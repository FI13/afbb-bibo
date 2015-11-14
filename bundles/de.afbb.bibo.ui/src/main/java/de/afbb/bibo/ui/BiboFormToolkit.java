package de.afbb.bibo.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * toolkit with specialized methods
 * 
 * @author david
 *
 */
public class BiboFormToolkit extends FormToolkit {

	public BiboFormToolkit(Display display) {
		super(display);
	}

	/**
	 * creates a group and adapts it for use with this toolkit
	 * 
	 * @param parent
	 * @param text
	 * @return
	 */
	public Group createGroup(final Composite parent, final String text) {
		final Group group = new Group(parent, SWT.SHADOW_NONE);
		adapt(group);
		group.setText(text);
		group.setBackground(getColors().getBackground());
		group.setForeground(getColors().getForeground());
		paintBordersFor(group);
		return group;
	}

}
