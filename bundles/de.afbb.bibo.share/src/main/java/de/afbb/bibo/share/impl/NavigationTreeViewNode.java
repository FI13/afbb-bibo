package de.afbb.bibo.share.impl;

import java.util.ArrayList;

import org.eclipse.jface.viewers.TreeNode;

public class NavigationTreeViewNode extends TreeNode {

	private String title;
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
		children.add(child);
		child.setParent(this);
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
		return children.size() > 0;
	}
}