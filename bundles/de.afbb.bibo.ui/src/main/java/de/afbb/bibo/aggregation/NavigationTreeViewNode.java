package de.afbb.bibo.aggregation;

import java.util.ArrayList;

import org.eclipse.jface.viewers.TreeNode;

import de.afbb.bibo.share.callback.IAggregatorTarget;
import de.afbb.bibo.share.model.NavigationTreeNodeType;

public class NavigationTreeViewNode extends TreeNode implements IAggregatorTarget {

	private static final String NEW_LINE = "\n";//$NON-NLS-1$
	private String title;
	private String information[];
	private NavigationTreeViewNode parent;
	private final NavigationTreeNodeType type;

	private final ArrayList<NavigationTreeViewNode> children = new ArrayList<NavigationTreeViewNode>();

	public NavigationTreeViewNode(final String title, final Object value, final NavigationTreeNodeType type) {
		super(value);
		this.title = title;
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String[] getInformation() {
		return information;
	}

	public String getInformationText() {
		if (information != null) {
			final String info0 = information[0];
			// don't show information text when nothing interesting is there
			if (!"0".equals(info0)) {//$NON-NLS-1$
				if (NavigationTreeNodeType.BOOK.equals(type)) {
					return String.format("[∑:%s, ↑:%s]", info0, information[1]);//$NON-NLS-1$
				} else if (NavigationTreeNodeType.PERSON.equals(type)) {
					return String.format("[↑:%s]", info0);//$NON-NLS-1$
				}
			}
		}
		return "";//$NON-NLS-1$
	}

	public String getTooltipText() {
		final StringBuilder tooltip = new StringBuilder(title);
		if (information != null) {
			tooltip.append(NEW_LINE);
			if (NavigationTreeNodeType.BOOK.equals(type)) {
				tooltip.append("Gesamt: ");
				tooltip.append(information[0]);
				tooltip.append(NEW_LINE);
				tooltip.append("Ausgeliehen: ");
				tooltip.append(information[1]);
			} else if (NavigationTreeNodeType.PERSON.equals(type)) {
				tooltip.append("Ausgeliehen: ");
				tooltip.append(information[0]);
			}
		}
		return tooltip.toString();
	}

	@Override
	public void setInformation(final String[] information) {
		this.information = information;
	}

	public void setParent(final NavigationTreeViewNode parent) {
		this.parent = parent;
	}

	@Override
	public NavigationTreeViewNode getParent() {
		return parent;
	}

	public boolean hasType() {
		return type != null;
	}

	public NavigationTreeNodeType getType() {
		return type;
	}

	public void addChild(final NavigationTreeViewNode child) {
		// children should never contain null elements
		if (child != null) {
			children.add(child);
			child.setParent(this);
		}
	}

	public void removeChild(final NavigationTreeViewNode child) {
		children.remove(child);
		child.setParent(null);
	}

	@Override
	public NavigationTreeViewNode[] getChildren() {
		return children.toArray(new NavigationTreeViewNode[children.size()]);
	}

	@Override
	public boolean hasChildren() {
		return !children.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (parent == null ? 0 : parent.hashCode());
		result = prime * result + (title == null ? 0 : title.hashCode());
		result = prime * result + (type == null ? 0 : type.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof NavigationTreeViewNode)) {
			return false;
		}
		final NavigationTreeViewNode other = (NavigationTreeViewNode) obj;
		if (parent == null) {
			if (other.parent != null) {
				return false;
			}
		} else if (!parent.equals(other.parent)) {
			return false;
		}
		if (title == null) {
			if (other.title != null) {
				return false;
			}
		} else if (!title.equals(other.title)) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		return true;
	}

}