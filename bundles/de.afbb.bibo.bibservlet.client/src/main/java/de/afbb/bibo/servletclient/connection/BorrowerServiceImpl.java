package de.afbb.bibo.servletclient.connection;

import java.net.ConnectException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
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

	private static final Gson gson = new GsonBuilder().addSerializationExclusionStrategy(new BeanExclusionStrategy())
			.create();

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
				gson.toJson(borrower));
		if (resp.getStatus() != HttpServletResponse.SC_OK) {
			throw new ConnectException("Wrong status code. Recieved was: " + resp.getStatus());
		}
		notifyListener(NavigationTreeNodeType.PERSONS);
	}

	@Override
	public void update(final Borrower borrower) throws ConnectException {
		final GsonBuilder gsonBuilder = new GsonBuilder()
				.addSerializationExclusionStrategy(new BeanExclusionStrategy());
		final HttpResponse resp = ServerConnection.getInstance().request("/user/updateBorrower", "POST", null,
				gsonBuilder.create().toJson(borrower));
		if (resp.getStatus() != HttpServletResponse.SC_OK) {
			throw new ConnectException("Wrong status code. Recieved was: " + resp.getStatus());
		}
		notifyListener(NavigationTreeNodeType.PERSONS);

	}

	@Override
	public Collection<Borrower> listAll() throws ConnectException {
		final HttpResponse resp = ServerConnection.getInstance().request("/user/getBorrower", "GET", null, null);
		if (resp.getStatus() == HttpServletResponse.SC_OK) {
			final Collection<Borrower> result = new HashSet<>();
			final String[] data = resp.getData().split("\n");
			for (int i = 0; i < data.length; i++) {
				final Borrower borrower = gson.fromJson(data[i], Borrower.class);
				if (borrower != null) {
					result.add(borrower);
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
	public Collection<Copy> listLent(final Borrower Borrower) throws ConnectException {
		// TODO Auto-generated method stub
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
