package de.afbb.bibo.servletclient.connection;

import java.net.ConnectException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.GsonBuilder;

import de.afbb.bibo.share.IBorrowerService;
import de.afbb.bibo.share.beans.BeanExclusionStrategy;
import de.afbb.bibo.share.callback.EventListener;
import de.afbb.bibo.share.model.Borrower;
import de.afbb.bibo.share.model.Copy;
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
		final Map<String, String> param = new HashMap<String, String>();
		param.put("forename", firstName);
		param.put("surname", surname);
		final HttpResponse resp = ServerConnection.getInstance().request("/user/borrowerExists", "GET", param, null);
		if (resp.getStatus() == HttpServletResponse.SC_OK) {
			return "1".equals(resp.getData());
		} else {
			throw new ConnectException("Wrong status code. Recieved was: " + resp.getStatus());
		}
	}

	@Override
	public void create(final Borrower borrower) throws ConnectException {
		final HttpResponse resp = ServerConnection.getInstance().request("/user/newBorrower", "POST", null,
				Utils.gson.toJson(borrower));
		if (resp.getStatus() != HttpServletResponse.SC_OK) {
			throw new ConnectException("Wrong status code. Recieved was: " + resp.getStatus());
		}
		notifyListener(NavigationTreeNodeType.PERSONS);
	}

	@Override
	public Borrower get(final Integer id) throws ConnectException {
		synchronized (cache) {
			if (cache.containsKey(id)) {
				return cache.get(id);
			}
		}
		final Map<String, String> param = new HashMap<String, String>();
		param.put("id", id.toString());
		final HttpResponse resp = ServerConnection.getInstance().request("/user/getBorrower", "GET", param, null);
		if (resp.getStatus() == HttpServletResponse.SC_OK) {
			final Borrower borrower = Utils.gson.fromJson(resp.getData(), Borrower.class);
			synchronized (cache) {
				cache.put(borrower.getId(), borrower);
			}
			return borrower;
		} else if (resp.getStatus() == HttpServletResponse.SC_NOT_FOUND) {
			return null;
		} else {
			throw new ConnectException("Wrong status code. Recieved was: " + resp.getStatus());
		}
	}

	@Override
	public void update(final Borrower borrower) throws ConnectException {
		final GsonBuilder gsonBuilder = new GsonBuilder()
				.addSerializationExclusionStrategy(new BeanExclusionStrategy());
		synchronized (cache) {
			cache.remove(borrower.getId());
		}
		final HttpResponse resp = ServerConnection.getInstance().request("/user/updateBorrower", "POST", null,
				gsonBuilder.create().toJson(borrower));
		if (resp.getStatus() != HttpServletResponse.SC_OK) {
			throw new ConnectException("Wrong status code. Recieved was: " + resp.getStatus());
		}
		notifyListener(NavigationTreeNodeType.PERSONS);

	}

	@Override
	public Collection<Borrower> listAll() throws ConnectException {
		final HttpResponse resp = ServerConnection.getInstance().request("/user/getBorrowers", "GET", null, null);
		if (resp.getStatus() == HttpServletResponse.SC_OK) {
			final Collection<Borrower> result = new HashSet<>();
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
			return result;
		} else {
			throw new ConnectException("Wrong status code. Recieved was: " + resp.getStatus());
		}
	}

	@Override
	public Collection<Borrower> listOpen() throws ConnectException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Copy> listLent(final Borrower borrower) throws ConnectException {
		final Map<String, String> param = new HashMap<String, String>();
		param.put("id", borrower.getId().toString());
		final HttpResponse resp = ServerConnection.getInstance().request("/stock/listLendCopies", "GET", param, null);
		if (resp.getStatus() == HttpServletResponse.SC_OK) {
			final Collection<Copy> result = new HashSet<>();
			final String[] data = resp.getData().split("\n");
			for (int i = 0; i < data.length; i++) {
				final Copy copy = Utils.gson.fromJson(data[i], Copy.class);
				if (copy != null) {
					result.add(copy);
				}
			}
			return result;
		}
		return null;
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
