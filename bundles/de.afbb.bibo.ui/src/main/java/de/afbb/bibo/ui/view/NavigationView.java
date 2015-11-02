package de.afbb.bibo.ui.view;

import java.util.ArrayList;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import de.afbb.bibo.ui.Activator;

public class NavigationView extends ViewPart {

	public static final String ID = "de.afbb.bibo.ui.navigationView";
	private TreeViewer viewer;

	private final Image iconBookGroup = Activator.getImageDescriptor("icons/16x16book.png").createImage();
	private final Image iconBook = Activator.getImageDescriptor("icons/16x16book2.png").createImage();
	private final Image iconPupil = Activator.getImageDescriptor("icons/16x16schueler.png").createImage();
	private final Image iconTeacher = Activator.getImageDescriptor("icons/16x16teacher.png").createImage();
	private final Image iconUserGroup = Activator.getImageDescriptor("icons/16x16user.png").createImage();
	private final Image iconCd = Activator.getImageDescriptor("icons/16x16cd.png").createImage();

	/*
	 * .. Personen
	 * .... Lehrer
	 * ...... Männlich
	 * ...... Weiblich
	 * .... Klasse
	 * ...... Männlich
	 * ...... Weiblich
	 * .. Bücher
	 * .... Buch
	 */
	class TreeObject {

		private final String name;
		private TreeParent parent;
		private Image icon;

		public TreeObject(final String name) {
			this.name = name;
		}

		public TreeObject(final String name, final Image icon) {
			this.name = name;
			this.icon = icon;
		}

		public String getName() {
			return name;
		}

		public void setParent(final TreeParent parent) {
			this.parent = parent;
		}

		public TreeParent getParent() {
			return parent;
		}

		public boolean hasIcon() {
			return icon != null;
		}

		public Image getIcon() {
			return icon;
		}

		@Override
		public String toString() {
			return getName();
		}
	}

	class TreeParent extends TreeObject {

		private final ArrayList<TreeObject> children;

		public TreeParent(final String name) {
			super(name);
			children = new ArrayList<TreeObject>();
		}

		public TreeParent(final String name, final Image icon) {
			super(name, icon);
			children = new ArrayList<TreeObject>();
		}

		public void addChild(final TreeObject child) {
			children.add(child);
			child.setParent(this);
		}

		public void removeChild(final TreeObject child) {
			children.remove(child);
			child.setParent(null);
		}

		public TreeObject[] getChildren() {
			return children.toArray(new TreeObject[children.size()]);
		}

		public boolean hasChildren() {
			return children.size() > 0;
		}
	}

	class ViewContentProvider implements IStructuredContentProvider, ITreeContentProvider {

		@Override
		public void inputChanged(final Viewer v, final Object oldInput, final Object newInput) {
		}

		@Override
		public void dispose() {
		}

		@Override
		public Object[] getElements(final Object parent) {
			return getChildren(parent);
		}

		@Override
		public Object getParent(final Object child) {
			if (child instanceof TreeObject) {
				return ((TreeObject) child).getParent();
			}
			return null;
		}

		@Override
		public Object[] getChildren(final Object parent) {
			if (parent instanceof TreeParent) {
				return ((TreeParent) parent).getChildren();
			}
			return new Object[0];
		}

		@Override
		public boolean hasChildren(final Object parent) {
			if (parent instanceof TreeParent) {
				return ((TreeParent) parent).hasChildren();
			}
			return false;
		}
	}

	class ViewLabelProvider extends LabelProvider {

		@Override
		public String getText(final Object obj) {
			return obj.toString();
		}

		@Override
		public Image getImage(final Object obj) {
			if (obj instanceof TreeObject && ((TreeObject) obj).hasIcon()) {
				return ((TreeObject) obj).getIcon();
			} else {
				String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
				if (obj instanceof TreeParent) {
					imageKey = ISharedImages.IMG_OBJ_FOLDER;
				}
				return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
			}
		}
	}

	/**
	 * We will set up a dummy model to initialize tree hierarchy. In real
	 * code, you will connect to a real model and expose its hierarchy.
	 */
	private TreeObject createDummyModel() {

		final TreeParent top1 = new TreeParent("Personen", iconUserGroup);
		final TreeParent top2 = new TreeParent("Bücher", iconBookGroup);

		final TreeParent pg1 = new TreeParent("Lehrer", iconUserGroup);
		final TreeParent pg2 = new TreeParent("FI13", iconUserGroup);
		final TreeParent pg3 = new TreeParent("FI15", iconUserGroup);

		final TreeObject b1 = new TreeObject("IT-Handbuch", iconBook);
		final TreeObject b2 = new TreeObject("PONS", iconBook);
		final TreeObject b3 = new TreeObject("Englisch CD", iconCd);

		final TreeObject p1 = new TreeObject("Hr. Henoch", iconTeacher);
		final TreeObject p2 = new TreeObject("Hr. Strecke", iconTeacher);
		final TreeObject p3 = new TreeObject("Fr. Schwarz", iconTeacher);
		final TreeObject p4 = new TreeObject("Philipp Widdra", iconPupil);
		final TreeObject p5 = new TreeObject("Michéle Lingel", iconPupil);

		top1.addChild(pg1);
		top1.addChild(pg2);
		top1.addChild(pg3);

		top2.addChild(b1);
		top2.addChild(b2);
		top2.addChild(b3);

		pg1.addChild(p1);
		pg1.addChild(p2);
		pg1.addChild(p3);
		pg2.addChild(p4);
		pg2.addChild(p5);

		final TreeParent root = new TreeParent("");
		root.addChild(top1);
		root.addChild(top2);
		return root;
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(final Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(createDummyModel());
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}