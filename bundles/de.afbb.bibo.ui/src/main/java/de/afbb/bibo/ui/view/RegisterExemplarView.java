package de.afbb.bibo.ui.view;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.nebula.widgets.xviewer.XViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

import de.afbb.bibo.databinding.BindingHelper;
import de.afbb.bibo.share.model.Copy;
import de.afbb.bibo.ui.Activator;
import de.afbb.bibo.ui.provider.BiboXViewerFactory;
import de.afbb.bibo.ui.provider.CopyLabelProvider;
import de.afbb.bibo.ui.provider.CopyTreeContentProvider;

/**
 * this view allows the registration of new exemplars
 * 
 * @author dbecker
 */
public class RegisterExemplarView extends AbstractEditView {

	private static final String REGISTER_COPY = "register.copy";
	public static final String ID = "de.afbb.bibo.ui.registerexemplar";//$NON-NLS-1$
	private static final String IMAGE_GROUP = "icons/16x16medien.png";//$NON-NLS-1$

	private final Set<Set<Copy>> copies = new HashSet<Set<Copy>>();
	private final Copy copyToModify = new Copy();
	private Group idGroup;
	private Text txtBarcode;
	private Text txtIsbn;
	private Text txtEdition;
	private Text txtTitle;
	private Text txtAuthor;
	private Text txtLanguage;
	private Text txtPublisher;

	Listener toListListener = new Listener() {

		@Override
		public void handleEvent(final Event event) {
			System.err.println("to list");
		}
	};
	Listener toEditListener = new Listener() {

		@Override
		public void handleEvent(final Event event) {
			System.err.println("to edit");
		}
	};

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
		txtBarcode = toolkit.createText(idGroup, "");
		toolkit.createLabel(idGroup, "ISBN");
		txtIsbn = toolkit.createText(idGroup, "");
		toolkit.createLabel(idGroup, "Auflage");
		txtEdition = toolkit.createText(idGroup, "");

		final Group mediumGroup = createGroup(top, "Informationen");
		mediumGroup.setLayout(new GridLayout(4, false));
		toolkit.createLabel(mediumGroup, "Titel");
		txtTitle = toolkit.createText(mediumGroup, "Titel");
		toolkit.createLabel(mediumGroup, "Autor");
		txtAuthor = toolkit.createText(mediumGroup, "Autor");
		toolkit.createLabel(mediumGroup, "Sprache");
		txtLanguage = toolkit.createText(mediumGroup, "Sprache");
		toolkit.createLabel(mediumGroup, "Verlag");
		txtPublisher = toolkit.createText(mediumGroup, "Verlag");
		toolkit.createLabel(mediumGroup, "Typ");
		toolkit.createText(mediumGroup, "Typ");

		final Composite middle = toolkit.createComposite(top, SWT.NONE);
		middle.setLayout(new GridLayout(2, false));
		final Button btnToList = toolkit.createButton(middle, "In Liste übernehmen", SWT.NONE);
		btnToList.addListener(SWT.MouseDown, toListListener);
		final Button btnToEdit = toolkit.createButton(middle, "In Beareitung übernehmen", SWT.NONE);
		btnToEdit.addListener(SWT.MouseDown, toEditListener);

		final Composite bottom = toolkit.createComposite(top, SWT.NONE);
		bottom.setLayout(new GridLayout(2, false));
		final GridData layoutDataMiddle = new GridData(SWT.CENTER, SWT.DEFAULT, true, false);
		layoutDataMiddle.horizontalSpan = 2;
		bottom.setLayoutData(layoutDataMiddle);

		final XViewer xViewer = new XViewer(bottom, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION, new BiboXViewerFactory(REGISTER_COPY));
		xViewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		xViewer.setContentProvider(new CopyTreeContentProvider());
		xViewer.setLabelProvider(new CopyLabelProvider(xViewer));

		final Composite buttonComposite = toolkit.createComposite(bottom, SWT.NONE);
		buttonComposite.setLayout(new GridLayout());
		final Button btnGroup = toolkit.createButton(buttonComposite, "Medien Gruppieren", SWT.TOP);
		btnGroup.setImage(JFaceResources.getImage(IMAGE_GROUP));
		final Button btnUngroup = toolkit.createButton(buttonComposite, "Gruppierung Lösen", SWT.TOP);

		GridDataFactory.fillDefaults().applyTo(idGroup);
		GridDataFactory.fillDefaults().span(2, 1).align(SWT.CENTER, SWT.CENTER).grab(true, false).applyTo(middle);
		GridDataFactory.fillDefaults().applyTo(mediumGroup);
		GridDataFactory.fillDefaults().span(2, 1).grab(true, true).applyTo(bottom);
		GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.TOP).applyTo(buttonComposite);
	}

	@Override
	protected void initBinding() {
		BindingHelper.bindStringToTextField(txtBarcode, copyToModify, Copy.class, Copy.FIELD_BARCODE, bindingContext, true);
		BindingHelper.bindStringToTextField(txtIsbn, copyToModify, Copy.class, Copy.FIELD_ISBN, bindingContext, false);
		BindingHelper.bindStringToTextField(txtEdition, copyToModify, Copy.class, Copy.FIELD_EDITION, bindingContext, false);
		BindingHelper.bindStringToTextField(txtTitle, copyToModify, Copy.class, Copy.FIELD_TITLE, bindingContext, false);
		BindingHelper.bindStringToTextField(txtAuthor, copyToModify, Copy.class, Copy.FIELD_AUTHOR, bindingContext, false);
		BindingHelper.bindStringToTextField(txtLanguage, copyToModify, Copy.class, Copy.FIELD_LANGUAGE, bindingContext, false);
		BindingHelper.bindStringToTextField(txtPublisher, copyToModify, Copy.class, Copy.FIELD_PUBLISHER, bindingContext, false);
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
