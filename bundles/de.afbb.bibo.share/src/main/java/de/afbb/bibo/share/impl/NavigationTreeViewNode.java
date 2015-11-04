package de.afbb.bibo.share.impl;

import java.util.ArrayList;

public class NavigationTreeViewNode {

	private final String name;
	private NavigationTreeViewNode parent;
	private NavigationTreeNodeType type;

	private final ArrayList<NavigationTreeViewNode> children = new ArrayList<NavigationTreeViewNode>();

	public NavigationTreeViewNode(final String name) {
		this.name = name;
	}

	public NavigationTreeViewNode(final String name, final NavigationTreeNodeType type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setParent(final NavigationTreeViewNode parent) {
		this.parent = parent;
	}

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

	public NavigationTreeViewNode[] getChildren() {
		return children.toArray(new NavigationTreeViewNode[children.size()]);
	}

	public boolean hasChildren() {
		return children.size() > 0;
	}

	@Override
	public String toString() {
		return getName();
	}
}