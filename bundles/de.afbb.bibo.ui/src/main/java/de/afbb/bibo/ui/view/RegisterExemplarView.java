package de.afbb.bibo.ui.view;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

/**
 * this view allows the registration of new exemplars
 * 
 * @author dbecker
 */
public class RegisterExemplarView extends AbstractEditView {

	public static final String ID = "de.afbb.bibo.ui.registerexemplar";//$NON-NLS-1$

	/**
	 * The text control that's displaying the content of the email message.
	 */
	private Text messageText;

	private Group idGroup;

	@Override
	public void initUi(final Composite parent) {
		final Composite top = toolkit.createComposite(parent, SWT.NONE);
		final GridLayout layout = new GridLayout(2, false);
		top.setLayout(layout);

		idGroup = createGroup(top, "Nummer");
		idGroup.setLayout(new GridLayout(2, false));
		toolkit.createLabel(idGroup, "Inventar-Nummer");
		final Text txtNumber = toolkit.createText(idGroup, "");
		toolkit.createLabel(idGroup, "ISBN");
		final Text txtIsbn = toolkit.createText(idGroup, "");
		toolkit.createLabel(idGroup, "Erscheinungsjahr");
		toolkit.createText(idGroup, "");
		toolkit.createLabel(idGroup, "Auflage");
		toolkit.createText(idGroup, "");

		final Group mediumGroup = createGroup(top, "Informationen");
		mediumGroup.setLayout(new GridLayout(4, false));
		toolkit.createLabel(mediumGroup, "Titel");
		Text txtTitle = toolkit.createText(mediumGroup, "Titel");
		toolkit.createLabel(mediumGroup, "Autor");
		Text txtAuthor = toolkit.createText(mediumGroup, "Autor");
		toolkit.createLabel(mediumGroup, "Sprache");
		Text txtLanguage = toolkit.createText(mediumGroup, "Sprache");
		toolkit.createLabel(mediumGroup, "Verlag");
		Text txtPublisher = toolkit.createText(mediumGroup, "Verlag");
		toolkit.createLabel(mediumGroup, "Typ");
		toolkit.createText(mediumGroup, "Typ");

		final Table copyTable = toolkit.createTable(top, SWT.NONE);

		GridDataFactory.fillDefaults().applyTo(idGroup);
		GridDataFactory.fillDefaults().applyTo(mediumGroup);
		GridDataFactory.fillDefaults().span(2, 1).grab(true, true).applyTo(copyTable);
	}

	@Override
	protected void initBinding() {
	}

	@Override
	public void setFocus() {
		idGroup.setFocus();
	}

	@Override
	public void init(final IEditorSite site, final IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
	}

	private Group createGroup(final Composite parent, final String text) {
		final Group group = new Group(parent, SWT.SHADOW_NONE);
		toolkit.adapt(group);
		group.setText(text);
		group.setBackground(toolkit.getColors().getBackground());
		group.setForeground(toolkit.getColors().getForeground());
		toolkit.paintBordersFor(group);
		return group;
	}
}
