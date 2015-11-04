package de.afbb.bibo.ui.view;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

import de.afbb.bibo.share.model.Copy;
import de.afbb.bibo.ui.Activator;

/**
 * this view allows the registration of new exemplars
 * 
 * @author dbecker
 */
public class RegisterExemplarView extends AbstractEditView {

	public static final String ID = "de.afbb.bibo.ui.registerexemplar";//$NON-NLS-1$
	private static final String IMAGE_GROUP = "icons/16x16medien.png";//$NON-NLS-1$

	private final Set<Set<Copy>> copies = new HashSet<Set<Copy>>();

	/**
	 * The text control that's displaying the content of the email message.
	 */
	private Text messageText;

	private Group idGroup;

	public RegisterExemplarView() {
		JFaceResources.getImageRegistry().put(IMAGE_GROUP, Activator.getImageDescriptor(IMAGE_GROUP).createImage());
	}

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
		final Text txtTitle = toolkit.createText(mediumGroup, "Titel");
		toolkit.createLabel(mediumGroup, "Autor");
		final Text txtAuthor = toolkit.createText(mediumGroup, "Autor");
		toolkit.createLabel(mediumGroup, "Sprache");
		final Text txtLanguage = toolkit.createText(mediumGroup, "Sprache");
		toolkit.createLabel(mediumGroup, "Verlag");
		final Text txtPublisher = toolkit.createText(mediumGroup, "Verlag");
		toolkit.createLabel(mediumGroup, "Typ");
		toolkit.createText(mediumGroup, "Typ");

		final Composite middle = toolkit.createComposite(top, SWT.NONE);
		GridDataFactory.fillDefaults().span(2, 1).align(SWT.CENTER, SWT.CENTER).grab(true, false).applyTo(middle);
		middle.setLayout(new GridLayout(2, false));
		final Button btnToList = toolkit.createButton(middle, "In Liste übernehmen", SWT.NONE);
		final Button btnToEdit = toolkit.createButton(middle, "In Beareitung übernehmen", SWT.NONE);

		final Composite bottom = toolkit.createComposite(top, SWT.NONE);
		bottom.setLayout(new GridLayout(2, false));
		final GridData layoutDataMiddle = new GridData(SWT.CENTER, SWT.DEFAULT, true, false);
		layoutDataMiddle.horizontalSpan = 2;
		bottom.setLayoutData(layoutDataMiddle);
		final Table copyTable = toolkit.createTable(bottom, SWT.NONE);
		final Composite buttonComposite = toolkit.createComposite(bottom, SWT.NONE);
		buttonComposite.setLayout(new GridLayout());
		final Button btnGroup = toolkit.createButton(buttonComposite, "Medien Gruppieren", SWT.TOP);
		btnGroup.setImage(JFaceResources.getImage(IMAGE_GROUP));
		final Button btnUngroup = toolkit.createButton(buttonComposite, "Gruppierung Lösen", SWT.TOP);

		GridDataFactory.fillDefaults().applyTo(idGroup);
		GridDataFactory.fillDefaults().applyTo(mediumGroup);
		GridDataFactory.fillDefaults().span(2, 1).grab(true, true).applyTo(bottom);
		GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.TOP).applyTo(buttonComposite);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(copyTable);
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
