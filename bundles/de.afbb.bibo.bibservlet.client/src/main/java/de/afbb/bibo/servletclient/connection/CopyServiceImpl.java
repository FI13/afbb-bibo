package de.afbb.bibo.servletclient.connection;

import java.net.ConnectException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.afbb.bibo.share.ICopyService;
import de.afbb.bibo.share.beans.BeanExclusionStrategy;
import de.afbb.bibo.share.callback.EventListener;
import de.afbb.bibo.share.model.Copy;
import de.afbb.bibo.share.model.Medium;
import de.afbb.bibo.share.model.NavigationTreeNodeType;

public class CopyServiceImpl implements ICopyService {

	private static final Gson gson = new GsonBuilder().addSerializationExclusionStrategy(new BeanExclusionStrategy())
			.create();

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
			throw new ConnectException("Wrong status code. Recieved was: " + resp.getStatus());
		}
	}

	@Override
	public Copy get(final String barcode) throws ConnectException {
		// TODO Auto-generated method stub
		return null;
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
					gson.toJson(groupedCopies));
			if (resp.getStatus() != HttpServletResponse.SC_OK) {
				// if we get any other code than 200 we override previous, but
				// we continue with other groups
				statusCode = resp.getStatus();
			}
		}
		notifyListener(NavigationTreeNodeType.BOOKS);
		// if we got an error we throw an exception
		if (statusCode > 0) {
			throw new ConnectException("Wrong status code. Recieved was: " + statusCode);
		}

	}

	@Override
	public void returnCopies(final Collection<Copy> copies) throws ConnectException {
		// TODO Auto-generated method stub

	}

	@Override
	public void lendCopies(final Collection<Copy> copies, final boolean printList) throws ConnectException {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<Copy> listAll() throws ConnectException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Copy> listCopies(final Medium medium) throws ConnectException {
		// TODO Auto-generated method stub
		return null;
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
