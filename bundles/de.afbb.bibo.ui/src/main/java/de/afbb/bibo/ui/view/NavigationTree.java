package de.afbb.bibo.ui.view;

import java.util.ArrayList;

import org.eclipse.swt.graphics.Image;

import de.afbb.bibo.ui.Activator;

public class NavigationTree {

	private final Image iconBookCategoryNode = Activator.getImageDescriptor("icons/16x16book.png").createImage();
	private final Image iconBookNode = Activator.getImageDescriptor("icons/16x16book2.png").createImage();
	private final Image iconPupilNode = Activator.getImageDescriptor("icons/16x16schueler.png").createImage();
	private final Image iconTeacherNode = Activator.getImageDescriptor("icons/16x16teacher.png").createImage();
	private final Image iconPersonsNode = Activator.getImageDescriptor("icons/16x16user.png").createImage();
	private final Image iconCdNode = Activator.getImageDescriptor("icons/16x16cd.png").createImage();

	public class PersonsNode extends AbstractNode {

		public PersonsNode(final String name) {
			super(name, iconPersonsNode);
		}
	}

	public class PupilNode extends AbstractNode {

		public PupilNode(final String name) {
			super(name, iconPupilNode);
		}
	}

	public class TeacherNode extends AbstractNode {

		public TeacherNode(final String name) {
			super(name, iconTeacherNode);
		}
	}

	public class BookCategoryNode extends AbstractNode {

		public BookCategoryNode(final String name) {
			super(name, iconBookCategoryNode);
		}
	}

	public class BookNode extends AbstractNode {

		public BookNode(final String name) {
			super(name, iconBookNode);
		}
	}

	public class CdNode extends AbstractNode {

		public CdNode(final String name) {
			super(name, iconCdNode);
		}
	}

	public abstract class AbstractNode {

		private final String name;
		private AbstractNode parent;
		private Image icon;

		private final ArrayList<AbstractNode> children = new ArrayList<AbstractNode>();

		public AbstractNode(final String name) {
			this.name = name;
		}

		public AbstractNode(final String name, final Image icon) {
			this.name = name;
			this.icon = icon;
		}

		public String getName() {
			return name;
		}

		public void setParent(final AbstractNode parent) {
			this.parent = parent;
		}

		public AbstractNode getParent() {
			return parent;
		}

		public boolean hasIcon() {
			return icon != null;
		}

		public Image getIcon() {
			return icon;
		}

		public void addChild(final AbstractNode child) {
			children.add(child);
			child.setParent(this);
		}

		public void removeChild(final AbstractNode child) {
			children.remove(child);
			child.setParent(null);
		}

		public AbstractNode[] getChildren() {
			return children.toArray(new AbstractNode[children.size()]);
		}

		public boolean hasChildren() {
			return children.size() > 0;
		}

		@Override
		public String toString() {
			return getName();
		}
	}
}
