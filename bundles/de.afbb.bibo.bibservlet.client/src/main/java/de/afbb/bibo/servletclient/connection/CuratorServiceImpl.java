/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.afbb.bibo.servletclient.connection;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.GsonBuilder;

import de.afbb.bibo.share.ICuratorService;
import de.afbb.bibo.share.beans.BeanExclusionStrategy;
import de.afbb.bibo.share.model.Curator;

/**
 * actual CU HTTP service implementation for {@link Curator}
 *
 * @author David Becker
 */
public class CuratorServiceImpl implements ICuratorService {

	private final Map<Integer, Curator> cache = new HashMap<Integer, Curator>();

	@Override
	public boolean exists(final String curatorName) throws ConnectException {
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("name", curatorName);
		final HttpResponse resp = ServerConnection.getInstance().request("/user/curatorExists", "GET", params, null);
		if (resp.getStatus() == HttpServletResponse.SC_OK) {
			return "1".equals(resp.getData());
		} else {
			final ConnectException exception = Utils.createExceptionForCode(resp.getStatus());
			if (exception != null) {
				throw exception;
			}
		}
		return false;
	}

	@Override
	public Curator get(final Integer id) throws ConnectException {
		if (id == null || id < 1) {
			return null;
		}
		synchronized (cache) {
			if (cache.containsKey(id)) {
				return cache.get(id);
			}
		}
		Curator result = null;
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", id.toString());
		final HttpResponse resp = ServerConnection.getInstance().request("/user/getCurator", "GET", params, null);
		if (resp.getStatus() == HttpServletResponse.SC_OK) {
			result = Utils.gson.fromJson(resp.getData(), Curator.class);
			synchronized (cache) {
				cache.put(id, result);
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
	public void create(final Curator curator) throws ConnectException {
		final GsonBuilder gsonBuilder = new GsonBuilder()
				.addSerializationExclusionStrategy(new BeanExclusionStrategy());
		final HttpResponse resp = ServerConnection.getInstance().request("/user/newCurator", "POST", null,
				gsonBuilder.create().toJson(curator));
		if (resp.getStatus() != HttpServletResponse.SC_OK) {
			final ConnectException exception = Utils.createExceptionForCode(resp.getStatus());
			if (exception != null) {
				throw exception;
			}
		}
	}

	@Override
	public void update(final Curator curator) throws ConnectException {
		final GsonBuilder gsonBuilder = new GsonBuilder()
				.addSerializationExclusionStrategy(new BeanExclusionStrategy());
		final HttpResponse resp = ServerConnection.getInstance().request("/user/updateCurator", "POST", null,
				gsonBuilder.create().toJson(curator));
		if (resp.getStatus() != HttpServletResponse.SC_OK) {
			final ConnectException exception = Utils.createExceptionForCode(resp.getStatus());
			if (exception != null) {
				throw exception;
			}
		}
	}

}
