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

	@Override
	public void create(final MediumType type) throws ConnectException {
		final HttpResponse resp = ServerConnection.getInstance().request("/stock/addMediaType", "POST", null,
				gson.toJson(type));
		if (resp.getStatus() != HttpServletResponse.SC_OK) {
			throw new ConnectException("Wrong status code. Recieved was: " + resp.getStatus());
		}
	}

	@Override
	public Collection<MediumType> list() throws ConnectException {
		final HttpResponse resp = ServerConnection.getInstance().request("/stock/listMediaTypes", "GET", null, null);
		if (resp.getStatus() == HttpServletResponse.SC_OK) {
			final Collection<MediumType> result = new HashSet<>();
			final String[] data = resp.getData().split("\n");
			for (int i = 0; i < data.length; i++) {
				result.add(gson.fromJson(data[i], MediumType.class));
			}
			return result;
		} else {
			throw new ConnectException("Wrong status code. Recieved was: " + resp.getStatus());
		}
	}

	@Override
	public MediumType get(final Integer id) throws ConnectException {
		final Map<String, String> param = new HashMap<String, String>();
		param.put("id", id.toString());
		final HttpResponse resp = ServerConnection.getInstance().request("/stock/getMediaType", "GET", param, null);
		if (resp.getStatus() == HttpServletResponse.SC_OK) {
			return gson.fromJson(resp.getData(), MediumType.class);
		} else {
			throw new ConnectException("Wrong status code. Recieved was: " + resp.getStatus());
		}
	}

}
