package de.afbb.bibo.servlet.server;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.afbb.bibo.share.beans.BeanExclusionStrategy;

/**
 * collection of utility methods
 *
 * @author fi13.pendrulat
 */
public final class Utils {

	private Utils() {
	}

	/**
	 * no need to create multiple {@link Gson} objects all the time
	 *
	 * @see https://sites.google.com/site/gson/gson-user-guide#TOC-Using-Gson
	 */
	public static final Gson gson = new GsonBuilder().addSerializationExclusionStrategy(new BeanExclusionStrategy())
			.setDateFormat("yyyyMMddHHmmss").create();

	public static int nthOccurrence(final String str, final String toFind, int n) {
		int pos = str.indexOf(toFind, 0);
		while (n-- > 0 && pos != -1) {
			pos = str.indexOf(toFind, pos + 1);
		}
		return pos;
	}

	public static String getRequestPart(final HttpServletRequest request, final int part) {
		final String[] requestParts = request.getRequestURI().split("/");
		return "/" + (requestParts.length > 1 ? requestParts[part + 1] : "");
	}

	public static void returnErrorMessage(final Class<?> servlet, final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		response.getWriter().println("the servlet: " + servlet.getSimpleName() + " could not serve your request: "
				+ request.getRequestURI());
	}
}
