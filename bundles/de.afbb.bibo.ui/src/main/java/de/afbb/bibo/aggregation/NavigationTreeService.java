package de.afbb.bibo.aggregation;

import java.net.ConnectException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;

import de.afbb.bibo.servletclient.ServiceLocator;
import de.afbb.bibo.share.IAggregationService;
import de.afbb.bibo.share.IBorrowerService;
import de.afbb.bibo.share.IMediumService;
import de.afbb.bibo.share.callback.EventListener;
import de.afbb.bibo.share.callback.IAggregatorTarget;
import de.afbb.bibo.share.model.Borrower;
import de.afbb.bibo.share.model.Medium;
import de.afbb.bibo.share.model.NavigationTreeNodeType;

public class NavigationTreeService implements EventListener {

	private static final String SUM = "∑";//$NON-NLS-1$
	private static final String UP = "↑";//$NON-NLS-1$
	private static final String MEDIA_INFORMATION = " [" + SUM + ":%d, " + UP + "%d]";//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	private final IBorrowerService borrowerService = ServiceLocator.getInstance().getBorrowerService();
	private final IMediumService mediumService = ServiceLocator.getInstance().getMediumService();
	private final IAggregationService aggregationService = ServiceLocator.getInstance().getAggregationService();

	private NavigationTreeViewNode borrowersRoot;
	private NavigationTreeViewNode mediaRoot;
	private final TreeViewer viewer;

	public NavigationTreeService(final TreeViewer viewer) throws ConnectException {
		this.viewer = viewer;
		ServiceLocator.getInstance().getLoginService().register(this);
		ServiceLocator.getInstance().getCopyService().register(this);
		aggregationService.register(this);
		borrowerService.register(this);
	}

	public void reloadBorrowers() throws ConnectException {
		borrowersRoot = new NavigationTreeViewNode("Ausleiher", null, NavigationTreeNodeType.PERSONS);
		loadBorrowers(borrowersRoot);
		setInput();
	}

	public void reloadCopies() throws ConnectException {
		mediaRoot = new NavigationTreeViewNode("Medien", null, NavigationTreeNodeType.BOOKS);
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
		mediaRoot.setInformation(null);
		final Collection<Medium> media = mediumService.list();
		final Iterator<Medium> mediaIterator = media.iterator();
		while (mediaIterator.hasNext()) {
			final Medium medium = mediaIterator.next();
			if (medium != null) {
				final NavigationTreeViewNode mediumNode = new NavigationTreeViewNode(medium.getTitle(), medium,
						NavigationTreeNodeType.BOOK);
				final Job job = new Job("Lade Informationen") {

					@Override
					protected IStatus run(final IProgressMonitor monitor) {
						aggregationService.aggregateMediumInformation(medium.getMediumId(), mediumNode);
						return Status.OK_STATUS;
					}

				};
				job.schedule();
				root.addChild(mediumNode);
			}
		}

	}

	private void loadBorrowers(final NavigationTreeViewNode root) throws ConnectException {
		borrowersRoot.setInformation(null);
		final HashMap<String, NavigationTreeViewNode> groups = new HashMap<String, NavigationTreeViewNode>();

		final Iterator<Borrower> borrowerIterator = borrowerService.listAll().iterator();
		while (borrowerIterator.hasNext()) {
			final Borrower borrower = borrowerIterator.next();
			NavigationTreeViewNode personsNode;
			final NavigationTreeViewNode pupilNode;

			if (groups.containsKey(borrower.getInfo())) {
				personsNode = groups.get(borrower.getInfo());
			} else {
				personsNode = new NavigationTreeViewNode(borrower.getInfo(), null, NavigationTreeNodeType.PERSONS);
				personsNode.setInformation(null);
				groups.put(borrower.getInfo(), personsNode);
			}

			pupilNode = new NavigationTreeViewNode(borrower.getForename() + " " + borrower.getSurname(), borrower,
					NavigationTreeNodeType.PERSON);
			final Job job = new Job("Lade Informationen") {

				@Override
				protected IStatus run(final IProgressMonitor monitor) {
					aggregationService.aggregateBorrowerInformation(borrower.getId(), pupilNode);
					return Status.OK_STATUS;
				}

			};
			job.schedule();

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

	@Override
	public void update(final IAggregatorTarget target, final String[] information) {
		target.setInformation(information);
		setInput();
	}

}
