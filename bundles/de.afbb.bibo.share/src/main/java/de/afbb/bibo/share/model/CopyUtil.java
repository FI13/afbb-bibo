package de.afbb.bibo.share.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public final class CopyUtil {

	private CopyUtil() {
	}

	public static Object copy(final Serializable input) {

		try {
			final ByteArrayOutputStream bos = new ByteArrayOutputStream();
			final ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(input);

			final byte[] data = bos.toByteArray();

			final ByteArrayInputStream bis = new ByteArrayInputStream(data);
			final ObjectInputStream ois = new ObjectInputStream(bis);

			return ois.readObject();
		} catch (final ClassNotFoundException e) {
		} catch (final IOException e) {
		}
		return null;
	}
}
