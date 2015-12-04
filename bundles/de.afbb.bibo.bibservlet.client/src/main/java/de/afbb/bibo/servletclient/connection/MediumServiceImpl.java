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

import de.afbb.bibo.servletclient.ServiceLocator;
import de.afbb.bibo.share.IMediumService;
import de.afbb.bibo.share.beans.BeanExclusionStrategy;
import de.afbb.bibo.share.callback.EventListener;
import de.afbb.bibo.share.model.Borrower;
import de.afbb.bibo.share.model.Medium;

public class MediumServiceImpl implements IMediumService {

	private static final Gson gson = new GsonBuilder().addSerializationExclusionStrategy(new BeanExclusionStrategy())
			.create();

	private final Set<EventListener> listeners = new HashSet<EventListener>();

	@Override
	public void update(final Medium medium) throws ConnectException {
		// TODO Auto-generated method stub
		// currently never called
		throw new UnsupportedOperationException("not implemented");
	}

	@Override
	public void create(final Medium medium) throws ConnectException {
		// TODO Auto-generated method stub
		// currently never called
		throw new UnsupportedOperationException("not implemented");
	}

	@Override
	public Medium getMedium(final int id) throws ConnectException {
		// TODO Auto-generated method stub
		// currently never called
		throw new UnsupportedOperationException("not implemented");
	}

	@Override
	public Medium getMedium(final String isbn) throws ConnectException {
		final Map<String, String> param = new HashMap<String, String>();
		param.put("ISBN", isbn);
		final HttpResponse resp = ServerConnection.getInstance().request("/stock/getMedium", "GET", param, null);
		if (resp.getStatus() == HttpServletResponse.SC_OK) {
			final Medium medium = gson.fromJson(resp.getData(), Medium.class);
			if (medium != null) {
				// we only get the id for type filled, so we need to fetch type
				// separately
				medium.setType(ServiceLocator.getInstance().getTypService().get(medium.getType().getId()));
			}
			return medium;
		} else if (resp.getStatus() == HttpServletResponse.SC_NOT_FOUND) {
			return null;
		} else {
			throw new ConnectException("Wrong status code. Recieved was: " + resp.getStatus());
		}
	}

	@Override
	public Collection<Borrower> listLent(final String isbn) throws ConnectException {
		// TODO Auto-generated method stub
		// currently never called
		throw new UnsupportedOperationException("not implemented");
	}

	@Override
	public Collection<Medium> list() throws ConnectException {
		final HttpResponse resp = ServerConnection.getInstance().request("/stock/listMedia", "GET", null, null);
		if (resp.getStatus() == HttpServletResponse.SC_OK) {
			final Collection<Medium> result = new HashSet<>();
			final String[] data = resp.getData().split("\n");
			for (int i = 0; i < data.length; i++) {
				final Medium medium = gson.fromJson(data[i], Medium.class);
				if (medium != null) {
					result.add(medium);
				}
			}
			return result;
		} else {
			throw new ConnectException("Wrong status code. Recieved was: " + resp.getStatus());
		}
	}

	@Override
	public void delete(final Medium medium) throws ConnectException {
		// currently never called
		throw new UnsupportedOperationException("not implemented");
	}

	@Override
	public void register(final EventListener listener) {
		listeners.add(listener);
	}

}
