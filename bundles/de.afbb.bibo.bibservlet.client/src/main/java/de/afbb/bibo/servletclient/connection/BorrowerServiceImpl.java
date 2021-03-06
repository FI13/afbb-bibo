package de.afbb.bibo.servletclient.connection;

import java.net.ConnectException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import de.afbb.bibo.servletclient.ServiceLocator;
import de.afbb.bibo.share.IBorrowerService;
import de.afbb.bibo.share.callback.EventListener;
import de.afbb.bibo.share.model.Borrower;
import de.afbb.bibo.share.model.Copy;
import de.afbb.bibo.share.model.Curator;
import de.afbb.bibo.share.model.NavigationTreeNodeType;

/**
 * Implementation of HTTP service for {@link Borrower}
 *
 * @author David Becker
 *
 */
public class BorrowerServiceImpl implements IBorrowerService {

	private final Map<Integer, Borrower> cache = new HashMap<Integer, Borrower>();
	private final Set<EventListener> listeners = new HashSet<EventListener>();

	@Override
	public boolean exists(final String firstName, final String surname) throws ConnectException {
		boolean exists = false;
		final Map<String, String> param = new HashMap<String, String>();
		param.put("forename", firstName);
		param.put("surname", surname);
		final HttpResponse resp = ServerConnection.getInstance().request("/user/borrowerExists", "GET", param, null);
		if (resp.getStatus() == HttpServletResponse.SC_OK) {
			exists = "1".equals(resp.getData());
		} else {
			final ConnectException exception = Utils.createExceptionForCode(resp.getStatus());
			if (exception != null) {
				throw exception;
			}
		}
		return exists;
	}

	@Override
	public void create(final Borrower borrower) throws ConnectException {
		final HttpResponse resp = ServerConnection.getInstance().request("/user/newBorrower", "POST", null,
				Utils.gson.toJson(borrower));
		if (resp.getStatus() != HttpServletResponse.SC_OK) {
			final ConnectException exception = Utils.createExceptionForCode(resp.getStatus());
			if (exception != null) {
				throw exception;
			}
		}
		notifyListener(NavigationTreeNodeType.PERSONS);
	}

	@Override
	public Borrower get(final Integer id) throws ConnectException {
		if (id == null || id < 1) {
			return null;
		}
		synchronized (cache) {
			if (cache.containsKey(id)) {
				return cache.get(id);
			}
		}
		Borrower result = null;
		final Map<String, String> param = new HashMap<String, String>();
		param.put("id", id.toString());
		final HttpResponse resp = ServerConnection.getInstance().request("/user/getBorrower", "GET", param, null);
		if (resp.getStatus() == HttpServletResponse.SC_OK) {
			result = Utils.gson.fromJson(resp.getData(), Borrower.class);
			synchronized (cache) {
				cache.put(result.getId(), result);
			}
		} else if (resp.getStatus() != HttpServletResponse.SC_NOT_FOUND) {
			final ConnectException exception = Utils.createExceptionForCode(resp.getStatus());
			if (exception != null) {
				throw exception;
			}
		}
		return result;
	}

	@Override
	public void update(final Borrower borrower) throws ConnectException {
		synchronized (cache) {
			cache.remove(borrower.getId());
		}
		final HttpResponse resp = ServerConnection.getInstance().request("/user/updateBorrower", "POST", null,
				Utils.gson.toJson(borrower));
		if (resp.getStatus() != HttpServletResponse.SC_OK) {
			final ConnectException exception = Utils.createExceptionForCode(resp.getStatus());
			if (exception != null) {
				throw exception;
			}
		}
		notifyListener(NavigationTreeNodeType.PERSONS);
	}

	@Override
	public Collection<Borrower> listAll() throws ConnectException {
		final Collection<Borrower> result = new HashSet<>();
		final HttpResponse resp = ServerConnection.getInstance().request("/user/getBorrowers", "GET", null, null);
		if (resp.getStatus() == HttpServletResponse.SC_OK) {
			final String[] data = resp.getData().split("\n");
			synchronized (cache) {
				cache.clear();
				for (int i = 0; i < data.length; i++) {
					final Borrower borrower = Utils.gson.fromJson(data[i], Borrower.class);
					if (borrower != null) {
						result.add(borrower);
						cache.put(borrower.getId(), borrower);
					}
				}
			}
		} else {
			final ConnectException exception = Utils.createExceptionForCode(resp.getStatus());
			if (exception != null) {
				throw exception;
			}
		}
		return result;
	}

	@Override
	public Collection<Borrower> listOpen() throws ConnectException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Copy> listLent(final Borrower b) throws ConnectException {
		final Collection<Copy> result = new HashSet<>();
		if (b != null && b.getId() != null) {
			final Map<String, String> param = new HashMap<String, String>();
			param.put("id", b.getId().toString());
			final HttpResponse resp = ServerConnection.getInstance().request("/stock/listLendCopies", "GET", param,
					null);
			if (resp.getStatus() == HttpServletResponse.SC_OK) {
				final String[] data = resp.getData().split("\n");
				for (int i = 0; i < data.length; i++) {
					final Copy copy = Utils.gson.fromJson(data[i], Copy.class);
					if (copy != null) {
						/*
						 * we only get the id for sub-entities filled, so we
						 * need to fetch them separately
						 */
						Borrower borrower = copy.getBorrower();
						if (borrower != null) {
							copy.setBorrower(ServiceLocator.getInstance().getBorrowerService().get(borrower.getId()));
						}
						borrower = copy.getLastBorrower();
						if (borrower != null) {
							copy.setLastBorrower(
									ServiceLocator.getInstance().getBorrowerService().get(borrower.getId()));
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
			} else {
				final ConnectException exception = Utils.createExceptionForCode(resp.getStatus());
				if (exception != null) {
					throw exception;
				}
			}
		}
		return result;
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
