package de.afbb.bibo.aggregation;

import java.net.ConnectException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;

import de.afbb.bibo.servletclient.ServiceLocator;
import de.afbb.bibo.share.IBorrowerService;
import de.afbb.bibo.share.IMediumService;
import de.afbb.bibo.share.callback.EventListener;
import de.afbb.bibo.share.model.Borrower;
import de.afbb.bibo.share.model.Medium;
import de.afbb.bibo.share.model.NavigationTreeNodeType;

public class NavigationTreeService implements EventListener {

	private final IBorrowerService borrowerService = ServiceLocator.getInstance().getBorrowerService();
	private final IMediumService mediumService = ServiceLocator.getInstance().getMediumService();

	private NavigationTreeViewNode borrowersRoot;
	private NavigationTreeViewNode mediaRoot;
	private final TreeViewer viewer;

	public NavigationTreeService(final TreeViewer viewer) throws ConnectException {
		this.viewer = viewer;
		ServiceLocator.getInstance().getLoginService().register(this);
		ServiceLocator.getInstance().getCopyService().register(this);
		borrowerService.register(this);
	}

	public void reloadBorrowers() throws ConnectException {
		borrowersRoot = new NavigationTreeViewNode("Ausleiher", null, NavigationTreeNodeType.PERSONS);
		loadBorrowers(borrowersRoot);
		setInput();
	}

	public void reloadCopies() throws ConnectException {
		mediaRoot = new NavigationTreeViewNode("Bücher", null, NavigationTreeNodeType.BOOKS);
		loadCopies(mediaRoot);
		setInput();
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
			if (medium != null) {
				final NavigationTreeViewNode mediumNode = new NavigationTreeViewNode(medium.getTitle(), medium,
						NavigationTreeNodeType.BOOK);
				root.addChild(mediumNode);
			}
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

	@Override
	public void invalidate(final NavigationTreeNodeType type) {
		try {
			if (NavigationTreeNodeType.ROOT.equals(type)) {
				reloadBorrowers();
				reloadCopies();
			} else if (NavigationTreeNodeType.PERSONS.equals(type)) {
				reloadBorrowers();
			}
			if (NavigationTreeNodeType.BOOKS.equals(type)) {
				reloadCopies();
			}
		} catch (final ConnectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setInput() {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				if (viewer != null && viewer.getTree() != null && !viewer.getTree().isDisposed()) {
					viewer.setInput(getRoot());
				}
				viewer.refresh();
			}
		});
	}
}
