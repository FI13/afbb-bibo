package de.afbb.bibo.servletclient.connection;

import java.net.ConnectException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import de.afbb.bibo.servletclient.ServiceLocator;
import de.afbb.bibo.share.ICopyService;
import de.afbb.bibo.share.callback.EventListener;
import de.afbb.bibo.share.model.Borrower;
import de.afbb.bibo.share.model.Copy;
import de.afbb.bibo.share.model.Curator;
import de.afbb.bibo.share.model.Medium;
import de.afbb.bibo.share.model.NavigationTreeNodeType;

public class CopyServiceImpl implements ICopyService {

	private final Set<EventListener> listeners = new HashSet<EventListener>();

	@Override
	public void update(final Copy copy) throws ConnectException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean exists(final String barcode) throws ConnectException {
		final Map<String, String> param = new HashMap<String, String>();
		param.put("barcode", barcode);
		final HttpResponse resp = ServerConnection.getInstance().request("/stock/existCopy", "GET", param, null);
		if (resp.getStatus() == HttpServletResponse.SC_OK) {
			return "1".equals(resp.getData());
		} else {
			throw Utils.createExceptionForCode(resp.getStatus());
		}
	}

	@Override
	public Copy get(final String barcode) throws ConnectException {
		final Map<String, String> param = new HashMap<String, String>();
		param.put("barcode", barcode);
		final HttpResponse resp = ServerConnection.getInstance().request("/stock/getCopy", "GET", param, null);
		if (resp.getStatus() == HttpServletResponse.SC_OK) {
			final Copy copy = Utils.gson.fromJson(resp.getData(), Copy.class);
			if (copy != null) {
				// we only get the id for sub-entities filled, so we need to
				// fetch them separately
				Borrower borrower = copy.getBorrower();
				if (borrower != null) {
					copy.setBorrower(ServiceLocator.getInstance().getBorrowerService().get(borrower.getId()));
				}
				borrower = copy.getLastBorrower();
				if (borrower != null) {
					copy.setLastBorrower(ServiceLocator.getInstance().getBorrowerService().get(borrower.getId()));
				}
				Curator curator = copy.getCurator();
				if (curator != null) {
					copy.setCurator(ServiceLocator.getInstance().getCuratorService().get(curator.getId()));
				}
				curator = copy.getLastCurator();
				if (curator != null) {
					copy.setLastCurator(ServiceLocator.getInstance().getCuratorService().get(curator.getId()));
				}
				copy.getMedium()
						.setType(ServiceLocator.getInstance().getTypService().get(copy.getMedium().getType().getId()));
			}
			return copy;
		} else if (resp.getStatus() == HttpServletResponse.SC_NOT_FOUND) {
			return null;
		} else {
			throw Utils.createExceptionForCode(resp.getStatus());
		}
	}

	@Override
	public Collection<Copy> getGroupedCopies(final Integer id) throws ConnectException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerCopies(final Collection<Copy> copies) throws ConnectException {
		// separate copies into single copies and grouped copies
		final Map<Integer, Set<Copy>> separatedByGroup = new HashMap<Integer, Set<Copy>>();
		Integer groupId;
		Set<Copy> group;
		for (final Copy copy : copies) {
			groupId = copy.getGroupId();
			final boolean contains = separatedByGroup.containsKey(groupId);
			group = contains ? separatedByGroup.get(groupId) : new HashSet<Copy>();
			group.add(copy);
			// this step is only needed when list wasn't in map, so we can safe
			// some computing time here
			if (!contains) {
				separatedByGroup.put(groupId, group);
			}
		}
		final Iterator<Integer> it = separatedByGroup.keySet().iterator();
		int statusCode = -1;
		while (it.hasNext()) {
			groupId = it.next();
			final String url = groupId > -1 ? "/stock/addGroupedCopies" : "/stock/addCopies";
			final Set<Copy> groupedCopies = separatedByGroup.get(groupId);
			// we send every group as one request
			final HttpResponse resp = ServerConnection.getInstance().request(url, "POST", null,
					Utils.gson.toJson(groupedCopies));
			if (resp.getStatus() != HttpServletResponse.SC_OK) {
				// if we get any other code than 200 we override previous, but
				// we continue with other groups
				statusCode = resp.getStatus();
			}
		}
		notifyListener(NavigationTreeNodeType.MEDIA);
		// if we got an error we throw an exception
		if (statusCode > 0) {
			throw new ConnectException("Wrong status code. Recieved was: " + statusCode);
		}

	}

	@Override
	public void returnCopies(final Collection<Copy> copies) throws ConnectException {
		int statusCode = -1;
		for (final Copy copy : copies) {
			final Map<String, String> param = new HashMap<String, String>();
			param.put("barcode", copy.getBarcode());
			param.put("condition", copy.getBarcode());
			param.put("condition", copy.getCondition());
			final HttpResponse resp = ServerConnection.getInstance().request("/borrow/return", "GET", param, null);
			if (resp.getStatus() != HttpServletResponse.SC_OK) {
				statusCode = resp.getStatus();
			}
		}
		notifyListener(NavigationTreeNodeType.ROOT);
		// if we got an error we throw an exception
		if (statusCode > 0) {
			throw new ConnectException("Wrong status code. Recieved was: " + statusCode);
		}

	}

	@Override
	public void lendCopies(final Collection<Copy> copies, final boolean printList) throws ConnectException {
		final Map<String, String> param = new HashMap<String, String>();
		int statusCode = -1;
		for (final Copy copy : copies) {
			param.put("barcode", copy.getBarcode());
			param.put("borrower", copy.getBorrower().getId().toString());
			param.put("condition", copy.getCondition());
			final HttpResponse resp = ServerConnection.getInstance().request("/borrow/doBorrow", "GET", param, null);
			if (resp.getStatus() != HttpServletResponse.SC_OK) {
				// if we get any other code than 200 we override previous, but
				// we continue with other copies
				statusCode = resp.getStatus();
			}
		}
		notifyListener(NavigationTreeNodeType.ROOT);
		// if we got an error we throw an exception
		if (statusCode > 0) {
			throw new ConnectException("Wrong status code. Recieved was: " + statusCode);
		}
		// TODO implement printing here
	}

	@Override
	public void doInventory(final Collection<Copy> copies) throws ConnectException {
		final Map<String, String> param = new HashMap<String, String>();
		int statusCode = -1;
		for (final Copy copy : copies) {
			param.put("barcode", copy.getBarcode());
			param.put("condition", copy.getCondition());
			final HttpResponse resp = ServerConnection.getInstance().request("/stock/physicalInventory", "GET", param,
					null);
			if (resp.getStatus() != HttpServletResponse.SC_OK) {
				// if we get any other code than 200 we override previous, but
				// we continue with other copies
				statusCode = resp.getStatus();
			}
		}
		// if we got an error we throw an exception
		if (statusCode > 0) {
			throw new ConnectException("Wrong status code. Recieved was: " + statusCode);
		}
	}

	@Override
	public Collection<Copy> listAll() throws ConnectException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Copy> listCopies(final Medium medium) throws ConnectException {
		final Map<String, String> param = new HashMap<String, String>();
		param.put("id", medium.getMediumId().toString());
		final HttpResponse resp = ServerConnection.getInstance().request("/stock/listCopies", "GET", param, null);
		if (resp.getStatus() == HttpServletResponse.SC_OK) {
			final Collection<Copy> result = new HashSet<>();
			final String[] data = resp.getData().split("\n");
			for (int i = 0; i < data.length; i++) {
				final Copy copy = Utils.gson.fromJson(data[i], Copy.class);
				if (copy != null) {
					/*
					 * we only get the id for sub-entities filled, so we need to
					 * fetch them separately
					 */
					Borrower borrower = copy.getBorrower();
					if (borrower != null) {
						copy.setBorrower(ServiceLocator.getInstance().getBorrowerService().get(borrower.getId()));
					}
					borrower = copy.getLastBorrower();
					if (borrower != null) {
						copy.setLastBorrower(ServiceLocator.getInstance().getBorrowerService().get(borrower.getId()));
					}
					Curator curator = copy.getCurator();
					if (curator != null) {
						copy.setCurator(ServiceLocator.getInstance().getCuratorService().get(curator.getId()));
					}
					curator = copy.getLastCurator();
					if (curator != null) {
						copy.setLastCurator(ServiceLocator.getInstance().getCuratorService().get(curator.getId()));
					}
					copy.getMedium().setType(
							ServiceLocator.getInstance().getTypService().get(copy.getMedium().getType().getId()));
					result.add(copy);
				}
			}
			return result;
		} else {
			throw Utils.createExceptionForCode(resp.getStatus());
		}
	}

	@Override
	public void delete(final Copy copy) throws ConnectException {
		// TODO Auto-generated method stub

	}

	@Override
	public void register(final EventListener listener) {
		listeners.add(listener);
	}

	private void notifyListener(final NavigationTreeNodeType type) {
		for (final EventListener eventListener : listeners) {
			eventListener.invalidate(type);
		}
	}

}
