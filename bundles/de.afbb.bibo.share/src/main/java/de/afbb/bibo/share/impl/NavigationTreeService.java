package de.afbb.bibo.share.impl;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Iterator;

import de.afbb.bibo.share.IBorrowerService;
import de.afbb.bibo.share.ServiceLocator;
import de.afbb.bibo.share.model.Borrower;

public class NavigationTreeService {

	private final IBorrowerService borrowerService = ServiceLocator.getInstance().getBorrowerService();

	private NavigationTreeViewNode borrowersRoot;
	private NavigationTreeViewNode copiesRoot;

	public NavigationTreeService() throws ConnectException {
		borrowersRoot = new NavigationTreeViewNode("Ausleiher", NavigationTreeNodeType.PERSONS);
		copiesRoot = new NavigationTreeViewNode("Bücher", NavigationTreeNodeType.BOOKS);
		loadBorrowers(borrowersRoot);
		loadCopies(copiesRoot);
	}

	public void reloadBorrowers() throws ConnectException {
		borrowersRoot = new NavigationTreeViewNode("Ausleiher", NavigationTreeNodeType.PERSONS);
		loadBorrowers(borrowersRoot);
	}

	public void reloadCopies() {
		copiesRoot = new NavigationTreeViewNode("Bücher", NavigationTreeNodeType.BOOKS);
		loadCopies(copiesRoot);
	}

	public NavigationTreeViewNode getRoot() {
		final NavigationTreeViewNode root = new NavigationTreeViewNode("");
		root.addChild(borrowersRoot);
		root.addChild(copiesRoot);
		return root;
	}

	private void loadCopies(final NavigationTreeViewNode root) {
		// TODO: Implement
	}

	private void loadBorrowers(final NavigationTreeViewNode root) throws ConnectException {
		final HashMap<String, NavigationTreeViewNode> groups = new HashMap<String, NavigationTreeViewNode>();

		final Iterator<Borrower> borrowerIterator = borrowerService.listAll().iterator();
		while (borrowerIterator.hasNext()) {
			final Borrower borrower = borrowerIterator.next();
			NavigationTreeViewNode personsNode;
			NavigationTreeViewNode pupilNode;

			if (groups.containsKey(borrower.getInfo())) {
				personsNode = groups.get(borrower.getInfo());
			} else {
				personsNode = new NavigationTreeViewNode(borrower.getInfo(), NavigationTreeNodeType.PERSONS);
				groups.put(borrower.getInfo(), personsNode);
			}

			pupilNode = new NavigationTreeViewNode(borrower.getFirstName() + " " + borrower.getSurname(), NavigationTreeNodeType.PERSON);

			personsNode.addChild(pupilNode);
		}

		final Iterator<NavigationTreeViewNode> groupsIterator = groups.values().iterator();
		while (groupsIterator.hasNext()) {
			final NavigationTreeViewNode group = groupsIterator.next();

			root.addChild(group);
		}
	}
}
