package de.afbb.bibo.share.impl;

import java.net.ConnectException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import de.afbb.bibo.share.IBorrowerService;
import de.afbb.bibo.share.IMediumService;
import de.afbb.bibo.share.ServiceLocator;
import de.afbb.bibo.share.model.Borrower;
import de.afbb.bibo.share.model.Medium;

public class NavigationTreeService {

	private final IBorrowerService borrowerService = ServiceLocator.getInstance().getBorrowerService();
	private final IMediumService mediumService = ServiceLocator.getInstance().getMediumService();

	private NavigationTreeViewNode borrowersRoot;
	private NavigationTreeViewNode mediaRoot;

	public NavigationTreeService() throws ConnectException {
		borrowersRoot = new NavigationTreeViewNode("Ausleiher", null, NavigationTreeNodeType.PERSONS);
		mediaRoot = new NavigationTreeViewNode("Bücher", null, NavigationTreeNodeType.BOOKS);
		loadBorrowers(borrowersRoot);
		loadCopies(mediaRoot);
	}

	public void reloadBorrowers() throws ConnectException {
		borrowersRoot = new NavigationTreeViewNode("Ausleiher", null, NavigationTreeNodeType.PERSONS);
		loadBorrowers(borrowersRoot);
	}

	public void reloadCopies() throws ConnectException {
		mediaRoot = new NavigationTreeViewNode("Bücher", null, NavigationTreeNodeType.BOOKS);
		loadCopies(mediaRoot);
	}

	public NavigationTreeViewNode getRoot() {
		final NavigationTreeViewNode root = new NavigationTreeViewNode("", null, NavigationTreeNodeType.ROOT);
		root.addChild(borrowersRoot);
		root.addChild(mediaRoot);
		return root;
	}

	private void loadCopies(final NavigationTreeViewNode root) throws ConnectException {
		final Collection<Medium> media = mediumService.list();
		final Iterator<Medium> mediaIterator = media.iterator();
		while (mediaIterator.hasNext()) {
			final Medium medium = mediaIterator.next();
			final NavigationTreeViewNode mediumNode = new NavigationTreeViewNode(medium.getTitle(), medium, NavigationTreeNodeType.BOOK);

			root.addChild(mediumNode);
		}

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
				personsNode = new NavigationTreeViewNode(borrower.getInfo(), null, NavigationTreeNodeType.PERSONS);
				groups.put(borrower.getInfo(), personsNode);
			}

			pupilNode = new NavigationTreeViewNode(borrower.getForename() + " " + borrower.getSurname(), borrower,
					NavigationTreeNodeType.PERSON);

			personsNode.addChild(pupilNode);
		}

		final Iterator<NavigationTreeViewNode> groupsIterator = groups.values().iterator();
		while (groupsIterator.hasNext()) {
			final NavigationTreeViewNode group = groupsIterator.next();

			root.addChild(group);
		}
	}
}
