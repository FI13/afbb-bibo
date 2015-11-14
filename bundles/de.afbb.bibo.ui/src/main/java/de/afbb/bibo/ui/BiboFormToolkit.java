package de.afbb.bibo.ui;

import javax.swing.text.html.parser.DTD;

import org.eclipse.nebula.widgets.cdatetime.CDT;
import org.eclipse.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
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

	public CDateTime createCDateTime(final Composite parent) {
		CDateTime dateTime = new CDateTime(parent, CDT.BORDER | CDT.DATE_MEDIUM);
		dateTime.setNullText("");
		return dateTime;
	}

	public DateTime createDateTime(final Composite parent) {
		DateTime dateTime = new DateTime(parent, SWT.DATE);
		adapt(dateTime);
		paintBordersFor(dateTime);
		return dateTime;
	}

}
