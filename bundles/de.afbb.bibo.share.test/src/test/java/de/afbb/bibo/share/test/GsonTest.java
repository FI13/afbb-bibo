package de.afbb.bibo.share.test;

import java.beans.PropertyChangeSupport;

import org.junit.Test;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.afbb.bibo.share.model.Borrower;
import junit.framework.Assert;

public class GsonTest {

	@Test
	public void testSerialize() {
		final Borrower borrower = new Borrower();
		final GsonBuilder gsonBuilder = new GsonBuilder().addSerializationExclusionStrategy(new ExclusionStrategy() {

			@Override
			public boolean shouldSkipField(final FieldAttributes arg0) {
				return false;
			}

			@Override
			public boolean shouldSkipClass(final Class<?> arg0) {
				return PropertyChangeSupport.class.equals(arg0);
			}
		});
		final Gson gson = gsonBuilder.create();
		final String json = gson.toJson(borrower);
		System.err.println(json);
		Assert.assertEquals(borrower, gson.fromJson(json, Borrower.class));
	}

}
