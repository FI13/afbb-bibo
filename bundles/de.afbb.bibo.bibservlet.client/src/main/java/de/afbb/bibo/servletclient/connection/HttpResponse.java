/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.afbb.bibo.servletclient.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * The HttpResponse is a wrapper class for the unnecessary complicated
 * HttpUrlConnection object. It reads the HttpURLConnection and extracts the
 * response data and the response status.
 *
 * @author fi13.pendrulat
 */
public class HttpResponse {

	private String data;
	private final int status;

	/**
	 * Create a new HttpResponse object. The HttpResponse is a wrapper class for
	 * the unnecessary complicated HttpUrlConnection object. It reads the
	 * HttpURLConnection and extracts the response data and the response status.
	 *
	 * @param connection
	 * @throws IOException
	 */
	public HttpResponse(final HttpURLConnection connection) throws IOException {
		data = "";
		try {
			try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"))) {
				String inputLine;

				while ((inputLine = in.readLine()) != null) {
					data += inputLine + "\n";
				}

				if (!data.isEmpty()) {
					data = data.substring(0, data.length() - 1);
				}
			}
		} catch (final IOException e) {
			// done this way to preserve status code
		}
		status = connection.getResponseCode();
	}

	/**
	 * Get the response data.
	 *
	 * @return the response data.
	 */
	public String getData() {
		return data;
	}

	/**
	 * Get the http status code of the response
	 *
	 * @return the http response status code.
	 */
	public int getStatus() {
		return status;
	}
}
