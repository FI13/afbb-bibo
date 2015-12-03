package de.afbb.bibo.servletclient.connection;

import java.beans.PropertyChangeSupport;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;

/**
 * {@link ExclusionStrategy} that allows {@link Gson} to serialize our model
 * classes
 *
 * @author david
 *
 */
public class BeanExclusionStrategy implements ExclusionStrategy {

	@Override
	public boolean shouldSkipField(final FieldAttributes arg0) {
		return arg0.getName().equals("password");
	}

	@Override
	public boolean shouldSkipClass(final Class<?> arg0) {
		return PropertyChangeSupport.class.equals(arg0);
	}

}
