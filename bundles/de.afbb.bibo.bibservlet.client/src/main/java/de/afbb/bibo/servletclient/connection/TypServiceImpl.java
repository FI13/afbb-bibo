package de.afbb.bibo.servletclient.connection;

import java.net.ConnectException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.afbb.bibo.share.ITypService;
import de.afbb.bibo.share.beans.BeanExclusionStrategy;
import de.afbb.bibo.share.model.MediumType;

/**
 * Implementation of HTTP service for {@link MediumType}
 *
 * @author David Becker
 *
 */
public class TypServiceImpl implements ITypService {

	private static final Gson gson = new GsonBuilder().addSerializationExclusionStrategy(new BeanExclusionStrategy())
			.create();

	private final Map<Integer, MediumType> cache = new HashMap<Integer, MediumType>();

	@Override
	public void create(final MediumType type) throws ConnectException {
		final HttpResponse resp = ServerConnection.getInstance().request("/stock/addMediaType", "POST", null,
				gson.toJson(type));
		if (resp.getStatus() != HttpServletResponse.SC_OK) {
			final ConnectException exception = Utils.createExceptionForCode(resp.getStatus());
			if (exception != null) {
				throw exception;
			}
		}
	}

	@Override
	public Collection<MediumType> list() throws ConnectException {
		final Collection<MediumType> result = new HashSet<>();
		final HttpResponse resp = ServerConnection.getInstance().request("/stock/listMediaTypes", "GET", null, null);
		if (resp.getStatus() == HttpServletResponse.SC_OK) {
			synchronized (cache) {
				cache.clear();
				final String[] data = resp.getData().split("\n");
				for (int i = 0; i < data.length; i++) {
					final MediumType type = gson.fromJson(data[i], MediumType.class);
					if (type != null) {
						result.add(type);
						cache.put(type.getId(), type);
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
	public MediumType get(final Integer id) throws ConnectException {
		if (id == null || id < 1) {
			return null;
		}
		synchronized (cache) {
			if (cache.containsKey(id)) {
				return cache.get(id);
			}
		}
		MediumType type = null;
		final Map<String, String> param = new HashMap<String, String>();
		param.put("id", id.toString());
		final HttpResponse resp = ServerConnection.getInstance().request("/stock/getMediaType", "GET", param, null);
		if (resp.getStatus() == HttpServletResponse.SC_OK) {
			type = gson.fromJson(resp.getData(), MediumType.class);
			synchronized (cache) {
				cache.put(type.getId(), type);
			}
		} else {
			final ConnectException exception = Utils.createExceptionForCode(resp.getStatus());
			if (exception != null) {
				throw exception;
			}
		}
		return type;
	}

}
