package de.afbb.bibo.servletclient.connection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.afbb.bibo.share.beans.BeanExclusionStrategy;

/**
 *
 * @author David Becker
 *
 */
final class Utils {

	private Utils() {
	}

	public static final Gson gson = new GsonBuilder().addSerializationExclusionStrategy(new BeanExclusionStrategy())
			.setDateFormat("yyyyMMddHHmmss").create();

}
